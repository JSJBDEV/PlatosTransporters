package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ControlKeyItem extends Item {


    public ControlKeyItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }




    @Override
    public ActionResult<ItemStack> onItemRightClick(World p_77659_1_, PlayerEntity user, Hand p_77659_3_) {
        if(user.getRidingEntity() instanceof BlockShipEntity && !p_77659_1_.isRemote)
        {
            CompoundNBT tag =((BlockShipEntity) user.getRidingEntity()).getItemStackFromSlot(EquipmentSlotType.CHEST).getTag();
            user.getRidingEntity().setMotion(user.getLookVec().x, user.getLookVec().y, user.getLookVec().z);
            if(tag.getInt("type")==1)
            {
                if(((ListNBT)tag.get("addons")).contains(StringNBT.valueOf("altitude")))
                {
                    user.getRidingEntity().setNoGravity(false);
                    ((BlockShipEntity) user.getRidingEntity()).addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 9999, 2, true, false));
                }
            }
        }
        PlatosTransporters.givePlayerStartBook(user);
        return super.onItemRightClick(p_77659_1_, user, p_77659_3_);
    }
}
