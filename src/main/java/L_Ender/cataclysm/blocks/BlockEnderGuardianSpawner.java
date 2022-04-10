package L_Ender.cataclysm.blocks;

import L_Ender.cataclysm.tileentities.TileEntityEnderGuardianSpawner;
import L_Ender.cataclysm.tileentities.TileEntityObsidianExplosionTrapBricks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockEnderGuardianSpawner extends Block {


	public BlockEnderGuardianSpawner(Properties props) {
		super(props);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState p_200122_1_, BlockState p_200122_2_, Direction p_200122_3_) {
		return p_200122_2_.getBlock() == this ? true : super.isSideInvisible(p_200122_1_, p_200122_2_, p_200122_3_);
	}


	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityEnderGuardianSpawner();
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return state.getBlockHardness(world, pos) >= 0f;
	}

	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}
}
