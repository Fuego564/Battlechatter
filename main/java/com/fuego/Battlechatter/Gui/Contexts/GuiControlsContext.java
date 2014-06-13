package com.fuego.Battlechatter.Gui.Contexts;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;
import com.fuego.Battlechatter.Gui.Elements.GuiButton;
import com.fuego.Battlechatter.Gui.Elements.GuiControlButton;
import com.fuego.Battlechatter.Gui.Elements.GuiElement;
import com.fuego.Battlechatter.Gui.Elements.GuiToggleButton;
import com.fuego.Battlechatter.Gui.NotificationManager.NotificationManager;
import com.fuego.Battlechatter.Util.Settings;

public class GuiControlsContext extends GuiElement
{
	private GuiButton cancel;
	private GuiButton save;
	private List<GuiControlButton> buttons = new ArrayList<GuiControlButton>();

	public GuiControlsContext(int xPos, int yPos, int width, int height,
			GuiMenuScreen g) {
		super(xPos, yPos, width, height, g);
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {
		cancel.draw(screen);
		save.draw(screen);
		for(int i =0; i < buttons.size();i++)
			buttons.get(i).draw(screen);
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) 
	{
		for(int i =0; i < buttons.size();i++)
		{
			buttons.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction);
		}
		if(cancel.handleMouseInput(mouseX, mouseY, mouseButton, direction))
		{
			super.close();
		}
		else if (save.handleMouseInput(mouseX, mouseY, mouseButton, direction))
		{
			updateSettings();
		}
		return false;
	}

	@Override
	public boolean keyTyped(char par1, int par2) {
		for(int i =0; i < buttons.size();i++)
			buttons.get(i).keyTyped(par1, par2);
		//System.out.println(par2);
		return false;
	}
	public void updateSettings()
	{
		if(!buttons.get(0).getKeyString().equals(buttons.get(1).getKeyString())
				&& !buttons.get(0).getKeyString().equals(buttons.get(2).getKeyString()) 
				&& !buttons.get(1).getKeyString().equals(buttons.get(2).getKeyString()))
		{
			Settings.menu_key = Keyboard.getKeyIndex(buttons.get(0).getKeyString());
			Settings.radar_key = Keyboard.getKeyIndex(buttons.get(1).getKeyString());
			Settings.irc_key = Keyboard.getKeyIndex(buttons.get(2).getKeyString());
			Settings.save();
			NotificationManager.addNotification("Saved Control Settings");
			g.getOverlay().getInputHandler().initBinds();
		}
		else
		{
			NotificationManager.addNotification("Cannot save repeated binds!");
		}

	}



	@Override
	public void init() {
		cancel =new GuiButton(super.xPos+5, super.yPos+118, 65, 15, g,"   Cancel");
		save  = new GuiButton(super.width-70, super.yPos+118, 65, 15, g,"     Save");

		buttons.add(new GuiControlButton(super.width-35, super.yPos, 30, 15, g,"Menu Key",Keyboard.getKeyName(Settings.menu_key).charAt(0)));
		buttons.add(new GuiControlButton(super.width-35, super.yPos+20, 30, 15, g,"Radar Key",Keyboard.getKeyName(Settings.radar_key).charAt(0)));
		buttons.add(new GuiControlButton(super.width-35, super.yPos+40, 30, 15, g,"IRC Key",Keyboard.getKeyName(Settings.irc_key).charAt(0)));

	}

}
