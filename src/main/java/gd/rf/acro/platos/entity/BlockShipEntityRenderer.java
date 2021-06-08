package gd.rf.acro.platos.entity;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class BlockShipEntityRenderer extends MobEntityRenderer<BlockShipEntity,BlockShipEntityModel> {


    public BlockShipEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BlockShipEntityModel(),1);
    }

    @Override
    public Identifier getTexture(BlockShipEntity entity) {
        return new Identifier("minecraft","textures/block/acacia_planks.png");
    }

    @Override
    public boolean shouldRender(BlockShipEntity mobEntity, Frustum frustum, double d, double e, double f) {
        if(mobEntity.getPos().squaredDistanceTo(new Vec3d(d,e,f))<100*100)
        {
            return true;
        }
        return super.shouldRender(mobEntity, frustum, d, e, f);
    }
}
