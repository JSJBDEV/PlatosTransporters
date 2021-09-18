package gd.rf.acro.platos;

import gd.rf.acro.platos.blocks.BlockControlWheel;
import gd.rf.acro.platos.blocks.NotFullBlock;
import gd.rf.acro.platos.entity.BlockShipEntity;
import gd.rf.acro.platos.items.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.gui.screen.options.ControlsListWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.Tag;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

import static gd.rf.acro.platos.ClientInit.up;
import static gd.rf.acro.platos.ClientInit.down;
import static gd.rf.acro.platos.ClientInit.stop;

public class PlatosTransporters implements ModInitializer {
	public static final ItemGroup TAB = FabricItemGroupBuilder.build(
			new Identifier("platos", "tab"),
			() -> new ItemStack(PlatosTransporters.BLOCK_CONTROL_WHEEL));
	
	public static final EntityType<BlockShipEntity> BLOCK_SHIP_ENTITY_ENTITY_TYPE =
			Registry.register(Registry.ENTITY_TYPE,new Identifier("platos","block_ship")
					, FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT,BlockShipEntity::new).dimensions(EntityDimensions.fixed(3,1)).trackRangeBlocks(100).build());

	public static final Tag<Block> BOAT_MATERIAL = TagRegistry.block(new Identifier("platos","boat_material"));
	public static final Tag<Block> BOAT_MATERIAL_BLACKLIST = TagRegistry.block(new Identifier("platos","boat_material_blacklist"));
	public static final Tag<Block> SCYTHEABLE = TagRegistry.block(new Identifier("platos","scytheable"));

	public static Identifier forwardPacket = new Identifier("platos","forwardpacket");


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		FabricDefaultAttributeRegistry.register(BLOCK_SHIP_ENTITY_ENTITY_TYPE, MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D));
		registerBlocks();
		registerItems();
		ConfigUtils.checkConfigs();
		System.out.println("Hello Fabric world!");


		ServerPlayNetworking.registerGlobalReceiver(forwardPacket,(server,handler,packetContext, attachedData,sender) ->
		{
			PlayerEntity user = packetContext.player;

			if(user.hasVehicle() && user.getVehicle() instanceof BlockShipEntity)
			{

				int move = attachedData.readInt();
				BlockShipEntity vehicle = (BlockShipEntity) user.getVehicle();
				if(move==0)
				{

					user.getVehicle().setVelocity(new Vec3d(vehicle.getRotationVector().x,vehicle.getRotationVector().y,vehicle.getRotationVector().z).multiply(0.8));
				}
				if(move==2)
				{
					vehicle.yaw+=5;
				}
				if(move==1)
				{
					vehicle.yaw-=5;

				}
				if(move==3 && ((BlockShipEntity) user.getVehicle()).getEquippedStack(EquipmentSlot.CHEST).getTag().getInt("type")==1)
				{
					vehicle.setVelocity(new Vec3d(0,0.2,0));
				}
				if(move==4 && ((BlockShipEntity) user.getVehicle()).getEquippedStack(EquipmentSlot.CHEST).getTag().getInt("type")==1)
				{
					vehicle.setVelocity(new Vec3d(0,-0.2,0));

				}
				if(move==5)
				{

					if(vehicle.getEquippedStack(EquipmentSlot.HEAD).getItem()==Items.STICK)
					{
						vehicle.equipStack(EquipmentSlot.HEAD,ItemStack.EMPTY);
					}
					else
					{
						vehicle.equipStack(EquipmentSlot.HEAD,new ItemStack(Items.STICK));
					}

				}
			}
		});
	}
	public static final BlockControlWheel BLOCK_CONTROL_WHEEL = new BlockControlWheel(AbstractBlock.Settings.of(Material.WOOD));
	public static final NotFullBlock BALLOON_BLOCK = new NotFullBlock(AbstractBlock.Settings.of(Material.WOOL));
	public static final NotFullBlock FLOAT_BLOCK = new NotFullBlock(AbstractBlock.Settings.of(Material.WOOL));
	public static final NotFullBlock WHEEL_BLOCK = new NotFullBlock(AbstractBlock.Settings.of(Material.WOOL));
	private void registerBlocks()
	{
		Registry.register(Registry.BLOCK,new Identifier("platos","ship_controller"),BLOCK_CONTROL_WHEEL);
		Registry.register(Registry.BLOCK,new Identifier("platos","balloon_block"),BALLOON_BLOCK);
		Registry.register(Registry.BLOCK,new Identifier("platos","float_block"),FLOAT_BLOCK);
		Registry.register(Registry.BLOCK,new Identifier("platos","wheel_block"),WHEEL_BLOCK);
	}

	public static final ControlKeyItem CONTROL_KEY_ITEM = new ControlKeyItem(new Item.Settings().group(PlatosTransporters.TAB));
	public static final LiftJackItem LIFT_JACK_ITEM = new LiftJackItem(new Item.Settings().group(PlatosTransporters.TAB));
	public static final WrenchItem WRENCH_ITEM = new WrenchItem(new Item.Settings().group(PlatosTransporters.TAB));
	public static final ClearingScytheItem CLEARING_SCYTHE_ITEM = new ClearingScytheItem(new Item.Settings().group(PlatosTransporters.TAB).maxDamage(100));
	public static final BoardingStairsItem BOARDING_STAIRS_ITEM = new BoardingStairsItem(new Item.Settings().group(PlatosTransporters.TAB));
	private void registerItems()
	{
		Registry.register(Registry.ITEM, new Identifier("platos", "ship_controller"), new BlockItem(BLOCK_CONTROL_WHEEL, new Item.Settings().group(PlatosTransporters.TAB)));
		Registry.register(Registry.ITEM, new Identifier("platos", "float_block"), new BlockItem(FLOAT_BLOCK, new Item.Settings().group(PlatosTransporters.TAB)));
		Registry.register(Registry.ITEM, new Identifier("platos", "balloon_block"), new BlockItem(BALLOON_BLOCK, new Item.Settings().group(PlatosTransporters.TAB)));
		Registry.register(Registry.ITEM, new Identifier("platos", "wheel_block"), new BlockItem(WHEEL_BLOCK, new Item.Settings().group(PlatosTransporters.TAB)));
		Registry.register(Registry.ITEM,new Identifier("platos","control_key"),CONTROL_KEY_ITEM);
		Registry.register(Registry.ITEM,new Identifier("platos","lift_jack"),LIFT_JACK_ITEM);
		Registry.register(Registry.ITEM,new Identifier("platos","wrench"),WRENCH_ITEM);
		Registry.register(Registry.ITEM,new Identifier("platos","clearing_scythe"),CLEARING_SCYTHE_ITEM);
		Registry.register(Registry.ITEM,new Identifier("platos","boarding_stairs"),BOARDING_STAIRS_ITEM);
	}

	public static void givePlayerStartBook(PlayerEntity playerEntity)
	{
		if(!playerEntity.getScoreboardTags().contains("platos_new") && playerEntity.world.isClient)
		{

			playerEntity.giveItemStack(createBook("Acro","Plato's Transporters"
					,I18n.translate("book.platos.page1")
					,I18n.translate("book.platos.page2")
					,I18n.translate("book.platos.page3")
					,I18n.translate("book.platos.page4")
					,I18n.translate("book.platos.page5")
					,I18n.translate("book.platos.page6")
					,I18n.translate("book.platos.page7")
					));
			playerEntity.addScoreboardTag("platos_new");
		}
	}
	private static ItemStack createBook(String author, String title,Object ...pages)
	{
		ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tags = new CompoundTag();
		tags.putString("author",author);
		tags.putString("title",title);
		ListTag contents = new ListTag();
		for (Object page : pages) {
			contents.add(StringTag.of("{\"text\":\""+page+"\"}"));
		}
		tags.put("pages",contents);
		book.setTag(tags);
		return book;
	}
}
