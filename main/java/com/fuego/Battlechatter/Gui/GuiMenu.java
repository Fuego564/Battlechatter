package com.fuego.Battlechatter.Gui;

import java.util.ArrayList;
import java.util.List;

import com.fuego.Battlechatter.Gui.Contexts.GuiAboutContext;
import com.fuego.Battlechatter.Gui.Contexts.GuiControlsContext;
import com.fuego.Battlechatter.Gui.Contexts.GuiFriendsContext;
import com.fuego.Battlechatter.Gui.Contexts.GuiNameTagsContext;
import com.fuego.Battlechatter.Gui.Contexts.GuiOtherContext;
import com.fuego.Battlechatter.Gui.Contexts.GuiQuickMenuContext;
import com.fuego.Battlechatter.Gui.Contexts.GuiRadarContext;
import com.fuego.Battlechatter.Gui.Elements.GuiContextButton;
import com.fuego.Battlechatter.Gui.Elements.GuiElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiMenu extends GuiElement
{
	protected Minecraft mc;
	protected GuiMenuScreen g;
	private List<GuiContextButton> buttonList = new ArrayList<GuiContextButton>();

	private GuiElement context;
	private int contextIndex = 1;
	private final int contextX = super.xPos+62;
	private final int contextY = super.yPos+11;
	private final int contextWidth = super.width;
	private final int contextHeight = super.height-super.yPos-11;

	/**
	 * This is the container for all other gui elements
	 */
	public GuiMenu(Minecraft mc, ScaledResolution screen,GuiMenuScreen g)
	{
		super(screen.getScaledWidth()/2-115, screen.getScaledHeight()/2-73, 230, 146,g);
		this.mc = mc;
		this.g = g;
		init();
	}



	@Override
	public void draw(ScaledResolution screen) 
	{
		//CONTAINER
		g.drawWBorderedRect(super.xPos, super.yPos, super.xPos+super.width,super.height, 0,0x99000000, 0x99000000);
		//HEADER
		g.drawBorderedNTGradientRectWithString ("BattleChatter",super.xPos+1, super.yPos, super.xPos+super.width, super.yPos+10,1,0x80000000, 0x99000000,0x99303030 , 0x99FFFFFF);//topbar
		//EXIT BUTTON
		g.drawBorderedNTGradientRectWithString("x",super.xPos+super.width-10, super.yPos+1, super.xPos+super.width-1,super.yPos+9,-0,0x80000000,0x99800000, 0x99111111, 0x99FFFFFF );
		//CONTEXT BACKGROUND
		g.drawWBorderedRect(super.xPos, super.yPos+10,super.xPos+62, super.height, 1, 0x99000000,0x99787878 );

		for(int i = 0; i < buttonList.size();i++)
			buttonList.get(i).draw(screen);
		context.draw(screen);
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton, int direction) 
	{
		if(mouseX > super.xPos+super.width-10 &&  mouseY>  super.yPos+1 && mouseX <super.xPos+super.width-1 && mouseY<super.yPos+9)
		{
			mc.displayGuiScreen(null);
		}
		//else
		//{
			for(int i = 0; i < buttonList.size();i++)
			{
				if(buttonList.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
				{
					//Changes the context
					if(getContextIndex() != i)
						setContext(i);

					//Unselect all other context buttons
					for(int o = 0; o < buttonList.size();o++)
						if(i != o)
							buttonList.get(o).setSelected(false);

					return true;
				}
			}

			if(context.handleMouseInput(mouseX, mouseY, mouseButton, direction))
			{
				return true;
			}
		//}

		return false;
	}


	@Override
	public boolean keyTyped(char par1, int par2) 
	{
		context.keyTyped(par1, par2);

		return false;
	}

	@Override
	public void init() 
	{
		buttonList.add(new GuiContextButton("Quick Menu", 	super.xPos+1,	super.yPos+10     ,60,15,super.g));
		buttonList.add(new GuiContextButton("Controls", 	super.xPos+1, 	super.yPos+10*2+10,60,15,super.g));
		buttonList.add(new GuiContextButton("Name Tags", 	super.xPos+1, 	super.yPos+10*3+20,60,15,super.g));
		buttonList.add(new GuiContextButton("Radar", 		super.xPos+1, 	super.yPos+10*4+30,60,15,super.g));
		buttonList.add(new GuiContextButton("Friends", 		super.xPos+1, 	super.yPos+10*5+40,60,15,super.g));
		buttonList.add(new GuiContextButton("IRC", 		super.xPos+1, 	super.yPos+10*6+50,60,15,super.g));
		buttonList.add(new GuiContextButton("About", 	super.xPos+1, 	super.yPos+10*7+60,60,15,super.g));

		buttonList.get(0).setSelected(true);
		setContext(0);
	}

	public int getSelectedContext()
	{
		for(int i =0; i < buttonList.size();i++)
		{
			if(buttonList.get(i).isSelected())
			{
				return i;
			}
		}
		return 0;
	}
	public void setContext(int index)
	{

		switch(index)
		{
		case 0: context = new GuiQuickMenuContext(contextX, contextY, super.xPos+contextWidth, contextHeight, g); 
		contextIndex=0;
		break;
		case 1: context = new GuiControlsContext(contextX, contextY, super.xPos+contextWidth, contextHeight, g);
		contextIndex=1;
		break;
		case 2: context = new GuiNameTagsContext(contextX, contextY, super.xPos+contextWidth, contextHeight, g);
		contextIndex=2;
		break;
		case 3: context = new GuiRadarContext(contextX, contextY,super.xPos+ contextWidth, contextHeight, g);
		contextIndex=3;
		break;
		case 4: context = new GuiFriendsContext(contextX, contextY,super.xPos+ contextWidth, contextHeight, g);
		contextIndex=4;
		break;
		case 5: context = new GuiOtherContext(contextX, contextY, super.xPos+contextWidth, contextHeight, g);
		contextIndex=5;
		break;
		case 6: context = new GuiAboutContext(contextX, contextY,super.xPos+ contextWidth, contextHeight, g);
		contextIndex=6;
		break;
		}
	}
	public int getContextIndex()
	{
		return this.contextIndex;
	}
	public GuiMenuScreen getMenuScreen()
	{
		return this.g;
	}

}
