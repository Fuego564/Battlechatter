package com.fuego.Battlechatter.Gui.Elements;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public abstract class GuiElement 
{
	protected int xPos;
	protected int yPos;
	protected int width;
	protected int height;
	protected GuiMenuScreen g;
	/**
	 * 
	 * @param xPos
	 * @param yPos
	 * @param width
	 * @param height
	 */
	public GuiElement(int xPos, int yPos,int width, int height,GuiMenuScreen g)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = yPos+height;
		this.g = g;
	}
	/**
	 *Primary drawing method. Override to use.
	 */
	public abstract void draw(ScaledResolution screen);

	/**
	 * Called when the mouse is clicked or released 
	 * @mousebutton:LMB =0 RMB = 1
	 * @direction DOWN = 0 UP =1
	 */
	public abstract boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,int direction);
	
	/**
	 * 
	 * @param par1- the letter pressed
	 * @param par2- the index of the key pressed
	 * @return
	 */
	public abstract boolean keyTyped(char par1, int par2);
	
	public abstract void init();
	/**
	 * Used to determine whether or not the mouse position is within the bounds of the element
	 */
	public boolean contains(int mouseX,int mouseY)
	{
		return mouseX > xPos &&  mouseX< xPos+width && mouseY >yPos && mouseY<height;
	}
	public void close()
	{
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
}
