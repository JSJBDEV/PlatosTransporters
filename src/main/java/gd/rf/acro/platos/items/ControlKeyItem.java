package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ControlKeyItem extends Item {
    public ControlKeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.getVehicle() instanceof BlockShipEntity)
        {
            if(((BlockShipEntity) user.getVehicle()).getEquippedStack(EquipmentSlot.CHEST).getTag().getInt("type")==1)
            {
                user.getVehicle().setVelocity(user.getRotationVector().x,user.getRotationVector().y,user.getRotationVector().z);
                ((BlockShipEntity) user.getVehicle()).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,9999,2,true,false));
            }
        }
        PlatosTransporters.givePlayerStartBook(user);
        return super.use(world, user, hand);
    }
}
