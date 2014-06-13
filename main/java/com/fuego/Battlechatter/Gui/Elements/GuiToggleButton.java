package com.fuego.Battlechatter.Gui.Elements;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;

import net.minecraft.client.gui.ScaledResolution;

public class GuiToggleButton extends GuiElement
{
	private boolean optionState = false;
	private String text="";
	private boolean isPressed = false;
	public GuiToggleButton(int xPos, int yPos, int width, int height, GuiMenuScreen g,String text,boolean state) {
		super(xPos, yPos, width, height, g);
		this.text = text;
		this.optionState = state;
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {

		if(isPressed)
			g.drawBorderedNTGradientRectWithString("    "+String.valueOf(optionState), super.xPos, super.yPos,super.xPos+ super.width, super.height, 2, 0x80FFFFFF,0x99000000,0x99707070, 0x99FFFFFF);
		else
			g.drawBorderedNTGradientRectWithString("    "+String.valueOf(optionState), super.xPos, super.yPos,super.xPos+  super.width, super.height, 2, 0x80FFFFFF,0x99707070 , 0x99000000, 0x99FFFFFF);

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
					optionState = !optionState;
					isPressed=false;
					return true;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init() {
		//Get optionState

	}
	public boolean getState()
	{
		return optionState;
	}
	public void setState(boolean state)
	{
		this.optionState=state;
	}
}
