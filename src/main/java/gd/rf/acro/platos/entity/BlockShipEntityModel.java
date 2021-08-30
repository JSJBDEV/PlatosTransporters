package gd.rf.acro.platos.entity;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3f;

import java.util.HashMap;

public class BlockShipEntityModel extends EntityModel<BlockShipEntity> {
    private HashMap<String, NbtList> entities;
    private String ship;
    private int direction;
    private float xOffset;
    private float zOffset;
    private int yOffset;

    @Override
    public void setAngles(BlockShipEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if(this.entities==null)
        {
            this.entities=new HashMap<>();
        }
        if(entity.getEquippedStack(EquipmentSlot.CHEST).getItem()== Items.OAK_PLANKS)
        {
            NbtCompound tag = entity.getEquippedStack(EquipmentSlot.CHEST).getNbt();
            this.ship=tag.getString("model");
            this.entities.put(this.ship,(NbtList)tag.get("parts"));
            this.direction=tag.getInt("direction");
            this.xOffset=getxOffset();
            this.zOffset=getzOffset();
            this.yOffset=tag.getInt("offset");
        }
    }

    private float getxOffset()
    {
        switch (this.direction)
        {
            case 90:
                return 1f;
            case 180:
            case 0:
                return -0.5f;
            case 270:
                return -2f;
            default:
                return 0f;
        }
    }
    private float getzOffset()
    {
        switch (this.direction)
        {
            case 90:
            case 270:
                return -0.5f;
            case 180:
                return 1f;
            case 0:
                return -2f;
            default:
                return 0f;
        }
    }



    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        if(this.ship!=null)
        {
            VertexConsumerProvider vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            matrices.push();
            matrices.scale(-1.0F, -1.0F, 1.0F);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(this.direction));
            entities.get(this.ship).forEach(line->
            {
                String[] compound = line.asString().split(" ");
                //Block block = Registry.BLOCK.get(new Identifier(compound[0]));
                matrices.push();
                matrices.translate(Double.parseDouble(compound[1])+this.xOffset,Double.parseDouble(compound[2])+this.yOffset-1.5,Double.parseDouble(compound[3])+this.zOffset);
                MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(Block.getStateFromRawId(Integer.parseInt(compound[0])),matrices, vertexConsumerProvider,light,overlay);
                matrices.pop();
            });
            matrices.pop();
        }
    }
}