package com.fuego.Battlechatter.Gui.Elements;

import java.util.List;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;

import net.minecraft.client.gui.ScaledResolution;

public class GuiMultivalueButton extends GuiElement
{
	private int index=0;
	private boolean isPressed = false;
	private String text="";
	private String[] values;
	
	public GuiMultivalueButton(int xPos, int yPos, int width, int height,
			GuiMenuScreen g,String text,String[] strings,int index) {
		super(xPos, yPos, width, height, g);
		this.text = text;
		this.values = strings;
		this.index = index;
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {
		
		if(isPressed)
			g.drawBorderedNTGradientRectWithString(values[index], super.xPos, super.yPos,super.xPos+ super.width, super.height, 2, 0x80FFFFFF,0x99000000,0x99707070, 0x99FFFFFF);
		else
			g.drawBorderedNTGradientRectWithString(values[index], super.xPos, super.yPos,super.xPos+  super.width, super.height, 2, 0x80FFFFFF,0x99707070 , 0x99000000, 0x99FFFFFF);

		g.drawString(g.getFontRenderer() , text, super.xPos-93, super.yPos+4, 0x99FFFFFF);
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {
		
		if(super.contains(mouseX, mouseY))
		{
			if(direction ==0)
			{
				isPressed=true;

			}
			else
			{
				if(isPressed)
				{
					spin();
				}
				isPressed=false;
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
	public void spin()
	{
		if(index==values.length-1)
		{
			index=0;
		}
		else
		{
			index++;
		}
	}
	public int getSelection()
	{
		return index;
	}
}
