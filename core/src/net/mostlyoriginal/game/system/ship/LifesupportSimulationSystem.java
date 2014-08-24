package net.mostlyoriginal.game.system.ship;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.ship.CrewMember;

/**
 * Lifesupport simulation for the crew.
 * <p/>
 * Should be called manually by the travel simulation.
 *
 * @author Daan van Yperen
 * @todo this is really a subsystem of the travel simulation system.
 */
@Wire
public class LifesupportSimulationSystem extends EntityProcessingSystem {

    public static final float CREW_FED_PER_FOOD = 1.25f;
    protected ComponentMapper<CrewMember> mCrewMember;
    public InventorySystem inventorySystem;
    protected CrewSystem crewSystem;

    private int crewThatAte;
    private int totalEaters;

    /**
     * Total food available
     */
    private float foodFactor;

    public LifesupportSimulationSystem() {
        super(Aspect.getAspectForAll(CrewMember.class));
    }

    @Override
    protected void begin() {
        super.begin();

        totalEaters = crewSystem.countOf(CrewMember.Ability.EAT);

        foodFactor = (inventorySystem.get(InventorySystem.Resource.FOOD) * CREW_FED_PER_FOOD) / (float) totalEaters;

        crewThatAte = 0;

        System.out.println(foodFactor);

    }

    @Override
    protected void end() {
        super.end();

        if (crewThatAte > 0) {
            // two crew members eat 0.5 - 1.5 food.
            inventorySystem.alter(InventorySystem.Resource.FOOD, -(int) MathUtils.random(crewThatAte / CREW_FED_PER_FOOD, crewThatAte / CREW_FED_PER_FOOD + 1f));
        }
    }

    @Override
    protected void process(Entity e) {

        CrewMember crewMember = mCrewMember.get(e);
        switch (crewMember.effect) {
            case HEALTHY:
                attemptEat(crewMember, CrewMember.Effect.HUNGRY);
                break;
            case HUNGRY:
                attemptEat(crewMember, CrewMember.Effect.STARVING);
                break;
            case STARVING:
                attemptEat(crewMember, CrewMember.Effect.DEAD);
                break;
            case DEAD:
                break;
        }


    }

    /**
     * Crewmember attempts eating, or grows hungry, depending on food available.
     */
    private void attemptEat(CrewMember crewMember, CrewMember.Effect newEffect) {

        // progress to next state, there is a 25% chance of not needing food, to give the ship a chance.
        if (MathUtils.random(0f, 0.99f) > foodFactor + 0.25f) {
            changeState(crewMember, newEffect);
        } else {
            crewThatAte++;
            changeState(crewMember, CrewMember.Effect.HEALTHY);
        }
    }

    private void changeState(CrewMember crewMember, CrewMember.Effect newEffect) {
        crewMember.effect = newEffect;
    }
}