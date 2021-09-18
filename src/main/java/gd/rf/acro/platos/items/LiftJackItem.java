package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class LiftJackItem extends Item {


    public LiftJackItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }


    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if(context.getWorld().getBlockState(context.getPos()).getBlock()!= PlatosTransporters.BLOCK_CONTROL_WHEEL)
        {
            PlayerEntity user = context.getPlayer();
            Hand hand = context.getHand();
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("off",1);
            if(user.getHeldItem(hand).hasTag())
            {
                tag=user.getHeldItem(hand).getTag();
            }
            if(user.isSneaking() && tag.getInt("off")>1)
            {
                tag.putInt("off",tag.getInt("off")-1);

            }
            else
            {
                tag.putInt("off",tag.getInt("off")+1);
            }
            if(context.getHand()==Hand.MAIN_HAND)
            {
                user.sendMessage(new StringTextComponent("new height: "+tag.getInt("off")));
            }
            user.getHeldItem(hand).setTag(tag);
            PlatosTransporters.givePlayerStartBook(user);
        }
        return super.onItemUse(context);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        super.addInformation(stack, p_77624_2_, tooltip, p_77624_4_);
        if(stack.hasTag())
        {
            tooltip.add(new TranslationTextComponent("liftjack.platos.tooltip"));
            tooltip.add(new StringTextComponent(stack.getTag().getInt("off")+""));
            tooltip.add(new TranslationTextComponent("liftjack.platos.tooltip2"));
        }
    }
}
