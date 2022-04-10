package L_Ender.cataclysm.blocks;

import L_Ender.cataclysm.tileentities.TileEntityObsidianExplosionTrapBricks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ObsidianExplosionTrapBricks extends TrapBlock {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public ObsidianExplosionTrapBricks(AbstractBlock.Properties properties) {
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
            world.setBlockState(pos, state.with(LIT, Boolean.valueOf(true)), 3);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityObsidianExplosionTrapBricks();
    }

}