package com.fuego.Battlechatter.Gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.fuego.Battlechatter.Main.Battlechatter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

/**
 *  The screen that hosts all elements and containers
 * @param overlay
 */
public class GuiMenuScreen extends GuiScreen
{
	private Minecraft mc;
	private int tabIndex = 0;
	private GuiMenu contextMenu;
	private FontRenderer f;
	private ScaledResolution screen;
	private GuiOverlay overlay;

	public GuiMenuScreen(GuiOverlay overlay)//Needs args and stuff
	{
		this.overlay =overlay;
		mc= Minecraft.getMinecraft();
		f = mc.fontRenderer;
		//tempSettings = Battlechatter.getSettings();
		screen = new ScaledResolution(mc.gameSettings,mc.displayWidth,mc.displayHeight);
		contextMenu = new GuiMenu(mc,screen,this);
	}

	public GuiMenuScreen (int index)
	{
		mc= Minecraft.getMinecraft();
		f = mc.fontRenderer;
		//tempSettings = Battlechatter.getSettings();
		contextMenu = new GuiMenu(mc,screen,this);
		tabIndex = index;
	}


	/**
	 *	par3 LMB=0 RMB =1
	 *	par4 down=0 up =1
	 */
	public void onMouseEvent(int par1, int par2, int par3,int par4)
	{
		contextMenu.handleMouseInput(par1,par2,par3,par4);
	}
	@Override
	protected void keyTyped(char par1, int par2)
	{
		if(par2 == 1)
			close();
		else 
			contextMenu.keyTyped(par1, par2);
	}
	public void drawScreen(int par1, int par2, float par3)
	{
		screen = new ScaledResolution(mc.gameSettings,mc.displayWidth,mc.displayHeight);
		//overlay.drawNotes();
		contextMenu.draw(screen);
	}
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		onMouseEvent(par1,par2,par3,0);
	}

	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3)
	{
		onMouseEvent(par1, par2, par3, 1);
	}
	public void close()
	{
		mc.displayGuiScreen(null);
	}
	public void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		super.drawGradientRect(par1, par2, par3, par4, par5, par6);
	}
	public void drawBorderedNTGradientRectWithString(String s, int x, int y, int x1, int y1, int size, int borderC, int insideC1, int insideC2, int SC) {
		drawVerticalLine(x, y, y1 -1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x, x1 - 1, y, borderC);
		drawHorizontalLine(x, x1 - 1, y1 -1, borderC);
		drawNTGradientRect(x + size, y + size, x1 - size, y1 - size, insideC1, insideC2);


		int S2 = x;
		int S3 = ((y1 - y) / 2);
		int S4 = (y1 - S3);
		f.drawString(s, S2 + 3, S4 - 4, SC);
	}
	public void drawNTGradientRect(int x, int y, int x1, int y1, int insideC1, int insideC2) {
		for(int Loop = -5; Loop < 40; Loop++){
			drawGradientRect(x, y, x1, y1, insideC1, insideC2);
		}
	}
	public void drawWBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
		drawVerticalLine(x, y, y1 -1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x + 1, x1 - 1, y, borderC);
		drawHorizontalLine(x + 1, x1 - 1, y1 -1, borderC);
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
	}
	public void drawBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
		drawVerticalLine(x, y, y1 -1, borderC);
		drawVerticalLine(x1 - 1, y, y1 - 1, borderC);
		drawHorizontalLine(x, x1 - 1, y, borderC);
		drawHorizontalLine(x, x1 - 1, y1 -1, borderC);
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
	}
	public FontRenderer getFontRenderer()
	{
		return f;
	}
	public GuiOverlay getOverlay()
	{
		return this.overlay;
	}
	public void drawHoveringText(List<String> list, int x, int y)
	{
		super.drawHoveringText(list, x, y, f);
	}
}
