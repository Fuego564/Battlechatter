package com.fuego.Battlechatter.Gui.Elements;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;
import com.fuego.Battlechatter.Util.FriendsList;

public class GuiFriendSpinner extends GuiElement
{
	private int index=0;
	private List<GuiButton> buttons = new ArrayList<GuiButton>();

	public GuiFriendSpinner(int xPos, int yPos, int width, int height,
			GuiMenuScreen g) {
		super(xPos, yPos, width, height, g);
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {
		for(int i =0;i<buttons.size();i++)
		{
			buttons.get(i).draw(screen);
		}
		if(FriendsList.size()>0)
			g.drawString(g.getFontRenderer(), FriendsList.get(index), super.xPos+22, super.yPos+4, 0x99FFFFFF);
		else
			g.drawString(g.getFontRenderer(),"EMPTY" , super.xPos+55, super.yPos+4, 0x99FFFFFF);

		if(index == 0)
		{
			g.drawRect(super.xPos+5, super.yPos, super.xPos+20 , super.yPos+15, 0xAA000000);
		}
		if(index>=FriendsList.size()-1)
		{
			g.drawRect(super.width-47, super.yPos, super.width-47+15 , super.yPos+15,0xAA000000);
		}
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {
		for(int i =0;i<buttons.size();i++)

			if((index != 0 || i !=0 ) && (index!=FriendsList.size()-1||i!=1)&& buttons.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
			{
				if(i==0&&index>0)
				{
					index--;
				}
				else if(i==1&& index<FriendsList.size()-1)
				{
					index++;
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
	public void init() {

		buttons.add(new GuiButton(super.xPos+5, super.yPos, 15 , 15, g," <"));
		buttons.add(new GuiButton(super.width-47, super.yPos, 15 , 15, g," >"));
	}

	public int getIndex()
	{
		return this.index;
	}
	public void setIndex(int i)
	{
		this.index= i;
	}
}
