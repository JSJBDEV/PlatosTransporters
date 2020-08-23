package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class LiftJackItem extends Item {
    public LiftJackItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!stack.hasTag())
        {
            CompoundTag tag = new CompoundTag();
            tag.putInt("off",1);
            stack.setTag(tag);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld().getBlockState(context.getBlockPos()).getBlock()!= PlatosTransporters.BLOCK_CONTROL_WHEEL)
        {
            PlayerEntity user = context.getPlayer();
            Hand hand = context.getHand();
            CompoundTag tag = new CompoundTag();
            tag.putInt("off",1);
            if(user.getStackInHand(hand).hasTag())
            {
                tag=user.getStackInHand(hand).getTag();
            }
            if(user.isSneaking() && tag.getInt("off")>1)
            {
                tag.putInt("off",tag.getInt("off")-1);

            }
            else
            {
                tag.putInt("off",tag.getInt("off")+1);
            }
            user.sendMessage(new LiteralText("new height: "+tag.getInt("off")),true);
            user.getStackInHand(hand).setTag(tag);
            PlatosTransporters.givePlayerStartBook(user);
        }
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(stack.hasTag())
        {
            tooltip.add(new TranslatableText("liftjack.platos.tooltip"));
            tooltip.add(new LiteralText(stack.getTag().getInt("off")+" ").append(new TranslatableText("liftjack.platos.tooltip2")));

        }
    }
}
