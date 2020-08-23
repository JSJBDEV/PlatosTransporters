package gd.rf.acro.platos.items;

import gd.rf.acro.platos.PlatosTransporters;
import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import java.util.List;

public class BoardingStairsItem extends Item {
    public BoardingStairsItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        HitResult result =user.rayTrace(100,0,true);
        for (int i = 0; i < 30; i++) {
            world.addParticle(ParticleTypes.SMOKE,user.getRotationVector().multiply(i).x,user.getRotationVector().multiply(i).y,user.getRotationVector().multiply(i).z,0,0,0);
        }
        List<BlockShipEntity> vv = world.getEntitiesByType(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE,new Box(result.getPos().add(-10,-10,-10),result.getPos().add(10,10,10)), LivingEntity::isAlive);
        if(vv.size()>0)
        {
            user.startRiding(vv.get(0));
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("boardingstairs.platos.tooltip"));
    }
}
