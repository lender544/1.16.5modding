package L_Ender.cataclysm.blocks;

import L_Ender.cataclysm.init.ModTag;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class TrapBlock extends Block {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;


    public TrapBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(LIT, Boolean.valueOf(false)));
    }


    public boolean ticksRandomly(BlockState state) {
        return state.get(LIT);
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            worldIn.setBlockState(pos, state.with(LIT, Boolean.valueOf(false)), 3);
        }

    }
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            spawnParticles(worldIn, pos);
        }


    }

    private static void spawnParticles(World world, BlockPos worldIn) {
        double d0 = 0.5625D;
        Random random = world.rand;

        for(Direction direction : Direction.values()) {
            BlockPos blockpos = worldIn.offset(direction);
            if (!world.getBlockState(blockpos).isOpaqueCube(world, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double)direction.getXOffset() : (double)random.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double)direction.getYOffset() : (double)random.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double)direction.getZOffset() : (double)random.nextFloat();
                world.addParticle(ParticleTypes.REVERSE_PORTAL, (double)worldIn.getX() + d1, (double)worldIn.getY() + d2, (double)worldIn.getZ() + d3, 0.0D, 0.0D, 0.0D);
            }
        }


    }

    public static boolean shouldTrigger(Entity entity) {
        if(entity instanceof LivingEntity) {
            if (!EntityTypeTags.getCollection().get(ModTag.TRAP_BLOCK_NOT_DETECTED).contains(entity.getType())) {
                if (entity instanceof PlayerEntity) {
                    return !((PlayerEntity) entity).isCreative() && !entity.isSpectator();
                } else return !(entity instanceof ArmorStandEntity);
            }
        }
        return false;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
