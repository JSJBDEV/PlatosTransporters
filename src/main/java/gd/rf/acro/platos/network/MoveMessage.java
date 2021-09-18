package gd.rf.acro.platos.network;

import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MoveMessage {
    int state;

    public MoveMessage(PacketBuffer packetBuffer) {
        state=packetBuffer.readInt();
    }
    public MoveMessage(int a)
    {
        state=a;
    }



    public void encode(PacketBuffer buf) {
        buf.writeInt(state);

    }

    public boolean receive(Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isServer()) {
            context.get().enqueueWork(() -> {
                ServerPlayerEntity user =context.get().getSender();
                if(user.getRidingEntity()!=null && user.getRidingEntity() instanceof BlockShipEntity)
                {
                    BlockShipEntity vehicle = (BlockShipEntity) user.getRidingEntity();
                    int s = vehicle.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().getInt("type");
                    int move = state;
                    if(move==0)
                    {

                        Vector3d v = new Vector3d(vehicle.getLookVec().x,vehicle.getLookVec().y,vehicle.getLookVec().z).scale(0.8f);
                        vehicle.setMotion(v);
                    }
                    if(move==2)
                    {
                        vehicle.rotationYaw+=5;
                        vehicle.rotationYawHead+=5;

                    }
                    if(move==1)
                    {
                        vehicle.rotationYaw-=5;
                        vehicle.rotationYawHead-=5;
                    }
                    if(move==3 && s==1)
                    {
                        vehicle.setMotion(0,1,0);
                    }
                    if(move==4 && s==1)
                    {
                        vehicle.setMotion(0,-1,0);

                    }
                    if(move==5)
                    {
                        if(vehicle.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()== Items.STICK)
                        {
                            vehicle.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                        }
                        else
                        {
                            vehicle.setItemStackToSlot(EquipmentSlotType.HEAD,new ItemStack(Items.STICK));
                        }

                    }
                }
            });

            return true;
        }
        return false;
    }
}
