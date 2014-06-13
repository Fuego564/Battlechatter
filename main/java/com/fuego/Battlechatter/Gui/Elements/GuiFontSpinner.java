package com.fuego.Battlechatter.Gui.Elements;

import java.util.ArrayList;
import java.util.List;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;

import net.minecraft.client.gui.ScaledResolution;


public class GuiFontSpinner extends GuiElement
{
	private int index=0;
	private List<String> values = new ArrayList<String>();
	private List<GuiButton> buttons = new ArrayList<GuiButton>();

	private String text ="";
	/**
	 * if TYPE
	 * 	1:font color
	 * 	2:font modifier
	 */
	public GuiFontSpinner(int xPos, int yPos, int width, int height, GuiMenuScreen g,int type,String text,String value) {
		super(xPos, yPos, width, height, g);
		this.text = text;
		init();

		if(type==1)
			initializeColorList();
		else if(type==2)
			initializeModifierList();

		this.index=valueToIndex(value);
	}

	@Override
	public void draw(ScaledResolution screen) {
		g.drawString(g.getFontRenderer() , text, super.xPos+5, super.yPos+4, 0x99FFFFFF);

		g.drawString(g.getFontRenderer() , values.get(index)+"FONT", super.width-48, super.yPos+4, 0x99FFFFFF);

		for(int i =0; i< buttons.size();i++)
		{
			buttons.get(i).draw(screen);
		}
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) 
	{
		for(int i =0; i <buttons.size();i++)
		{
			if(buttons.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
			{
				if(i==0)
				{
					spinLeft();
				}
				else
				{
					spinRight();
				}
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char par1, int par2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init() 
	{
		buttons.add(new GuiButton(super.width-70, super.yPos, 15 , 15, g," <"));
		buttons.add(new GuiButton(super.width-20, super.yPos, 15 , 15, g," >"));
	}
	public void spinLeft()
	{
		if(index==0)
		{
			index = values.size()-1;
		}
		else
		{
			index--;
		}
	}
	public void spinRight()
	{
		if(index == values.size()-1)
		{
			index=0;
		}
		else
		{
			index++;
		}
	}
	public String getSelection()
	{
		return values.get(index);
	}
	private void initializeColorList()
	{
		values.add("\247a");
		values.add("\247b");
		values.add("\247c");
		values.add("\247d");
		values.add("\247e");
		values.add("\247f");
		values.add("\2471");
		values.add("\2472");
		values.add("\2473");
		values.add("\2474");
		values.add("\2475");
		values.add("\2476");
		values.add("\2477");
		values.add("\2478");
		values.add("\2479");
		values.add("\2470");
	}
	private void initializeModifierList()
	{
		values.add("\0");
		values.add("\247l");
		values.add("\247n");
		values.add("\247m");
		values.add("\247o");
		values.add("\247k");
	}
	private int valueToIndex(String value)
	{
		for(int i =0; i <values.size();i++)
		{
			if(values.get(i).equals(value))
			{
				return i;
			}
		}
		return 0;
	}

}
