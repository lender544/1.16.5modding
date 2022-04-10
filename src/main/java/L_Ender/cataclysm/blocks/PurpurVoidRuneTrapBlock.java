package L_Ender.cataclysm.blocks;

import L_Ender.cataclysm.entity.projectile.Void_Rune_Entity;
import L_Ender.cataclysm.init.ModEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PurpurVoidRuneTrapBlock extends TrapBlock {
    //The code and texture were brought from savage and ravage. Thx abnormal
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public PurpurVoidRuneTrapBlock(AbstractBlock.Properties properties) {
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
            Void_Rune_Entity voidrune = ModEntities.VOID_RUNE.get().create(world);
            if (voidrune != null) {
                voidrune.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0F, 0.0F);
                world.addEntity(voidrune);
            }
            ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 50,3));
            world.setBlockState(pos, state.with(LIT, Boolean.valueOf(true)), 3);
        }
    }
}
