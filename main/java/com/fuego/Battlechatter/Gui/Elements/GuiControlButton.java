package com.fuego.Battlechatter.Gui.Elements;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;

import net.minecraft.client.gui.ScaledResolution;

public class GuiControlButton extends GuiElement
{
	private boolean isSelected = false;
	private String text="";
	private char keyChar=' ';
	
	private long lastUpdated = System.currentTimeMillis();
	private final long drawTime= 500;
	private boolean isDrawn=false;
	public GuiControlButton(int xPos, int yPos, int width, int height,
			GuiMenuScreen g,String text,char keyChar) {
		super(xPos, yPos, width, height, g);
		this.text = text;
		this.keyChar=keyChar;
	}

	@Override
	public void draw(ScaledResolution screen) {
		g.drawBorderedRect(super.xPos, super.yPos,super.xPos+super.width, super.height, 2, 0x80FFFFFF,0x99202020);
		g.drawString(g.getFontRenderer(), String.valueOf(keyChar)+drawMarker(), super.xPos+12, super.yPos+4, 0x99FFFFFF);
		
		g.drawString(g.getFontRenderer() , text, super.xPos-128, super.yPos+4, 0x99FFFFFF);
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {
		
		if(super.contains(mouseX, mouseY))//if within
		{
			if(direction ==0)//if down
			{
				isSelected=true;
			}
		}
		else
		{
			isSelected=false;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char par1, int par2) {
		
		if(isSelected)
		{
			keyChar = par1;
		}
		return false;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	private String drawMarker()
	{
		if(!isSelected)
		{
			return "";
		}
		if(lastUpdated+drawTime<System.currentTimeMillis())
		{//Change the state
			isDrawn = !isDrawn;
			lastUpdated = System.currentTimeMillis();
		}
		//draw the current state
		if(isDrawn)
		{
			return "|";
		}
		else
		{
			return "";
		}
	}
	public String getKeyString()
	{
		return String.valueOf(this.keyChar).toUpperCase();
	}
	public char getKeyChar()
	{
		return this.keyChar;
	}
}
