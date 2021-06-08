package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ClearingScytheItem extends Item {
    public ClearingScytheItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        for (int i = user.getBlockPos().getX()-10; i < user.getBlockPos().getX()+10; i++) {
            for (int j = user.getBlockPos().getY()-3; j < user.getBlockPos().getY()+3; j++) {
                for (int k = user.getBlockPos().getZ()-10; k < user.getBlockPos().getZ()+10; k++) {
                    if(PlatosTransporters.SCYTHEABLE.contains(world.getBlockState(new BlockPos(i,j,k)).getBlock()))
                    {
                        world.breakBlock(new BlockPos(i,j,k),true,user);
                        user.getStackInHand(hand).damage(1,user,(dobreak)-> dobreak.sendToolBreakStatus(hand));
                    }
                }
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("scythe.platos.tooltip"));
    }
}
