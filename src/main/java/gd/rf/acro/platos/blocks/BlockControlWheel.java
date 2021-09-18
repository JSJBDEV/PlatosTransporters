package gd.rf.acro.platos.blocks;

import gd.rf.acro.platos.ConfigUtils;
import gd.rf.acro.platos.PlatosTransporters;
import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlockControlWheel extends HorizontalBlock {

    public BlockControlWheel(Properties p_i48339_1_) {
        super(p_i48339_1_);
    }



    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return Block.makeCuboidShape(1d,0d,1d,15d,8d,15d);
    }



    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(hand==Hand.MAIN_HAND && !world.isRemote)
        {
            int balloons = Integer.parseInt(ConfigUtils.config.get("balloon"));
            int floats = Integer.parseInt(ConfigUtils.config.get("float"));
            int wheels = Integer.parseInt(ConfigUtils.config.get("wheel"));
            String whitelist = ConfigUtils.config.getOrDefault("whitelist","false");
            int type = -1;
            int blocks = 0;
            int balances = 0;
            HashMap<String,Integer> used = new HashMap<>();
            ListNBT list = new ListNBT();
            CompoundNBT storage = new CompoundNBT();
            ListNBT addons = new ListNBT();
            List<Integer[]> filtered = new ArrayList<>();
            List<Integer[]> accepted = new ArrayList<>();
            int mposx = 3;
            int mposz = 3;
            int nposx = -3;
            int nposz = -3;
            int mposy = 3;
            int nposy = -3;
            filtered.add(new Integer[]{0,0,0});

            while(!filtered.isEmpty())
            {
                Integer[] thisPos = filtered.get(0);
                BlockPos gpos = pos.add(thisPos[0],thisPos[1],thisPos[2]);
                for (int i = -2; i < 3; i++) {
                    for (int j = -2; j < 3; j++) {
                        for (int k = -2; k < 3; k++)
                        {
                            if(!world.getBlockState(gpos.add(i, j, k)).isAir() && !world.getBlockState(gpos.add(i, j, k)).getBlock().getTranslationKey().contains("ore") && world.getBlockState(gpos.add(i, j, k)).getFluidState().isEmpty())
                            {
                                if ((whitelist.equals("true") && PlatosTransporters.BOAT_MATERIAL.contains(world.getBlockState(gpos.add(i, j, k)).getBlock())
                                ) || (whitelist.equals("false") && !PlatosTransporters.BOAT_MATERIAL_BLACKLIST.contains(world.getBlockState(gpos.add(i, j, k)).getBlock())))
                                {
                                    Integer[] passable = new Integer[]{thisPos[0]+i,thisPos[1]+j,thisPos[2]+k};
                                    if(i==0 && j==0 && k==0)
                                    {
                                        //System.out.println("centre block skipping");
                                    }
                                    else if(filtered.stream().anyMatch(inside-> Arrays.equals(inside,passable)))
                                    {

                                        //System.out.println("already added, skipping");
                                    }
                                    else if(accepted.stream().anyMatch(inside-> Arrays.equals(inside,passable)))
                                    {
                                        //System.out.println("already accepted, skipping");
                                    }
                                    else
                                    {
                                        filtered.add(passable);
                                        if(passable[0]>mposx)
                                        {
                                            mposx=passable[0];
                                        }
                                        if(passable[0]<nposx)
                                        {
                                            nposx=passable[0];
                                        }
                                        if(passable[1]>mposy)
                                        {
                                            mposy=passable[1];
                                        }
                                        if(passable[1]<nposy)
                                        {
                                            nposy=passable[1];
                                        }
                                        if(passable[2]>mposz)
                                        {
                                            mposz=passable[2];
                                        }
                                        if(passable[2]<nposz)
                                        {
                                            nposz=passable[2];
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                accepted.add(filtered.remove(0));
            }

            for (Integer[] integers : accepted) {
                {
                    int i = integers[0];
                    int j = integers[1];
                    int k = integers[2];
                    addIfCan(used, world.getBlockState(pos.add(i, j, k)).getBlock().getTranslationKey(), 1);
                    list.add(StringNBT.valueOf(
                            Block.getStateId(world.getBlockState(pos.add(i, j, k))) + " " + i + " " + j + " " + k));
                    blocks++;

                    if(world.getBlockState(pos.add(i, j, k)).getBlock()==Blocks.BLAST_FURNACE)
                    {
                        addons.add(StringNBT.valueOf("engine"));
                    }
                    if(world.getBlockState(pos.add(i, j, k)).getBlock()==Blocks.ANVIL)
                    {
                        addons.add(StringNBT.valueOf("altitude"));
                    }

                    if (world.getTileEntity(pos.add(i, j, k)) != null) {
                        CompoundNBT data = world.getTileEntity(pos.add(i, j, k)).write(new CompoundNBT());
                        storage.put(i + " " + j + " " + k, data);
                    }


                    if (world.getBlockState(pos.add(i, j, k)).getBlock() == PlatosTransporters.FLOAT_BLOCK && (type == 0 || type == -1)) {
                        type = 0; //watership
                        balances += floats;

                    }
                    if (world.getBlockState(pos.add(i, j, k)).getBlock() == PlatosTransporters.BALLOON_BLOCK && (type == 1 || type == -1)) {
                        type = 1; //airship
                        balances += balloons;

                    }
                    if (world.getBlockState(pos.add(i, j, k)).getBlock() == PlatosTransporters.WHEEL_BLOCK && (type == 2 || type == -1)) {
                        type = 2; //carriage
                        balances += wheels;
                    }
                }

            }
            System.out.println("blocks: "+blocks);
            System.out.println("balances: "+balances);
            if(type==-1)
            {
                player.sendMessage(new StringTextComponent("No wheel/float/balloon found"));
                return ActionResultType.FAIL;
            }
            if(balances<blocks)
            {
                player.sendMessage(new StringTextComponent("Cannot assemble, not enough floats/balloons/wheels"));
                used.keySet().forEach(key->
                {
                    player.sendMessage(new StringTextComponent(key+": "+used.get(key)));
                });
                player.sendMessage(new StringTextComponent("If you believe any of the above blocks was added in error report it on CurseForge!"));
                return ActionResultType.FAIL;
            }
            list.forEach(block->
            {
               String[] vv = block.getString().split(" ");
               if(world.getTileEntity(pos.add(Integer.parseInt(vv[1]),Integer.parseInt(vv[2]),Integer.parseInt(vv[3])))!=null)
               {
                   
                   IClearable.clearObj(world.getTileEntity(pos.add(Integer.parseInt(vv[1]),Integer.parseInt(vv[2]),Integer.parseInt(vv[3]))));
               }
                world.setBlockState(pos.add(Integer.parseInt(vv[1]),Integer.parseInt(vv[2]),Integer.parseInt(vv[3])), Blocks.AIR.getDefaultState());
            });

            BlockShipEntity entity = PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE.create(world);
            int offset = 1;
            if(player.getHeldItem(hand).getItem()==PlatosTransporters.LIFT_JACK_ITEM)
            {
                if(player.getHeldItem(hand).hasTag())
                {
                    offset=player.getHeldItem(hand).getTag().getInt("off");
                }
            }
            world.addEntity(entity);
            entity.setModel(list,getDirection(state),offset,type,storage,addons);
            if(type==1)
            {
                entity.setNoGravity(true);
            }
            entity.teleportKeepLoaded(player.getPosX(),player.getPosY(),player.getPosZ());
            player.startRiding(entity, true);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }



    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return this.getDefaultState().with(HORIZONTAL_FACING,ctx.getPlacementHorizontalFacing());
    }

    private static int getDirection(BlockState state)
    {
        if(state.get(BlockStateProperties.HORIZONTAL_FACING)== Direction.EAST)
        {
            return 270;
        }
        if(state.get(BlockStateProperties.HORIZONTAL_FACING)==Direction.SOUTH)
        {
            return 180;
        }
        if(state.get(BlockStateProperties.HORIZONTAL_FACING)==Direction.WEST)
        {
            return 90;
        }
        return 0;
    }

    private static HashMap<String,Integer> addIfCan(HashMap<String,Integer> input, String key, int mod)
    {
        if(input.containsKey(key))
        {
            input.put(key,input.get(key)+mod);
        }
        else
        {
            input.put(key,mod);
        }
        return input;
    }

}
