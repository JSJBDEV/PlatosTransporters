package gd.rf.acro.platos.entity;

import gd.rf.acro.platos.ConfigUtils;
import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.UUID;

public class BlockShipEntity extends PigEntity {
    public BlockShipEntity(EntityType<? extends BlockShipEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.16D);
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public float getAIMoveSpeed() {
        {
            if(this.getControllingPassenger() instanceof  PlayerEntity)
            {
                if(((PlayerEntity) this.getControllingPassenger()).getHeldItemMainhand().getItem()== PlatosTransporters.CONTROL_KEY_ITEM)
                {
                    float cspeed = Float.parseFloat(ConfigUtils.config.getOrDefault("cspeed","0.2"));
                    float nspeed = Float.parseFloat(ConfigUtils.config.getOrDefault("nspeed","0.05"));
                    if(this.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem()==Items.OAK_PLANKS)
                    {
                        if(this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().getInt("type")==0)
                        {
                            if(this.world.getBlockState(this.getPositionUnderneath()).getBlock()== Blocks.WATER)
                            {
                                ListNBT go = (ListNBT) this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().get("addons");
                                if(go.contains(StringNBT.valueOf("engine")))
                                {
                                    return cspeed*1.5f;
                                }
                                return cspeed;
                            }
                            return nspeed;
                        }
                        if(this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().getInt("type")==1)
                        {
                            return 0f;
                        }
                        else
                        {
                            ListNBT go = (ListNBT) this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().get("addons");
                            if(go.contains(StringNBT.valueOf("engine")))
                            {
                                return cspeed*1.5f;
                            }
                            return cspeed;
                        }
                    }
                }
            }
            return 0;
        }
    }





    @Override
    public boolean canBeRiddenInWater() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }



    @Override
    protected int calculateFallDamage(float p_225508_1_, float p_225508_2_) {
        return 0;
    }

    public void setModel(ListNBT input, int direction, int offset, int type, CompoundNBT storage, ListNBT addons)
    {
        ItemStack itemStack = new ItemStack(Items.OAK_PLANKS);
        CompoundNBT tag = new CompoundNBT();
        tag.putString("model", UUID.randomUUID().toString());
        tag.put("parts",input);
        tag.putInt("direction",direction);
        tag.putInt("offset",offset);
        tag.putInt("type",type);
        tag.put("storage",storage);
        tag.put("addons",addons);
        itemStack.setTag(tag);
        this.setItemStackToSlot(EquipmentSlotType.CHEST,itemStack);
    }

    public void tryDisassemble()
    {
        if(this.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem()==Items.OAK_PLANKS)
        {
            ListNBT list = (ListNBT) this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().get("parts");
            int offset = this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().getInt("offset");
            CompoundNBT storage = this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().getCompound("storage");
            for (INBT tag : list)
            {
                String[] split = tag.getString().split(" ");
                BlockState state =world.getBlockState(this.getPositionUnderneath().up().add(Integer.parseInt(split[1]), Integer.parseInt(split[2]) + offset, Integer.parseInt(split[3])));
                if (!state.isAir() && state.getBlock()!=(Blocks.WATER))
                {
                    if (this.getControllingPassenger() instanceof PlayerEntity) {
                        this.getControllingPassenger().sendMessage(new StringTextComponent("cannot disassemble, not enough space"));

                    }
                    return;
                }
            }
            list.forEach(block->
            {
                String[] split = block.getString().split(" ");
                world.setBlockState(this.getPositionUnderneath().add(Integer.parseInt(split[1]),Integer.parseInt(split[2])+offset+1,Integer.parseInt(split[3])), Block.getStateById(Integer.parseInt(split[0])));
            });
            storage.keySet().forEach(blockEntity->
            {
                String[] split = blockEntity.split(" ");
                BlockPos newpos = this.getPositionUnderneath().add(Integer.parseInt(split[0]),Integer.parseInt(split[1])+offset+1,Integer.parseInt(split[2]));
                TileEntity entity = world.getTileEntity(newpos);
                CompoundNBT data = storage.getCompound(blockEntity);
                if(data!=null)
                {
                    data.putInt("x", newpos.getX());
                    data.putInt("y", newpos.getY());
                    data.putInt("z", newpos.getZ());
                    entity.handleUpdateTag(data);
                    entity.markDirty();
                }
            });
            this.removePassengers();
            this.teleportKeepLoaded(0,-1000,0);
        }
    }

    @Override
    public boolean preventDespawn() {
        return true;
    }

    @Override
    protected boolean canBeRidden(Entity p_184228_1_) {
        return true;
    }

    @Override
    public boolean canPassengerSteer() {
        return true;
    }





    @Override
    public boolean processInteract(PlayerEntity playerEntity, Hand p_184645_2_) {
        if(!playerEntity.world.isRemote)
        {
            playerEntity.startRiding(this);
        }
        return true;
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d p_184199_2_, Hand hand) {
        if(!player.getEntityWorld().isRemote && player.getHeldItem(hand)==ItemStack.EMPTY && hand==Hand.MAIN_HAND)
        {
            return ActionResultType.SUCCESS;
        }
        if(!player.getEntityWorld().isRemote && player.getHeldItem(hand).getItem()==PlatosTransporters.LIFT_JACK_ITEM && hand==Hand.MAIN_HAND)
        {
            this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().putInt("offset",player.getHeldItem(hand).getTag().getInt("off"));
            return ActionResultType.SUCCESS;
        }
        return super.applyPlayerInteraction(player, p_184199_2_, hand);
    }



    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        int extra = 0;
        if(this.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem()==Items.OAK_PLANKS)
        {
            extra = this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().getInt("offset")-1;
        }
        if(this.getControllingPassenger() instanceof PlayerEntity)
        {
            if(passenger==this.getControllingPassenger())
            {
                passenger.setPosition(this.getPosX(),this.getPosY()+0.5+extra,this.getPosZ());
            }
        }
    }

    @Override
    public boolean shouldRiderSit() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource p_180431_1_) {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.inWater && this.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().getInt("type")==0)
        {
            if(this.getPassengers().size()>0
                    && ((LivingEntity)this.getControllingPassenger()).getHeldItemMainhand().getItem() ==PlatosTransporters.CONTROL_KEY_ITEM)
            {
                Vec3d vec3d3 = this.getLookVec();
                if(vec3d3.y<0)
                {
                    this.setMotion(vec3d3.x, 0, vec3d3.z);
                }
            }
            else
            {
                this.setMotion(0, 0, 0);
            }
        }
    }

    @Override
    public void onStruckByLightning(LightningBoltEntity p_70077_1_) {

    }
}
