package L_Ender.cataclysm.entity.AI;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class CmAttackGoal extends MeleeAttackGoal {
    private LivingEntity target;
    private int delayCounter;
    protected final double moveSpeed;



    public CmAttackGoal(CreatureEntity creatureEntity, double moveSpeed) {
        super(creatureEntity, moveSpeed, true);
        this.moveSpeed = moveSpeed;
    }

    @Override
    public boolean shouldExecute() {
        this.target = this.attacker.getAttackTarget();
        return this.target != null && target.isAlive();
    }

    @Override
    public void resetTask() {
        this.attacker.setAggroed(false);
        this.attacker.getNavigator().clearPath();
    }



    @Override
    public void tick() {
        LivingEntity target = this.attacker.getAttackTarget();
        if (target == null) {
            return;
        }
        this.attacker.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
        double distSq = this.attacker.getDistanceSq(target.getPosX(), target.getBoundingBox().minY, target.getPosZ());
        if (--this.delayCounter <= 0) {
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
            if (distSq > Math.pow(this.attacker.getAttribute(Attributes.FOLLOW_RANGE).getValue(), 2.0D)) {
                if (!this.attacker.hasPath()) {
                    if (!this.attacker.getNavigator().tryMoveToEntityLiving(target, 1.0D)) {
                        this.delayCounter += 5;
                    }
                }
            } else {
                this.attacker.getNavigator().tryMoveToEntityLiving(target, this.moveSpeed);
            }
        }
    }
}

