package zornco.bedcraftbeyond.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelColoredBed - Zorn_Taov
 * Created using Tabula 5.1.0
 */
public class ModelColoredBed extends ModelBase {
    public ModelRenderer plank1;
    public ModelRenderer bedHead0;
    public ModelRenderer bedFoot0;
    /*public ModelRenderer bedHead1;
    public ModelRenderer bedHead2;
    public ModelRenderer bedFoot1;
    public ModelRenderer bedFoot2;*/

    public ModelColoredBed() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.plank1 = new ModelRenderer(this, -16, 0);
        this.plank1.setTextureSize(16, 16);
        this.plank1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.plank1.addBox(-8.0F, -3.0F, -8.0F, 16, 0, 16, 0.0F);
        this.bedHead0 = new ModelRenderer(this, 0, 0);
        this.bedHead0.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bedHead0.addBox(-8.0F, -9.0F, 0.0F, 16, 9, 8, 0.0F);
        this.bedFoot0 = new ModelRenderer(this, 0, 31);
        this.bedFoot0.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bedFoot0.addBox(-8.0F, -9.0F, -8.0F, 16, 9, 24, 0.0F);
        /*this.bedHead1 = new ModelRenderer(this, 0, 0);
        this.bedHead1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bedHead1.addBox(-8.0F, -9.0F, -8.0F, 16, 9, 16, 0.0F);
        this.bedHead2 = new ModelRenderer(this, 0, 0);
        this.bedHead2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bedHead2.addBox(-8.0F, -9.0F, -8.0F, 16, 9, 16, 0.0F);*/
        /*this.bedFoot1 = new ModelRenderer(this, 0, 32);
        this.bedFoot1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bedFoot1.addBox(-8.0F, -9.0F, -8.0F, 16, 9, 16, 0.0F);
        this.bedFoot2 = new ModelRenderer(this, 0, 32);
        this.bedFoot2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bedFoot2.addBox(-8.0F, -9.0F, -8.0F, 16, 9, 16, 0.0F);*/
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        //this.plank1.render(f5);
        if(f == 0)
		{
        	this.bedFoot0.render(f5);
        	/*switch ((int)f1) {
			default:
	        	this.bedFoot0.render(f5);
				break;
			case 1:
	            this.bedFoot1.render(f5);
				break;
			case 2:
	            this.bedFoot2.render(f5);
				break;

			}*/
		}
		else
		{
			this.bedHead0.render(f5);
        	/*switch ((int)f1) {
			default:
	            this.bedHead0.render(f5);
				break;
			case 1:
	        	this.bedHead1.render(f5);
				break;
			case 2:
	            this.bedHead2.render(f5);
				break;
	
			}*/
		}
    }
    
    public void renderPlank(float f5) {
    	this.plank1.rotateAngleY = 0;//(float) (Math.PI/2.0);
        this.plank1.render(f5);
    }
    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
