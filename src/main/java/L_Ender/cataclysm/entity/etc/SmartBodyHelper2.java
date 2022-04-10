package L_Ender.cataclysm.entity.etc;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.util.math.MathHelper;

public class SmartBodyHelper2 extends BodyController {
    private final MobEntity mob;
    private static final float MAX_ROTATE = 75;
    private int rotationTickCounter;
    private static final int HISTORY_SIZE = 10;
    private float prevRenderYawHead;
    private final double[] histPosX = new double[HISTORY_SIZE];
    private final double[] histPosZ = new double[HISTORY_SIZE];

    public SmartBodyHelper2(MobEntity mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void updateRenderAngles() {
        for (int i = histPosX.length - 1; i > 0; i--) {
            histPosX[i] = histPosX[i - 1];
            histPosZ[i] = histPosZ[i - 1];
        }
        histPosX[0] = mob.getPosX();
        histPosZ[0] = mob.getPosZ();
        if (this.hasMoved()) {
            this.mob.renderYawOffset = this.mob.rotationYaw;
            this.func_220664_c();
            this.prevRenderYawHead = this.mob.rotationYawHead;
            this.rotationTickCounter = 0;
        } else {
            if (this.noMobPassengers()) {
                if (Math.abs(this.mob.rotationYawHead - this.prevRenderYawHead) > 15.0F) {
                    this.rotationTickCounter = 0;
                    this.prevRenderYawHead = this.mob.rotationYawHead;
                    this.func_220663_b();
                } else {
                    float limit = MAX_ROTATE;
                    ++this.rotationTickCounter;
                    final int speed = 10;
                    if (this.rotationTickCounter > speed) {
                        limit = Math.max(1 - (rotationTickCounter - speed) / (float) speed, 0) * MAX_ROTATE;
                    }

                    mob.renderYawOffset = approach(mob.rotationYawHead, mob.renderYawOffset, limit);// https://gist.github.com/TheGreyGhost/b5ea2acd1c651a2d6350#file-gistfile1-txt-L60
                }
            }
        }
    }

    private void func_220663_b() {
        this.mob.renderYawOffset = MathHelper.func_219800_b(this.mob.renderYawOffset, this.mob.rotationYawHead, (float)this.mob.getHorizontalFaceSpeed());
    }

    private void func_220664_c() {
        this.mob.rotationYawHead = MathHelper.func_219800_b(this.mob.rotationYawHead, this.mob.renderYawOffset, (float)this.mob.getHorizontalFaceSpeed());
    }

    private boolean noMobPassengers() {
        return this.mob.getPassengers().isEmpty() || !(this.mob.getPassengers().get(0) instanceof MobEntity);
    }

    private boolean hasMoved() {
        double d0 = this.mob.getPosX() - this.mob.prevPosX;
        double d1 = this.mob.getPosZ() - this.mob.prevPosZ;
        return d0 * d0 + d1 * d1 > (double)2.5000003E-7F;
    }

    public static float approach(float target, float current, float limit) {
        float delta = MathHelper.wrapDegrees(current - target);
        if (delta < -limit) {
            delta = -limit;
        } else if (delta >= limit) {
            delta = limit;
        }
        return target + delta * 0.55F;
    }
}
