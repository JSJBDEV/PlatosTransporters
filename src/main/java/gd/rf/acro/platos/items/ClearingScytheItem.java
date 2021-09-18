package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ClearingScytheItem extends Item {


    public ClearingScytheItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

   

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity user = context.getPlayer();
        BlockPos pos = new BlockPos(user.getPosX(),user.getPosY(),user.getPosZ());
        for (int i = pos.getX()-10; i < pos.getX()+10; i++) {
            for (int j = pos.getY()-3; j < pos.getY()+3; j++) {
                for (int k = pos.getZ()-10; k < pos.getZ()+10; k++) {
                    if(PlatosTransporters.SCYTHEABLE.contains(context.getWorld().getBlockState(new BlockPos(i,j,k)).getBlock()))
                    {
                        context.getWorld().destroyBlock(new BlockPos(i,j,k),true,user);
                        user.getHeldItem(context.getHand()).damageItem(1,user,(dobreak)-> dobreak.sendBreakAnimation(context.getHand()));
                    }
                }
            }
        }
        return super.onItemUse(context);
    }


    @Override
    public void addInformation(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        p_77624_3_.add(new TranslationTextComponent("scythe.platos.tooltip"));
    }
}
