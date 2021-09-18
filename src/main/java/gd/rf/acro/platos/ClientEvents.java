package gd.rf.acro.platos;

import gd.rf.acro.platos.network.MoveMessage;
import gd.rf.acro.platos.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlatosTransporters.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void onEvent(TickEvent.ClientTickEvent event)
    {



        while (PlatosTransporters.SHIP_UP.isPressed())
        {

            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(3));
        }
        while (PlatosTransporters.SHIP_DOWN.isPressed())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(4));
        }
        while (PlatosTransporters.SHIP_STOP.isPressed())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(5));
        }



        while (Minecraft.getInstance().gameSettings.keyBindForward.isPressed())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(0));
        }
        while (Minecraft.getInstance().gameSettings.keyBindLeft.isPressed())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(1));
        }
        while (Minecraft.getInstance().gameSettings.keyBindRight.isPressed())
        {
            NetworkHandler.INSTANCE.sendToServer(new MoveMessage(2));


        }






    }


}
