package gd.rf.acro.platos.entity;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class BlockShipEntityRenderer extends MobEntityRenderer<BlockShipEntity,BlockShipEntityModel> {
    public BlockShipEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new BlockShipEntityModel(),1);
    }

    @Override
    public Identifier getTexture(BlockShipEntity entity) {
        return new Identifier("minecraft","textures/block/acacia_planks.png");
    }
}
