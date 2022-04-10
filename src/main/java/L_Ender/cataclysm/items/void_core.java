package L_Ender.cataclysm.items;

import L_Ender.cataclysm.entity.projectile.Void_Rune_Entity;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class void_core extends Item {

    public void_core(Properties group) {
        super(group);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        int standingOnY = MathHelper.floor(player.getPosY()) - 1;
        double headY = player.getPosY() + 1.0D;
        float yawRadians = (float) (Math.toRadians(90 + player.rotationYaw));
        boolean hasSucceeded = false;
        if (player.rotationPitch > 70) {
            for (int i = 0; i < 5; i++) {
                float rotatedYaw = yawRadians + (float) i * (float) Math.PI * 0.4F;
                if (this.spawnFangs(player.getPosX() + (double) MathHelper.cos(rotatedYaw) * 1.5D, headY, player.getPosZ() + (double) MathHelper.sin(rotatedYaw) * 1.5D, standingOnY, rotatedYaw, 0, world, player))
                    hasSucceeded = true;

            }
            for (int k = 0; k < 8; k++) {
                float rotatedYaw = yawRadians + (float) k * (float) Math.PI * 2.0F / 8.0F + 1.2566371F;
                if (this.spawnFangs(player.getPosX() + (double) MathHelper.cos(rotatedYaw) * 2.5D, headY, player.getPosZ() + (double) MathHelper.sin(rotatedYaw) * 2.5D, standingOnY, rotatedYaw, 3, world, player))
                    hasSucceeded = true;

            }
        } else {
            for (int l = 0; l < 10; l++) {
                double d2 = 1.25D * (double) (l + 1);
                if(this.spawnFangs(player.getPosX() + (double) MathHelper.cos(yawRadians) * d2, headY, player.getPosZ() + (double) MathHelper.sin(yawRadians) * d2, standingOnY, yawRadians, l, world, player))
                    hasSucceeded = true;

            }
        }
        ItemStack stack = player.getHeldItem(hand);
        if (hasSucceeded) {
            player.getCooldownTracker().setCooldown(this, 120);
            return ActionResult.resultSuccess(stack);
        }
        return ActionResult.resultPass(stack);
    }

    private boolean spawnFangs(double x, double y, double z, int lowestYCheck, float rotationYaw, int warmupDelayTicks, World world, PlayerEntity player) {
        BlockPos blockpos = new BlockPos(x, y, z);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.down();
            BlockState blockstate = world.getBlockState(blockpos1);
            if (blockstate.isSolidSide(world, blockpos1, Direction.UP)) {
                if (!world.isAirBlock(blockpos)) {
                    BlockState blockstate1 = world.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShapeUncached(world, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.getEnd(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.down();
        } while (blockpos.getY() >= lowestYCheck);

        if (flag) {
            world.addEntity(new Void_Rune_Entity(world, x, (double) blockpos.getY() + d0, z, rotationYaw, warmupDelayTicks, player));
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.cataclysm.void_core.desc").mergeStyle(TextFormatting.DARK_GREEN));
    }
}

