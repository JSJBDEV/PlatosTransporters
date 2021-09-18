package gd.rf.acro.platos;

import gd.rf.acro.platos.blocks.BlockControlWheel;
import gd.rf.acro.platos.blocks.NotFullBlock;
import gd.rf.acro.platos.entity.BlockShipEntity;
import gd.rf.acro.platos.entity.BlockShipEntityRenderer;
import gd.rf.acro.platos.items.*;
import gd.rf.acro.platos.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import static net.minecraft.client.resources.I18n.format;

@Mod(value = PlatosTransporters.MODID)
public class PlatosTransporters{
	public static final String MODID = "platos";
	public static final ItemGroup TAB = ItemGroup.MISC;
	public static KeyBinding SHIP_UP;
	public static KeyBinding SHIP_DOWN;
	public static KeyBinding SHIP_STOP;
	
	public static final EntityType<BlockShipEntity> BLOCK_SHIP_ENTITY_ENTITY_TYPE = createEntity("block_ship",BlockShipEntity::new,1,1);

	public static final Tag<Block> BOAT_MATERIAL =  new BlockTags.Wrapper(new ResourceLocation("platos", "boat_material"));
	public static final Tag<Block> BOAT_MATERIAL_BLACKLIST =  new BlockTags.Wrapper(new ResourceLocation("platos", "boat_material_blacklist"));
	public static final Tag<Block> SCYTHEABLE = new BlockTags.Wrapper(new ResourceLocation("platos", "sctheable"));

	private static <T extends AnimalEntity> EntityType<T> createEntity(String name, EntityType.IFactory<T> factory, float width, float height) {
		ResourceLocation location = new ResourceLocation("platos", name);
		EntityType<T> entity = EntityType.Builder.create(factory, EntityClassification.CREATURE).size(width, height).setTrackingRange(64).setUpdateInterval(1).build(location.toString());
		entity.setRegistryName(location);
		return entity;
	}

	public PlatosTransporters() {
		final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		registerBlocks();
		registerItems();
		NetworkHandler.registerMessages();
		ConfigUtils.checkConfigs();
		eventBus.addListener(this::setupClient);
	}
	private static KeyBinding registerKeybinding(KeyBinding key) {
		ClientRegistry.registerKeyBinding(key);
		return key;
	}

	public void setupClient(final FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, BlockShipEntityRenderer::new);

		SHIP_UP = registerKeybinding(new KeyBinding("key.platos.up", GLFW.GLFW_KEY_Z, "category.platos.main"));
		SHIP_DOWN = registerKeybinding(new KeyBinding("key.platos.down", GLFW.GLFW_KEY_C, "category.platos.main"));
		SHIP_STOP = registerKeybinding(new KeyBinding("key.platos.stop", GLFW.GLFW_KEY_V, "category.platos.main"));
	}

	public static final BlockControlWheel BLOCK_CONTROL_WHEEL = new BlockControlWheel(Block.Properties.create(Material.WOOD));
	public static final NotFullBlock BALLOON_BLOCK = new NotFullBlock(Block.Properties.create(Material.WOOL));
	public static final NotFullBlock FLOAT_BLOCK = new NotFullBlock(Block.Properties.create(Material.WOOL));
	public static final NotFullBlock WHEEL_BLOCK = new NotFullBlock(Block.Properties.create(Material.WOOL));
	private void registerBlocks()
	{
		Registry.register(Registry.BLOCK,new ResourceLocation("platos","ship_controller"),BLOCK_CONTROL_WHEEL);
		Registry.register(Registry.BLOCK,new ResourceLocation("platos","balloon_block"),BALLOON_BLOCK);
		Registry.register(Registry.BLOCK,new ResourceLocation("platos","float_block"),FLOAT_BLOCK);
		Registry.register(Registry.BLOCK,new ResourceLocation("platos","wheel_block"),WHEEL_BLOCK);
	}

	public static final ControlKeyItem CONTROL_KEY_ITEM = new ControlKeyItem(new Item.Properties().group(PlatosTransporters.TAB));
	public static final LiftJackItem LIFT_JACK_ITEM = new LiftJackItem(new Item.Properties().group(PlatosTransporters.TAB));
	public static final WrenchItem WRENCH_ITEM = new WrenchItem(new Item.Properties().group(PlatosTransporters.TAB));
	public static final ClearingScytheItem CLEARING_SCYTHE_ITEM = new ClearingScytheItem(new Item.Properties().group(PlatosTransporters.TAB).maxDamage(100));
	public static final BoardingStairsItem BOARDING_STAIRS_ITEM = new BoardingStairsItem(new Item.Properties().group(PlatosTransporters.TAB));
	private void registerItems()
	{
		Registry.register(Registry.ITEM, new ResourceLocation("platos", "ship_controller"), new BlockItem(BLOCK_CONTROL_WHEEL, new Item.Properties().group(PlatosTransporters.TAB)));
		Registry.register(Registry.ITEM, new ResourceLocation("platos", "float_block"), new BlockItem(FLOAT_BLOCK, new Item.Properties().group(PlatosTransporters.TAB)));
		Registry.register(Registry.ITEM, new ResourceLocation("platos", "balloon_block"), new BlockItem(BALLOON_BLOCK, new Item.Properties().group(PlatosTransporters.TAB)));
		Registry.register(Registry.ITEM, new ResourceLocation("platos", "wheel_block"), new BlockItem(WHEEL_BLOCK, new Item.Properties().group(PlatosTransporters.TAB)));
		Registry.register(Registry.ITEM,new ResourceLocation("platos","control_key"),CONTROL_KEY_ITEM);
		Registry.register(Registry.ITEM,new ResourceLocation("platos","lift_jack"),LIFT_JACK_ITEM);
		Registry.register(Registry.ITEM,new ResourceLocation("platos","wrench"),WRENCH_ITEM);
		Registry.register(Registry.ITEM,new ResourceLocation("platos","clearing_scythe"),CLEARING_SCYTHE_ITEM);
		Registry.register(Registry.ITEM,new ResourceLocation("platos","boarding_stairs"),BOARDING_STAIRS_ITEM);
	}

	public static void givePlayerStartBook(PlayerEntity playerEntity)
	{
		if(!playerEntity.getTags().contains("platos_new"))
		{
			playerEntity.addItemStackToInventory(createBook("Acro","Plato's Transporters"
					, format("book.platos.page1")
					, format("book.platos.page2")
					, format("book.platos.page3")
					, format("book.platos.page4")
					, format("book.platos.page5")
					, format("book.platos.page6")
					, format("book.platos.page7")
			));
			playerEntity.addTag("platos_new");
		}
	}
	private static ItemStack createBook(String author, String title,Object ...pages)
	{
		ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
		CompoundNBT tags = new CompoundNBT();
		tags.putString("author",author);
		tags.putString("title",title);
		ListNBT contents = new ListNBT();
		for (Object page : pages) {
			contents.add(StringNBT.valueOf("{\"text\":\""+page+"\"}"));
		}
		tags.put("pages",contents);
		book.setTag(tags);
		return book;
	}


}
