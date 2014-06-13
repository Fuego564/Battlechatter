package com.fuego.Battlechatter.Gui;

import org.lwjgl.opengl.GL11;




import com.fuego.Battlechatter.Gui.IRC.IrcChat;
import com.fuego.Battlechatter.Gui.NotificationManager.NotificationManager;
import com.fuego.Battlechatter.Gui.Radar.GuiRadar;
import com.fuego.Battlechatter.Input.InputHandler;
import com.fuego.Battlechatter.Util.Settings;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

/**
 * The ingame overlay
 */
public class GuiOverlay extends Gui
{

	private Minecraft mc;
	private InputHandler inputHandler;
	//	private Settings settings;

	private GuiRadar radar;
	private FontRenderer f;
	private ScaledResolution screen;
	private boolean radarEnabled = false;
	private NotificationManager noteManager;
	private int lastX = 0;
	private int lastZ = 0;
	private int lastHealth = 0;
	public GuiOverlay(Minecraft mc)
	{
		super();
		this.mc = mc;
		this.inputHandler = new InputHandler(this);

		radar = new GuiRadar(mc,this);
		noteManager = new NotificationManager(mc,this);

		FMLCommonHandler.instance().bus().register(inputHandler);

		screen = new ScaledResolution(
				mc.gameSettings,
				mc.displayWidth,
				mc.displayHeight);
		f = mc.fontRenderer;

	}

	private void drawOverlay(RenderTickEvent event)
	{
		screen = new ScaledResolution(mc.gameSettings,mc.displayWidth,mc.displayHeight);


		if(isRadarEnabled())
			radar.draw(screen,f);

		noteManager.draw(screen,f);
		noteManager.update();
		
		if(Settings.tempHealthAwareness && IrcChat.ircBot!=null && IrcChat.ircMain.connected())//if ready for battleawareness
			handleAwareness();
	}

	@SubscribeEvent
	public void renderTick(RenderTickEvent event)
	{
		if(mc.inGameHasFocus)
		{
			drawOverlay(event);
		}
		else
		{
			noteManager.draw(screen,f);
			noteManager.update();
		}

	}
	/**
	 * When connected to an IRC with BattleAwareness enabled this will share your
	 * x position, z position and health all rounded for easier transfer
	 */
	private void handleAwareness()
	{
		int newX = getRoundedLocation(mc.thePlayer.posX);
		int newZ = getRoundedLocation(mc.thePlayer.posZ);
		int newHealth= (int)getRoundedHealth(mc.thePlayer.getHealth());
		
		String toAwareness="BA%";
		
		if(lastX != newX|| lastZ !=newZ|| lastHealth != newHealth)//if there is change
		{
			updateSelfAware(newX,newZ,newHealth);
			
			toAwareness += newX+"%"+newZ+"%"+newHealth;
			
			IrcChat.ircBot.sendMessage(IrcChat.ircBot.getChannels()[0], toAwareness);
		}
		
	}
	private void updateSelfAware(int x, int z, int h)
	{
		this.lastX = x;
		this.lastZ = z;
		this.lastHealth = h;
	}
	
	public void drawNotes()
	{
		noteManager.draw(screen,f);
	}
	public float getZLevel()
	{
		return super.zLevel;
	}
	public void toggleRadar()
	{
		radarEnabled = !radarEnabled;
	}
	public boolean isRadarEnabled()
	{
		return radarEnabled;
	}
	public void drawSideGradientRect(int i, int j, int k, int l, int i1, int j1)
	{
		float f = (float)(i1 >> 24 & 0xff) / 255F;
		float f1 = (float)(i1 >> 16 & 0xff) / 255F;
		float f2 = (float)(i1 >> 8 & 0xff) / 255F;
		float f3 = (float)(i1 & 0xff) / 255F;
		float f4 = (float)(j1 >> 24 & 0xff) / 255F;
		float f5 = (float)(j1 >> 16 & 0xff) / 255F;
		float f6 = (float)(j1 >> 8 & 0xff) / 255F;
		float f7 = (float)(j1 & 0xff) / 255F;
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(7425 /*GL_SMOOTH*/);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex(k, j, getZLevel());
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex(i, j,getZLevel());
		tessellator.addVertex(i, l, getZLevel());
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex(k, l, getZLevel());
		tessellator.draw();
		GL11.glShadeModel(7424 /*GL_FLAT*/);
		GL11.glDisable(3042 /*GL_BLEND*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}
	public void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		float f = (float)(par5 >> 24 & 255) / 255.0F;
		float f1 = (float)(par5 >> 16 & 255) / 255.0F;
		float f2 = (float)(par5 >> 8 & 255) / 255.0F;
		float f3 = (float)(par5 & 255) / 255.0F;
		float f4 = (float)(par6 >> 24 & 255) / 255.0F;
		float f5 = (float)(par6 >> 16 & 255) / 255.0F;
		float f6 = (float)(par6 >> 8 & 255) / 255.0F;
		float f7 = (float)(par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex((double)par3, (double)par2, (double)this.zLevel);
		tessellator.addVertex((double)par1, (double)par2, (double)this.zLevel);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex((double)par1, (double)par4, (double)this.zLevel);
		tessellator.addVertex((double)par3, (double)par4, (double)this.zLevel);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	public void drawBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
		drawVerticalLine(x, y, y1 -1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x, x1 - 1, y, borderC);
		drawHorizontalLine(x, x1 - 1, y1 -1, borderC);
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
	}
	public InputHandler getInputHandler()
	{
		return this.inputHandler;
	}
	public void setRadar(boolean state)
	{
		radarEnabled = state;
	}
	
	public int getRoundedLocation(double i)
	{
		return (((int)i)/50)*50;
	}
	public float getRoundedHealth(float g)
	{
		return (g+3)/4;
	}
}
