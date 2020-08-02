package gd.rf.acro.platos.blocks;

import gd.rf.acro.platos.ConfigUtils;
import gd.rf.acro.platos.PlatosTransporters;
import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Clearable;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlockControlWheel extends HorizontalFacingBlock {
    public BlockControlWheel(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(1d,0d,1d,15d,8d,15d);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient && hand==Hand.MAIN_HAND)
        {
            int balloons = Integer.parseInt(ConfigUtils.config.get("balloon"));
            int floats = Integer.parseInt(ConfigUtils.config.get("float"));
            int wheels = Integer.parseInt(ConfigUtils.config.get("wheel"));
            String whitelist = ConfigUtils.config.getOrDefault("whitelist","false");
            int type = -1;
            int blocks = 0;
            int balances = 0;
            HashMap<String,Integer> used = new HashMap<>();
            ListTag list = new ListTag();
            CompoundTag storage = new CompoundTag();

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
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        for (int k = -1; k < 2; k++)
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
                int i = integers[0];
                int j = integers[1];
                int k = integers[2];
                {
                    addIfCan(used, world.getBlockState(pos.add(i, j, k)).getBlock().getTranslationKey(), 1);
                    list.add(StringTag.of(
                            Block.getRawIdFromState(world.getBlockState(pos.add(i, j, k))) + " " + i + " " + j + " " + k));
                    blocks++;

                    if (world.getBlockEntity(pos.add(i, j, k)) != null) {
                        CompoundTag data = world.getBlockEntity(pos.add(i, j, k)).toTag(new CompoundTag());
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
                player.sendMessage(new LiteralText("No wheel/float/balloon found"),false);
                return ActionResult.FAIL;
            }
            if(balances<blocks)
            {
                player.sendMessage(new LiteralText("Cannot assemble, not enough floats/balloons/wheels"),false);
                used.keySet().forEach(key->
                {
                    player.sendMessage(new LiteralText(key+": "+used.get(key)),false);
                });
                player.sendMessage(new LiteralText("If you believe any of the above blocks was added in error report it on CurseForge!"),false);
                return ActionResult.FAIL;
            }
            list.forEach(block->
            {
               String[] vv = block.asString().split(" ");
               if(world.getBlockEntity(pos.add(Integer.parseInt(vv[1]),Integer.parseInt(vv[2]),Integer.parseInt(vv[3])))!=null)
               {
                   Clearable.clear(world.getBlockEntity(pos.add(Integer.parseInt(vv[1]),Integer.parseInt(vv[2]),Integer.parseInt(vv[3]))));
               }
                world.setBlockState(pos.add(Integer.parseInt(vv[1]),Integer.parseInt(vv[2]),Integer.parseInt(vv[3])), Blocks.AIR.getDefaultState());
            });

            BlockShipEntity entity = new BlockShipEntity(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, world);
            int offset = 1;
            if(player.getStackInHand(hand).getItem()==PlatosTransporters.LIFT_JACK_ITEM)
            {
                if(player.getStackInHand(hand).hasTag())
                {
                    offset=player.getStackInHand(hand).getTag().getInt("off");
                }
            }
            entity.setBoundingBox(new Box(nposx,nposy,nposz,mposx,nposy,nposz));
            entity.setModel(list,getDirection(state),offset,type,storage);
            entity.teleport(player.getX(), player.getY(), player.getZ());
            world.spawnEntity(entity);
            player.startRiding(entity, true);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING,ctx.getPlayerFacing());
    }

    private static int getDirection(BlockState state)
    {
        if(state.get(Properties.HORIZONTAL_FACING)==Direction.EAST)
        {
            return 90;
        }
        if(state.get(Properties.HORIZONTAL_FACING)==Direction.SOUTH)
        {
            return 180;
        }
        if(state.get(Properties.HORIZONTAL_FACING)==Direction.WEST)
        {
            return 270;
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
