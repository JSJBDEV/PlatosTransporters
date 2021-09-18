package gd.rf.acro.platos;

import gd.rf.acro.platos.entity.BlockShipEntity;
import gd.rf.acro.platos.network.MoveMessage;
import gd.rf.acro.platos.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlatosTransporters.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Events {

    @SubscribeEvent
    public static void registerBoat(RegistryEvent.Register<EntityType<?>> entityTypeRegister)
    {
        entityTypeRegister.getRegistry().register(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE);
        GlobalEntityTypeAttributes.put(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, BlockShipEntity.getAttributes().create());
        EntitySpawnPlacementRegistry.register(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BlockShipEntity::canAnimalSpawn);
        System.out.println("ship attributes registered!");
    }


}
