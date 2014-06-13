package com.fuego.Battlechatter.Gui.Contexts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;
import com.fuego.Battlechatter.Gui.Elements.GuiButton;
import com.fuego.Battlechatter.Gui.Elements.GuiElement;
import com.fuego.Battlechatter.Gui.Elements.GuiFriendSpinner;
import com.fuego.Battlechatter.Gui.Elements.GuiTextbox;
import com.fuego.Battlechatter.Gui.Elements.GuiToggleButton;
import com.fuego.Battlechatter.Gui.NotificationManager.NotificationManager;
import com.fuego.Battlechatter.Util.FriendsList;
import com.fuego.Battlechatter.Util.Settings;

public class GuiFriendsContext extends GuiElement
{
	private GuiButton cancel;
	private GuiButton save;
	private List <GuiToggleButton> toggles = new ArrayList<GuiToggleButton>();
	private List<GuiTextbox> textBoxes = new ArrayList<GuiTextbox>();
	private List<GuiButton> buttons = new ArrayList<GuiButton>();
	private GuiFriendSpinner spinner;
	public GuiFriendsContext(int xPos, int yPos, int width, int height,
			GuiMenuScreen g) {
		super(xPos, yPos, width, height, g);
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {
		cancel.draw(screen);
		save.draw(screen);
		for(int i =0; i <toggles.size();i++)
			toggles.get(i).draw(screen);
		for(int i =0; i <textBoxes.size();i++)
			textBoxes.get(i).draw(screen);
		for(int i =0; i <buttons.size();i++)
			buttons.get(i).draw(screen);
		spinner.draw(screen);
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {
		for(int i =0; i <toggles.size();i++)
		{
			if(toggles.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
			{
				
			}
		}
		for(int i =0; i<textBoxes.size();i++)
		{
			if(textBoxes.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
			{

			}
		}
		for(int i =0; i<buttons.size();i++)
		{
			if(buttons.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
			{
				if(i==0)
				{
					if(textBoxes.get(0).getText().length()>0)
					{
						FriendsList.addEntry(textBoxes.get(0).getText());
						textBoxes.get(0).setText("");
					}
				}
				else if(i==1)
				{
					NotificationManager.addNotification("Clan tag set to \""+textBoxes.get(1).getText()+"\"");
					Settings.clanTag = textBoxes.get(1).getText();
					Settings.save();
				}
				else if(i==2)
				{
					int index = spinner.getIndex();

					if(index == FriendsList.size()-1 && index>0)
					{
						spinner.setIndex(index-1);
					}
					if(FriendsList.size()>0)
						FriendsList.remove(index);
					else
						NotificationManager.addNotification("No friends to remove!");
				}

			}
		}
		if(spinner.handleMouseInput(mouseX, mouseY, mouseButton, direction))
		{

		}
		else if(cancel.handleMouseInput(mouseX, mouseY, mouseButton, direction))
		{
			super.close();
		}
		else if (save.handleMouseInput(mouseX, mouseY, mouseButton, direction))
		{
			updateSettings();
		}

		return false;
	}

	public void updateSettings()
	{
		Settings.friendlyFireDefault = toggles.get(0).getState();
		Settings.tempFriendlyFire = toggles.get(0).getState();
		Settings.allowNotifications = toggles.get(1).getState();
		Settings.save();
		NotificationManager.addNotification("Saved Friends Settings");
	}
	@Override
	public boolean keyTyped(char par1, int par2) {

		for(int i =0; i<textBoxes.size();i++)
		{
			textBoxes.get(i).keyTyped(par1, par2);
		}
		return false;
	}

	@Override
	public void init() {
		cancel =new GuiButton(super.xPos+5, super.yPos+118, 65, 15, g,"   Cancel");
		save  = new GuiButton(super.width-70, super.yPos+118, 65, 15, g,"     Save");
		toggles.add(new GuiToggleButton(super.width-70, super.yPos,65, 15, g,"Friendly Fire",Settings.friendlyFireDefault));
		toggles.add(new GuiToggleButton(super.width-70, super.yPos+20,65, 15, g,"Join Notifications",Settings.allowNotifications));
		textBoxes.add(new GuiTextbox(super.xPos+5, super.yPos+40,130, 15,16, g,"New Friend's name"));
		textBoxes.add(new GuiTextbox(super.xPos+5, super.yPos+80,130, 15,16, g,"Clan tag",Settings.clanTag));
		buttons.add(new GuiButton(super.width-30, super.yPos+40,25, 15, g,"Add"));
		buttons.add(new GuiButton(super.width-30, super.yPos+80,25, 15, g,"Set"));
		buttons.add(new GuiButton(super.width-30, super.yPos+60,25, 15, g,"Del"));
		spinner = new GuiFriendSpinner(super.xPos,super.yPos+60,super.width,15, g);
	}

}
