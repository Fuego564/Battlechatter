package com.fuego.Battlechatter.Gui.Contexts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;
import com.fuego.Battlechatter.Gui.Elements.GuiButton;
import com.fuego.Battlechatter.Gui.Elements.GuiElement;
import com.fuego.Battlechatter.Gui.Elements.GuiFontSpinner;
import com.fuego.Battlechatter.Gui.Elements.GuiTextbox;
import com.fuego.Battlechatter.Gui.NotificationManager.NotificationManager;
import com.fuego.Battlechatter.Util.Settings;

public class GuiOtherContext extends GuiElement
{
	/*
	 * Needs IRC server and channel option
	 */


	private List<GuiTextbox> textboxes = new ArrayList<GuiTextbox>();
	private List<GuiButton> buttons = new ArrayList<GuiButton>();
	private List<GuiFontSpinner> spinners = new ArrayList<GuiFontSpinner>();


	private GuiButton cancel;
	private GuiButton save;
	public GuiOtherContext(int xPos, int yPos, int width, int height, GuiMenuScreen g) {
		super(xPos, yPos, width, height, g);
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {
		cancel.draw(screen);
		save.draw(screen);

		for(int i=0;i<textboxes.size();i++)
			textboxes.get(i).draw(screen);
		for(int i=0;i<buttons.size();i++)
			buttons.get(i).draw(screen);
		for(int i =0; i <spinners.size();i++)
			spinners.get(i).draw(screen);
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {


		if(cancel.handleMouseInput(mouseX, mouseY, mouseButton, direction))
		{
			super.close();
		}
		else if (save.handleMouseInput(mouseX, mouseY, mouseButton, direction))
		{
			updateSettings();
		}
		else
		{
			for(int i=0;i<textboxes.size();i++)
				if(textboxes.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
				{

				}
			for(int i=0;i<buttons.size();i++)
				if(buttons.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
				{
					if(i==0)
					{
						Settings.ircServer = textboxes.get(0).getText();
						Settings.save();
					}
					else if(i==1)
					{
						Settings.ircChannel = textboxes.get(1).getText();
						Settings.save();
					}
				}
			for(int i =0; i <spinners.size();i++)
			{
				if(spinners.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
				{

				}
			}

		}




		return false;
	}

	@Override
	public boolean keyTyped(char par1, int par2) {
		for(int i=0;i<textboxes.size();i++)
			textboxes.get(i).keyTyped(par1, par2);


		return false;
	}
	private void updateSettings() {

		Settings.ircServer = textboxes.get(0).getText();
		Settings.ircChannel = textboxes.get(1).getText();
		Settings.ircTagColor = spinners.get(0).getSelection();
		Settings.ircSenderColor = spinners.get(1).getSelection();
		Settings.ircMessageColor = spinners.get(2).getSelection();
		Settings.save();
		NotificationManager.addNotification("Saved Name Tag Settings");
	}
	@Override
	public void init() {
		cancel =new GuiButton(super.xPos+5, super.yPos+118, 65, 15, g,"   Cancel");
		save  = new GuiButton(super.width-70, super.yPos+118, 65, 15, g,"     Save");
		textboxes.add(new GuiTextbox(super.xPos+5, super.yPos,130, 15,30, g,"IRC Server",Settings.ircServer));
		textboxes.add(new GuiTextbox(super.xPos+5, super.yPos+20,130, 15,30, g,"IRC channel",Settings.ircChannel));
		buttons.add(new GuiButton(super.width-30, super.yPos,25, 15, g,"Set"));
		buttons.add(new GuiButton(super.width-30, super.yPos+20,25, 15, g,"Set"));
		spinners.add(new GuiFontSpinner(super.xPos,super.yPos+40,super.width,15, g, 1,"[IRC] Tag Color",Settings.ircTagColor));
		spinners.add(new GuiFontSpinner(super.xPos,super.yPos+60,super.width,15, g, 1,"User Color",Settings.ircSenderColor));
		spinners.add(new GuiFontSpinner(super.xPos,super.yPos+80,super.width,15, g, 1,"Message Color",Settings.ircMessageColor));
		

	}

}
