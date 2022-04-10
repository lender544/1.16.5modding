package L_Ender.cataclysm.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EndStoneTeleportTrapBricks extends TrapBlock {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public EndStoneTeleportTrapBricks(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(LIT, Boolean.valueOf(false)));
    }

    /**
     * Called when the given entity walks on this Block
     */
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        activate(worldIn.getBlockState(pos), worldIn, pos, entityIn);
        super.onEntityWalk(worldIn, pos, entityIn);
    }


    private static void activate(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!state.get(LIT) && shouldTrigger(entity)) {

            double d0 = entity.getPosX() + (entity.world.rand.nextDouble() - 0.5D) * 16.0D;
            double d1 = entity.getPosY();
            double d2 = entity.getPosZ() + (entity.world.rand.nextDouble() - 0.5D) * 16.0D;
            ((LivingEntity)entity).attemptTeleport(d0, d1, d2,false);

            ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.BLINDNESS, 25));
            world.setBlockState(pos, state.with(LIT, Boolean.valueOf(true)), 3);
            world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

}
