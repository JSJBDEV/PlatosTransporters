package gd.rf.acro.platos.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class NotFullBlock extends Block {


    public NotFullBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return Block.makeCuboidShape(1d,0d,1d,15d,15d,15d);
    }
    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return Block.makeCuboidShape(1d,0d,1d,15d,15d,15d);
    }
}
