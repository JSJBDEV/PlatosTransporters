package gd.rf.acro.platos.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.HashMap;

public class BlockShipEntityModel extends EntityModel<BlockShipEntity> {
    private HashMap<String, ListNBT> entities;
    private String ship;
    private int direction;
    private float xOffset;
    private float zOffset;
    private int yOffset;

    private float getxOffset()
    {
        switch (this.direction)
        {
            case 90:
                return -2f;
            case 180:
            case 0:
                return -0.5f;
            case 270:
                return 1f;
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
    public void setRotationAngles(BlockShipEntity entity, float v, float v1, float v2, float v3, float v4) {
        if(this.entities==null)
        {
            this.entities=new HashMap<>();
        }
        if(entity.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem()== Items.OAK_PLANKS)
        {
            CompoundNBT tag = entity.getItemStackFromSlot(EquipmentSlotType.CHEST).getTag();
            this.ship=tag.getString("model");
            this.entities.put(this.ship,(ListNBT) tag.get("parts"));
            this.direction=tag.getInt("direction");
            this.xOffset=getxOffset();
            this.zOffset=getzOffset();
            this.yOffset=tag.getInt("offset");
        }
    }

    @Override
    public void render(MatrixStack matrices, IVertexBuilder iVertexBuilder, int light, int overlay, float red, float green, float blue, float alpha) {
        if(this.ship!=null)
        {
            IRenderTypeBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
            matrices.push();
            matrices.scale(-1.0F, -1.0F, 1.0F);
            matrices.rotate(Vector3f.YN.rotationDegrees(this.direction));
            entities.get(this.ship).forEach(line->
            {
                String[] compound = line.getString().split(" ");
                //Block block = Registry.BLOCK.get(new Identifier(compound[0]));
                matrices.push();
                matrices.translate(Double.parseDouble(compound[1])+this.xOffset,Double.parseDouble(compound[2])+this.yOffset-1.5,Double.parseDouble(compound[3])+this.zOffset);
                Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(Block.getStateById(Integer.parseInt(compound[0])),matrices,buffer,light,overlay);
                matrices.pop();
            });
            matrices.pop();
        }
    }
}