package com.fuego.Battlechatter.Gui.Contexts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;
import com.fuego.Battlechatter.Gui.Elements.GuiElement;

public class GuiAboutContext extends GuiElement
{
	private List<String> text = new ArrayList<String>();
	
	public GuiAboutContext(int xPos, int yPos, int width, int height,
			GuiMenuScreen g) {
		super(xPos, yPos, width, height, g);
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {
		
	for(int i =0; i< text.size();i++)
	{
		g.drawString(g.getFontRenderer(), text.get(i), super.xPos+5, super.yPos+5+i*12, 0x99FFFFFF);
	}
		
	g.drawString(g.getFontRenderer(), "By: Fuego_Tortuga", super.xPos+5, super.yPos+4+10*12, 0x99FFFFFF);
	
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char par1, int par2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init() {
		text.add("Battlechatter 2.0");
		text.add("MC 1.7.2");
		text.add("==========================");
		text.add("Changelog:");
		text.add("-Now forge only");
		text.add("-Remade Gui");
		text.add("-Optimized performance");

		
	}

}
