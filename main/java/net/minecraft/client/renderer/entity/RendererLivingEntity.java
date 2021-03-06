package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.fuego.Battlechatter.Util.FriendsList;
import com.fuego.Battlechatter.Util.Settings;
import com.fuego.Battlechatter.Util.BattleAwareness.BattleAwareness;

@SideOnly(Side.CLIENT)
public abstract class RendererLivingEntity extends Render
{
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	protected ModelBase mainModel;
	/** The model to be used during the render passes. */
	protected ModelBase renderPassModel;
	private static final String __OBFID = "CL_00001012";

	public static float NAME_TAG_RANGE = 64.0f;
	public static float NAME_TAG_RANGE_SNEAK = 32.0f;

	public RendererLivingEntity(ModelBase par1ModelBase, float par2)
	{
		this.mainModel = par1ModelBase;
		this.shadowSize = par2;
	}

	/**
	 * Sets the model to be used in the current render pass (the first render pass is done after the primary model is
	 * rendered) Args: model
	 */
	public void setRenderPassModel(ModelBase par1ModelBase)
	{
		this.renderPassModel = par1ModelBase;
	}

	/**
	 * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
	 * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
	 * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
	 */
	private float interpolateRotation(float par1, float par2, float par3)
	{
		float f3;

		for (f3 = par2 - par1; f3 < -180.0F; f3 += 360.0F)
		{
			;
		}

		while (f3 >= 180.0F)
		{
			f3 -= 360.0F;
		}

		return par1 + par3 * f3;
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9)
	{
		if (MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(par1EntityLivingBase, this, par2, par4, par6))) return;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		this.mainModel.onGround = this.renderSwingProgress(par1EntityLivingBase, par9);

		if (this.renderPassModel != null)
		{
			this.renderPassModel.onGround = this.mainModel.onGround;
		}

		this.mainModel.isRiding = par1EntityLivingBase.isRiding();

		if (this.renderPassModel != null)
		{
			this.renderPassModel.isRiding = this.mainModel.isRiding;
		}

		this.mainModel.isChild = par1EntityLivingBase.isChild();

		if (this.renderPassModel != null)
		{
			this.renderPassModel.isChild = this.mainModel.isChild;
		}

		try
		{
			float f2 = this.interpolateRotation(par1EntityLivingBase.prevRenderYawOffset, par1EntityLivingBase.renderYawOffset, par9);
			float f3 = this.interpolateRotation(par1EntityLivingBase.prevRotationYawHead, par1EntityLivingBase.rotationYawHead, par9);
			float f4;

			if (par1EntityLivingBase.isRiding() && par1EntityLivingBase.ridingEntity instanceof EntityLivingBase)
			{
				EntityLivingBase entitylivingbase1 = (EntityLivingBase)par1EntityLivingBase.ridingEntity;
				f2 = this.interpolateRotation(entitylivingbase1.prevRenderYawOffset, entitylivingbase1.renderYawOffset, par9);
				f4 = MathHelper.wrapAngleTo180_float(f3 - f2);

				if (f4 < -85.0F)
				{
					f4 = -85.0F;
				}

				if (f4 >= 85.0F)
				{
					f4 = 85.0F;
				}

				f2 = f3 - f4;

				if (f4 * f4 > 2500.0F)
				{
					f2 += f4 * 0.2F;
				}
			}

			float f13 = par1EntityLivingBase.prevRotationPitch + (par1EntityLivingBase.rotationPitch - par1EntityLivingBase.prevRotationPitch) * par9;
			this.renderLivingAt(par1EntityLivingBase, par2, par4, par6);
			f4 = this.handleRotationFloat(par1EntityLivingBase, par9);
			this.rotateCorpse(par1EntityLivingBase, f4, f2, par9);
			float f5 = 0.0625F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(par1EntityLivingBase, par9);
			GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
			float f6 = par1EntityLivingBase.prevLimbSwingAmount + (par1EntityLivingBase.limbSwingAmount - par1EntityLivingBase.prevLimbSwingAmount) * par9;
			float f7 = par1EntityLivingBase.limbSwing - par1EntityLivingBase.limbSwingAmount * (1.0F - par9);

			if (par1EntityLivingBase.isChild())
			{
				f7 *= 3.0F;
			}

			if (f6 > 1.0F)
			{
				f6 = 1.0F;
			}

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			this.mainModel.setLivingAnimations(par1EntityLivingBase, f7, f6, par9);
			this.renderModel(par1EntityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
			int j;
			float f8;
			float f9;
			float f10;

			for (int i = 0; i < 4; ++i)
			{
				j = this.shouldRenderPass(par1EntityLivingBase, i, par9);

				if (j > 0)
				{
					this.renderPassModel.setLivingAnimations(par1EntityLivingBase, f7, f6, par9);
					this.renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f3 - f2, f13, f5);

					if ((j & 240) == 16)
					{
						this.func_82408_c(par1EntityLivingBase, i, par9);
						this.renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
					}

					if ((j & 15) == 15)
					{
						f8 = (float)par1EntityLivingBase.ticksExisted + par9;
						this.bindTexture(RES_ITEM_GLINT);
						GL11.glEnable(GL11.GL_BLEND);
						f9 = 0.5F;
						GL11.glColor4f(f9, f9, f9, 1.0F);
						GL11.glDepthFunc(GL11.GL_EQUAL);
						GL11.glDepthMask(false);

						for (int k = 0; k < 2; ++k)
						{
							GL11.glDisable(GL11.GL_LIGHTING);
							f10 = 0.76F;
							GL11.glColor4f(0.5F * f10, 0.25F * f10, 0.8F * f10, 1.0F);
							GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
							GL11.glMatrixMode(GL11.GL_TEXTURE);
							GL11.glLoadIdentity();
							float f11 = f8 * (0.001F + (float)k * 0.003F) * 20.0F;
							float f12 = 0.33333334F;
							GL11.glScalef(f12, f12, f12);
							GL11.glRotatef(30.0F - (float)k * 60.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslatef(0.0F, f11, 0.0F);
							GL11.glMatrixMode(GL11.GL_MODELVIEW);
							this.renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
						}

						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glDepthMask(true);
						GL11.glLoadIdentity();
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glDepthFunc(GL11.GL_LEQUAL);
					}

					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			GL11.glDepthMask(true);
			this.renderEquippedItems(par1EntityLivingBase, par9);
			float f14 = par1EntityLivingBase.getBrightness(par9);
			j = this.getColorMultiplier(par1EntityLivingBase, f14, par9);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

			if ((j >> 24 & 255) > 0 || par1EntityLivingBase.hurtTime > 0 || par1EntityLivingBase.deathTime > 0)
			{
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDepthFunc(GL11.GL_EQUAL);

				if (par1EntityLivingBase.hurtTime > 0 || par1EntityLivingBase.deathTime > 0)
				{
					GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(par1EntityLivingBase, f7, f6, f4, f3 - f2, f13, f5);

					for (int l = 0; l < 4; ++l)
					{
						if (this.inheritRenderPass(par1EntityLivingBase, l, par9) >= 0)
						{
							GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
						}
					}
				}

				if ((j >> 24 & 255) > 0)
				{
					f8 = (float)(j >> 16 & 255) / 255.0F;
					f9 = (float)(j >> 8 & 255) / 255.0F;
					float f15 = (float)(j & 255) / 255.0F;
					f10 = (float)(j >> 24 & 255) / 255.0F;
					GL11.glColor4f(f8, f9, f15, f10);
					this.mainModel.render(par1EntityLivingBase, f7, f6, f4, f3 - f2, f13, f5);

					for (int i1 = 0; i1 < 4; ++i1)
					{
						if (this.inheritRenderPass(par1EntityLivingBase, i1, par9) >= 0)
						{
							GL11.glColor4f(f8, f9, f15, f10);
							this.renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f3 - f2, f13, f5);
						}
					}
				}

				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}
		catch (Exception exception)
		{
			logger.error("Couldn\'t render entity", exception);
		}

		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
		this.passSpecialRender(par1EntityLivingBase, par2, par4, par6);
		MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(par1EntityLivingBase, this, par2, par4, par6));
	}

	/**
	 * Renders the model in RenderLiving
	 */
	protected void renderModel(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.bindEntityTexture(par1EntityLivingBase);

		if (!par1EntityLivingBase.isInvisible())
		{
			this.mainModel.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
		}
		else if (!par1EntityLivingBase.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
		{
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
			this.mainModel.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glPopMatrix();
			GL11.glDepthMask(true);
		}
		else
		{
			this.mainModel.setRotationAngles(par2, par3, par4, par5, par6, par7, par1EntityLivingBase);
		}
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	protected void renderLivingAt(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
	{
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
	}

	protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
	{
		GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);

		if (par1EntityLivingBase.deathTime > 0)
		{
			float f3 = ((float)par1EntityLivingBase.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
			f3 = MathHelper.sqrt_float(f3);

			if (f3 > 1.0F)
			{
				f3 = 1.0F;
			}

			GL11.glRotatef(f3 * this.getDeathMaxRotation(par1EntityLivingBase), 0.0F, 0.0F, 1.0F);
		}
		else
		{
			String s = EnumChatFormatting.getTextWithoutFormattingCodes(par1EntityLivingBase.getCommandSenderName());

			if ((s.equals("Dinnerbone") || s.equals("Grumm")) && (!(par1EntityLivingBase instanceof EntityPlayer) || !((EntityPlayer)par1EntityLivingBase).getHideCape()))
			{
				GL11.glTranslatef(0.0F, par1EntityLivingBase.height + 0.1F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}

	protected float renderSwingProgress(EntityLivingBase par1EntityLivingBase, float par2)
	{
		return par1EntityLivingBase.getSwingProgress(par2);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(EntityLivingBase par1EntityLivingBase, float par2)
	{
		return (float)par1EntityLivingBase.ticksExisted + par2;
	}

	protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2) {}

	/**
	 * renders arrows the Entity has been attacked with, attached to it
	 */
	protected void renderArrowsStuckInEntity(EntityLivingBase par1EntityLivingBase, float par2)
	{
		int i = par1EntityLivingBase.getArrowCountInEntity();

		if (i > 0)
		{
			EntityArrow entityarrow = new EntityArrow(par1EntityLivingBase.worldObj, par1EntityLivingBase.posX, par1EntityLivingBase.posY, par1EntityLivingBase.posZ);
			Random random = new Random((long)par1EntityLivingBase.getEntityId());
			RenderHelper.disableStandardItemLighting();

			for (int j = 0; j < i; ++j)
			{
				GL11.glPushMatrix();
				ModelRenderer modelrenderer = this.mainModel.getRandomModelBox(random);
				ModelBox modelbox = (ModelBox)modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
				modelrenderer.postRender(0.0625F);
				float f1 = random.nextFloat();
				float f2 = random.nextFloat();
				float f3 = random.nextFloat();
				float f4 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f1) / 16.0F;
				float f5 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f2) / 16.0F;
				float f6 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f3) / 16.0F;
				GL11.glTranslatef(f4, f5, f6);
				f1 = f1 * 2.0F - 1.0F;
				f2 = f2 * 2.0F - 1.0F;
				f3 = f3 * 2.0F - 1.0F;
				f1 *= -1.0F;
				f2 *= -1.0F;
				f3 *= -1.0F;
				float f7 = MathHelper.sqrt_float(f1 * f1 + f3 * f3);
				entityarrow.prevRotationYaw = entityarrow.rotationYaw = (float)(Math.atan2((double)f1, (double)f3) * 180.0D / Math.PI);
				entityarrow.prevRotationPitch = entityarrow.rotationPitch = (float)(Math.atan2((double)f2, (double)f7) * 180.0D / Math.PI);
				double d0 = 0.0D;
				double d1 = 0.0D;
				double d2 = 0.0D;
				float f8 = 0.0F;
				this.renderManager.renderEntityWithPosYaw(entityarrow, d0, d1, d2, f8, par2);
				GL11.glPopMatrix();
			}

			RenderHelper.enableStandardItemLighting();
		}
	}

	protected int inheritRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
	{
		return this.shouldRenderPass(par1EntityLivingBase, par2, par3);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
	{
		return -1;
	}

	protected void func_82408_c(EntityLivingBase par1EntityLivingBase, int par2, float par3) {}

	protected float getDeathMaxRotation(EntityLivingBase par1EntityLivingBase)
	{
		return 90.0F;
	}

	/**
	 * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
	 */
	protected int getColorMultiplier(EntityLivingBase par1EntityLivingBase, float par2, float par3)
	{
		return 0;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
	 * entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {}

	/**
	 * Passes the specialRender and renders it
	 * 
	 */
	protected void passSpecialRender(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
	{
		if (MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Specials.Pre(par1EntityLivingBase, this, par2, par4, par6))) return;
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

		if (this.func_110813_b(par1EntityLivingBase))
		{
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			double d3 = par1EntityLivingBase.getDistanceSqToEntity(this.renderManager.livingPlayer);
			float f2 = par1EntityLivingBase.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;

			if (d3 < (double)(f2 * f2))
			{
				String s = par1EntityLivingBase.func_145748_c_().getFormattedText();

				Entity entity = (Entity)par1EntityLivingBase;

				if(Settings.useCustomTags && entity instanceof EntityPlayer)
				{
					EntityPlayer p = (EntityPlayer)entity;

					if(FriendsList.contains(p.getDisplayName()))// if on friendsList
					{
						//draw Name tag
						renderCustomTag(par1EntityLivingBase, Settings.tagColor+Settings.tagModifier+s, par2, par4, par6, 64);

						int i = BattleAwareness.indexOf(p.getDisplayName());

						if(i!=-1)
						{
							renderHealthTag(par1EntityLivingBase, getHealthBars(BattleAwareness.getHealth(i)), par2, par4+.3D, par6, 64,BattleAwareness.getHealth(i));

							if( Settings.useClanTags)
							{
								renderCustomTag(par1EntityLivingBase, Settings.clanColor+Settings.clanModifier+Settings.clanTag, par2, par4+.6D, par6, 64);   		
							}
						}
						else if(Settings.useClanTags)
						{
							renderCustomTag(par1EntityLivingBase, Settings.clanColor+Settings.clanModifier+Settings.clanTag, par2, par4+.3D, par6, 64);   		
						}
					}
					else 
					{
						if (par1EntityLivingBase.isSneaking())
						{
							FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
							GL11.glPushMatrix();
							GL11.glTranslatef((float)par2 + 0.0F, (float)par4 + par1EntityLivingBase.height + 0.5F, (float)par6);
							GL11.glNormal3f(0.0F, 1.0F, 0.0F);
							GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
							GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
							GL11.glScalef(-f1, -f1, f1);
							GL11.glDisable(GL11.GL_LIGHTING);
							GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
							GL11.glDepthMask(false);
							GL11.glEnable(GL11.GL_BLEND);
							OpenGlHelper.glBlendFunc(770, 771, 1, 0);
							Tessellator tessellator = Tessellator.instance;
							GL11.glDisable(GL11.GL_TEXTURE_2D);
							tessellator.startDrawingQuads();
							int i = fontrenderer.getStringWidth(s) / 2;
							tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
							tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
							tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
							tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
							tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
							tessellator.draw();
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							GL11.glDepthMask(true);
							fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
							GL11.glEnable(GL11.GL_LIGHTING);
							GL11.glDisable(GL11.GL_BLEND);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glPopMatrix();
						}
						else
						{
							this.func_96449_a(par1EntityLivingBase, par2, par4, par6, s, f1, d3);
						}
					}
				}
				else
				{

					if (par1EntityLivingBase.isSneaking())
					{
						FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
						GL11.glPushMatrix();
						GL11.glTranslatef((float)par2 + 0.0F, (float)par4 + par1EntityLivingBase.height + 0.5F, (float)par6);
						GL11.glNormal3f(0.0F, 1.0F, 0.0F);
						GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
						GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
						GL11.glScalef(-f1, -f1, f1);
						GL11.glDisable(GL11.GL_LIGHTING);
						GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
						GL11.glDepthMask(false);
						GL11.glEnable(GL11.GL_BLEND);
						OpenGlHelper.glBlendFunc(770, 771, 1, 0);
						Tessellator tessellator = Tessellator.instance;
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						tessellator.startDrawingQuads();
						int i = fontrenderer.getStringWidth(s) / 2;
						tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
						tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
						tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
						tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
						tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
						tessellator.draw();
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glDepthMask(true);
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glPopMatrix();
					}
					else
					{
						this.func_96449_a(par1EntityLivingBase, par2, par4, par6, s, f1, d3);
					}
				}
			}
		}
		MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Specials.Post(par1EntityLivingBase, this, par2, par4, par6));
	}

	protected boolean func_110813_b(EntityLivingBase par1EntityLivingBase)
	{
		return Minecraft.isGuiEnabled() && par1EntityLivingBase != this.renderManager.livingPlayer && !par1EntityLivingBase.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && par1EntityLivingBase.riddenByEntity == null;
	}

	//renderLivingLabel
	protected void func_96449_a(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, String par8Str, float par9, double par10)
	{
		if (par1EntityLivingBase.isPlayerSleeping())
		{
			this.func_147906_a(par1EntityLivingBase, par8Str, par2, par4 - 1.5D, par6, 64);
		}
		else
		{
			this.func_147906_a(par1EntityLivingBase, par8Str, par2, par4, par6, 64);
		}
	}

/**
 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
 */
public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
{
	this.doRender((EntityLivingBase)par1Entity, par2, par4, par6, par8, par9);
}
protected void renderCustomTag(Entity entity, String username, double posX, double posY, double posZ, int otherInt)
{
	double d3 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);

	if (d3 <= (double)(otherInt * otherInt))
	{
		FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
		float f = 1.6F;
		float f1 = 0.016666668F * f;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)posX + 0.0F, (float)posY + entity.height + 0.5F, (float)posZ);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f1, -f1, f1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		Tessellator tessellator = Tessellator.instance;
		byte var16 = 0;

		if (username.equals("deadmau5"))
		{
			var16 = -10;
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		int j = fontrenderer.getStringWidth(username) / 2;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
		tessellator.addVertex((double)(-j - 1), (double)(-1 + var16), 0.0D);
		tessellator.addVertex((double)(-j - 1), (double)(8 + var16), 0.0D);
		tessellator.addVertex((double)(j + 1), (double)(8 + var16), 0.0D);
		tessellator.addVertex((double)(j + 1), (double)(-1 + var16), 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fontrenderer.drawString(username, -fontrenderer.getStringWidth(username) / 2, var16, -1);
		//fontrenderer.drawString(username, -fontrenderer.getStringWidth(username) / 2, var16, 553648127);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		fontrenderer.drawString(username, -fontrenderer.getStringWidth(username) / 2, var16, -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
protected void renderHealthTag(Entity entity, String username, double posX, double posY, double posZ, int otherInt, int health)
{
	double d3 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);

	if (d3 <= (double)(otherInt * otherInt))
	{
		FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
		float f = 1.6F;
		float f1 = 0.016666668F * f;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)posX + 0.0F, (float)posY + entity.height + 0.5F, (float)posZ);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f1, -f1, f1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		Tessellator tessellator = Tessellator.instance;
		byte var16 = 0;

		if (username.equals("deadmau5"))
		{
			var16 = -10;
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		int j = fontrenderer.getStringWidth(username) / 2;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
		tessellator.addVertex((double)(-j - 1), (double)(-1 + var16), 0.0D);
		tessellator.addVertex((double)(-j - 1), (double)(8 + var16), 0.0D);
		tessellator.addVertex((double)(j + 1), (double)(8 + var16), 0.0D);
		tessellator.addVertex((double)(j + 1), (double)(-1 + var16), 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fontrenderer.drawString(getHealthColor(health)+username, -fontrenderer.getStringWidth(username) / 4, var16, -1);
		//fontrenderer.drawString(username, -fontrenderer.getStringWidth(username) / 2, var16, 553648127);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		fontrenderer.drawString(getHealthColor(health)+username, -fontrenderer.getStringWidth(username) / 4, var16, -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
private final char[] allowedCharacters = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9','_'};

private boolean containsChar(char c)
{
	for(int i=0;i<allowedCharacters.length;i++)
	{
		if(c==allowedCharacters[i])
			return true;
	}
	return false;
}
private String removeDisallowed(String s)
{
	String result=s;

	List<Character> characters =new ArrayList<Character>();

	for( int i=0;i<s.length();i++)
	{
		characters.add(s.charAt(i));
	}

	for(int i = characters.size()-1;i>=0;i--)
	{
		if(!containsChar(characters.get(i)))
		{
			characters.remove(i);
		}
	}
	result = String.valueOf(characters.toArray());
	return result;
}

private String getHealthBars(int h)
{
	String result = getHealthColor(h);
	
	for(int i =0; i< h;i++)
	{
		result+="\u275A";
	}
	
	return result;
}
private String getHealthColor(int h)
{
	if(h==5)
		return "\2472";
	else if(h==4)
		return "\247a";
	else if(h==3)
		return "\247e";
	else if (h==2)
		return "\2476";
	else if (h==1)
		return "\2474";
	else
		return "";
}
}