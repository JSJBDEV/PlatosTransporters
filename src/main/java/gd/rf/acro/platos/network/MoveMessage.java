package gd.rf.acro.platos.network;

import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
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
                    int s = ((BlockShipEntity) user.getRidingEntity()).getItemStackFromSlot(EquipmentSlotType.CHEST).getTag().getInt("type");
                    int move = state;
                    if(move==0)
                    {
                        Vec3d v = new Vec3d(user.getLookVec().x,user.getLookVec().y,user.getLookVec().z).scale(0.8);
                       
                        user.getRidingEntity().setMotion(v);
                    }
                    if(move==2)
                    {
                        Vec3d v = new Vec3d(user.getRidingEntity().getLookVec().x,user.getRidingEntity().getLookVec().y,user.getRidingEntity().getLookVec().z).scale(0.8f).rotateYaw(-90);
                        user.getRidingEntity().setMotion(v);
                    }
                    if(move==1)
                    {
                        Vec3d v = new Vec3d(user.getRidingEntity().getLookVec().x,user.getRidingEntity().getLookVec().y,user.getRidingEntity().getLookVec().z).scale(0.8f).rotateYaw(90);
                        user.getRidingEntity().setMotion(v);
                    }
                    if(move==3 && s==1)
                    {
                        user.getRidingEntity().setMotion(0,1,0);
                    }
                    if(move==4 && s==1)
                    {
                        user.getRidingEntity().setMotion(0,-1,0);

                    }
                    if(move==5)
                    {
                        BlockShipEntity entity = (BlockShipEntity) user.getRidingEntity();
                        if(entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()== Items.STICK)
                        {
                            entity.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                        }
                        else
                        {
                            entity.setItemStackToSlot(EquipmentSlotType.HEAD,new ItemStack(Items.STICK));
                        }

                    }
                }
            });

            return true;
        }
        return false;
    }
}
