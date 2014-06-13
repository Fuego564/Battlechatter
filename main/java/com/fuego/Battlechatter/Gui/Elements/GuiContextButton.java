package com.fuego.Battlechatter.Gui.Elements;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiContextButton extends GuiElement
{
	private String text;
	private boolean isPressed = false;//while held
	private boolean isSelected = false;//while selected
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public GuiContextButton(String text,int x, int y, int width, int height,GuiMenuScreen g)
	{
		super(x, y, width, height,g);
		this.text = text;
	}

	@Override
	public void draw(ScaledResolution screen) 
	{
		if(isPressed)
			g.drawBorderedNTGradientRectWithString("\2477"+text, super.xPos, super.yPos, super.xPos+super.width, super.height, 1, 0x80000000, 0x99000000,0x99707070 , 0x99FFFFFF);
		else if(isSelected)
			g.drawBorderedNTGradientRectWithString("\2477"+text, super.xPos, super.yPos, super.xPos+super.width, super.height, 1, 0x80000000,0x99707070,0x99000000,  0x99FFFFFF);
		else
			g.drawBorderedNTGradientRectWithString(text, super.xPos, super.yPos, super.xPos+super.width, super.height, 1, 0x80000000,0x99707070 , 0x99000000, 0x99FFFFFF);

	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,int direction) {

		if(super.contains(mouseX, mouseY))
		{
			if(direction==0)
			{
				isPressed = true;
			}
			else
			{
				if(isPressed)
				{
					isSelected = true;
					isPressed=false;
					return true;
				}
				else
				{
				isPressed=false;
				}
			}
		}
		else
		{
			isPressed=false;
		}
		return false;
	}
	@Override
	public boolean keyTyped(char par1, int par2) {

		return false;
	}

	@Override
	public void init() {


	}
	public void setText(String s)
	{
		this.text = s;
	}
	public void setSelected(boolean state)
	{
		this.isSelected=state;
	}
	public boolean isSelected()
	{
		return isSelected;
	}
}
