package gd.rf.acro.platos;

import gd.rf.acro.platos.entity.BlockShipEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.PlayerEntityRenderer;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, (entityRenderDispatcher, context) -> new BlockShipEntityRenderer(entityRenderDispatcher));

    }
}
