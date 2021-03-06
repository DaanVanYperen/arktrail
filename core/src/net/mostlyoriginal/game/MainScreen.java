package net.mostlyoriginal.game;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.managers.UuidEntityManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.event.common.EventManager;
import net.mostlyoriginal.api.system.camera.CameraShakeSystem;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.camera.EntityCameraSystem;
import net.mostlyoriginal.api.system.graphics.ColorAnimationSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.api.system.mouse.MouseCursorSystem;
import net.mostlyoriginal.api.system.physics.*;
import net.mostlyoriginal.api.system.render.AnimRenderSystem;
import net.mostlyoriginal.api.system.script.EntitySpawnerSystem;
import net.mostlyoriginal.api.system.script.SchedulerSystem;
import net.mostlyoriginal.game.manager.AssetSystem;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.manager.FontManager;
import net.mostlyoriginal.game.system.render.BarRenderSystem;
import net.mostlyoriginal.game.system.render.LabelRenderSystem;
import net.mostlyoriginal.game.system.ship.*;
import net.mostlyoriginal.game.system.tutorial.TutorialSystem;
import net.mostlyoriginal.game.system.ui.*;

/**
 * @author Daan van Yperen
 */
public class MainScreen implements Screen {

    public static final int CAMERA_ZOOM_FACTOR = 4;
    private final World world;

    public MainScreen() {

        world = new World();

        /** UTILITY - MANAGERS */

        world.setManager(new GroupManager());
        world.setManager(new TagManager());
        world.setManager(new UuidEntityManager());
        world.setManager(new FontManager());

	    world.setManager(new EventManager());

        /** UTILITY - PASSIVE */

        //world.setSystem(new CollisionSystem());
        world.setSystem(new InventorySystem());
        world.setSystem(new EntityFactorySystem());
        world.setSystem(new ConstructionSystem());
        world.setSystem(new ShipComponentSystem());
        world.setSystem(new HullSystem());
        //world.setSystem(new TiledMapSystem("level1.tmx"));
        world.setSystem(new AssetSystem());
        world.setSystem(new CameraSystem(CAMERA_ZOOM_FACTOR));

        /** CONTROL */
        world.setSystem(new RouteSystem());
        world.setSystem(new TravelSimulationSystem());
        /* */ world.setSystem(new LifesupportSimulationSystem(),true);
        /* */ world.setSystem(new ProductionSimulationSystem(),true);
        world.setSystem(new DilemmaSystem());
        world.setSystem(new CrewSystem());

        // control systems.
        /** Agency Systems (Control and Interact) */
        //world.setSystem(new PlayerControlSystem());
        //world.setSystem(new SlumbererSystem());

        /** Acting Systems (Control and Interact) */
        //world.setSystem(new PluckableSystem());
        world.setSystem(new SchedulerSystem());
        world.setSystem(new EntitySpawnerSystem());

        world.setSystem(new MouseCursorSystem());
        world.setSystem(new MouseClickSystem());
        world.setSystem(new ButtonSystem());
        world.setSystem(new TutorialSystem());

        /** SIMULATE */


        /** Physics systems that apply a vector on an entity */
        world.setSystem(new HomingSystem());
        world.setSystem(new GravitySystem());
        /** Physics systems that constrain the movement*/
        world.setSystem(new CollisionSystem());
        //world.setSystem(new ClampedSystem());
        /** Physics systems that move the entity to an absolute location. */
        world.setSystem(new AttachmentSystem());
        world.setSystem(new InbetweenSystem());
        world.setSystem(new GateSystem());


        /** apply velocity */
        world.setSystem(new PhysicsSystem());

        /** Post Physics Simulations */
        //world.setSystem(new AimSystem());
        //world.setSystem(new MapWallSensorSystem());


        /** PRE-RENDER */

        world.setSystem(new ColorAnimationSystem());
        world.setSystem(new AccelerationEffectSystem());
        world.setSystem(new EngineEffectSystem());

        /** RENDER */

        /** Camera */
        world.setSystem(new EntityCameraSystem());
        world.setSystem(new CameraShakeSystem());

        /** Rendering */

        final RenderBatchingSystem renderBatchingSystem = new RenderBatchingSystem();
        world.setSystem(renderBatchingSystem);
        world.setSystem(new AnimRenderSystem(renderBatchingSystem), true);
        world.setSystem(new LabelRenderSystem(renderBatchingSystem), true);
        world.setSystem(new BarRenderSystem(renderBatchingSystem), true);

        world.initialize();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
  		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // limit world delta to prevent clipping through walls.
        world.setDelta(MathUtils.clamp(delta, 0, 1 / 15f));
        world.process();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
