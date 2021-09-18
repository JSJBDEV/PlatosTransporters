package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class WrenchItem extends Item {


    public WrenchItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity user, Hand hand) {
        if(user.getRidingEntity() instanceof BlockShipEntity)
        {
            ((BlockShipEntity) user.getRidingEntity()).tryDisassemble();
        }
        else
        {
            PlatosTransporters.givePlayerStartBook(user);
        }
        return super.onItemRightClick(world, user, hand);
    }
}
