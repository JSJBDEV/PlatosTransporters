package gd.rf.acro.platos.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BlockShipEntityRenderer extends MobRenderer<BlockShipEntity,BlockShipEntityModel> {


    public BlockShipEntityRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new BlockShipEntityModel(), 1);
    }

    @Override
    public ResourceLocation getEntityTexture(BlockShipEntity entity) {
        return new ResourceLocation("minecraft","textures/block/acacia_planks.png");
    }
}
