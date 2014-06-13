package com.fuego.Battlechatter.Gui.Elements;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class GuiButton extends GuiElement
{
	private String text="";
	private boolean isPressed=false;
	//List<String> hover = new ArrayList<String>();
	public GuiButton(int xPos, int yPos, int width, int height, GuiMenuScreen g,String text) {
		super(xPos, yPos, width, height, g);
		this.text = text;	
		//hover.add("THIS IS A TEST");
		//hover.add("button");
	}

	@Override//toggles.add(new GuiToggleButton(super.width-60, super.yPos+60, 50, 15, g,"Clan Tags"));
	public void draw(ScaledResolution screen) {
		if(isPressed)
			g.drawBorderedNTGradientRectWithString(text ,super.xPos, super.yPos,super.xPos+super.width, super.height, 2, 0x80FFFFFF,0x99000000,0x99707070, 0x99FFFFFF);
		else
			g.drawBorderedNTGradientRectWithString(text, super.xPos, super.yPos,super.xPos+super.width, super.height, 2, 0x80FFFFFF,0x99707070 , 0x99000000, 0x99FFFFFF);
		
		/*if(super.contains(Mouse.getX() * screen.getScaledWidth() /Minecraft.getMinecraft().displayWidth, screen.getScaledHeight() - (Mouse.getY() * screen.getScaledHeight()) / Minecraft.getMinecraft().displayHeight))
		{
			g.drawHoveringText(hover,Mouse.getX() * screen.getScaledWidth() /Minecraft.getMinecraft().displayWidth, 
				screen.getScaledHeight() - (Mouse.getY() * screen.getScaledHeight()) / Minecraft.getMinecraft().displayHeight);
		}*/

	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {
		
		System.out.println(Mouse.getY()+":"+mouseY);
		if(super.contains(mouseX, mouseY))//if within
		{
			if(direction ==0)//if down
			{
				isPressed=true;
			}
			else//else up
			{
				if(isPressed)
				{
					isPressed=false;

					return true;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
