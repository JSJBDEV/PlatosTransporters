package gd.rf.acro.platos;

import gd.rf.acro.platos.entity.BlockShipEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ClientInit implements ClientModInitializer {
    public static KeyBinding up;
    public static KeyBinding down;
    public static KeyBinding stop;
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, (entityRenderDispatcher, context) -> new BlockShipEntityRenderer(entityRenderDispatcher));
        up = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.platos.up", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z,"category.platos.main"));
        down= KeyBindingHelper.registerKeyBinding(new KeyBinding("key.platos.down", InputUtil.Type.KEYSYM,GLFW.GLFW_KEY_C,"category.platos.main"));
        stop = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.platos.stop", InputUtil.Type.KEYSYM,GLFW.GLFW_KEY_V,"category.platos.main"));
    }
}
