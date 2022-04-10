package L_Ender.cataclysm.entity.AI;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

class AttackMoveGoal extends Goal  {

    private CreatureEntity creatureEntity;
    private LivingEntity target;
    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;


    public AttackMoveGoal(CreatureEntity creatureEntity) {
        this.creatureEntity = creatureEntity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        this.target = creatureEntity.getAttackTarget();
        return this.target != null && target.isAlive(); //&& this.creatureEntity.getAnimation() == IAnimatedEntity.NO_ANIMATION;
    }

    @Override
    public void startExecuting() {
        this.repath = 0;
    }

    @Override
    public void resetTask() {
        this.creatureEntity.getNavigator().clearPath();
    }

    @Override
    public void tick() {
        LivingEntity target = this.creatureEntity.getAttackTarget();
        if (target == null) return;
        double dist = this.creatureEntity.getDistanceSq(this.targetX, this.targetY, this.targetZ);
        this.creatureEntity.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
                this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                        target.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                this.creatureEntity.getNavigator().noPath()) {
            this.targetX = target.getPosX();
            this.targetY = target.getPosY();
            this.targetZ = target.getPosZ();
            this.repath = 4 + this.creatureEntity.getRNG().nextInt(7);
            if (dist > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (dist > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.creatureEntity.getNavigator().tryMoveToEntityLiving(target, 1.0D)) {
                this.repath += 15;
            }
        }
    }
}
