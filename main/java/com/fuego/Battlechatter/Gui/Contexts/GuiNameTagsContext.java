package com.fuego.Battlechatter.Gui.Contexts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;
import com.fuego.Battlechatter.Gui.Elements.GuiButton;
import com.fuego.Battlechatter.Gui.Elements.GuiElement;
import com.fuego.Battlechatter.Gui.Elements.GuiFontSpinner;
import com.fuego.Battlechatter.Gui.Elements.GuiToggleButton;
import com.fuego.Battlechatter.Gui.NotificationManager.NotificationManager;
import com.fuego.Battlechatter.Util.Settings;

public class GuiNameTagsContext extends GuiElement
{
	/*On by default
	Color
	modifier
	use clan tag?
	clan tag color
	clan tag modifier
	apply
	cancel
	 */
	private List<GuiToggleButton> toggles = new ArrayList<GuiToggleButton>();
	private List<GuiFontSpinner> spinners = new ArrayList<GuiFontSpinner>();
	private GuiButton cancel;
	private GuiButton save;
	
	public GuiNameTagsContext(int xPos, int yPos, int width, int height,
			GuiMenuScreen g) {
		super(xPos, yPos, width, height, g);
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {
		for(int i =0; i <toggles.size();i++)
		{
			toggles.get(i).draw(screen);
		}
		for(int i =0; i <spinners.size();i++)
		{
			spinners.get(i).draw(screen);
		}
		cancel.draw(screen);
		save.draw(screen);

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
		for(int i =0; i <spinners.size();i++)
		{
			if(spinners.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
			{
				
			}
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

	private void updateSettings() {
		
		Settings.useCustomTags = toggles.get(0).getState();
		Settings.tagColor = spinners.get(0).getSelection();
		Settings.tagModifier = spinners.get(1).getSelection();
		Settings.useClanTags = toggles.get(1).getState();
		Settings.clanColor = spinners.get(2).getSelection();
		Settings.clanModifier = spinners.get(3).getSelection();
		Settings.save();
		
		NotificationManager.addNotification("Saved Name Tag Settings");
	}

	@Override
	public boolean keyTyped(char par1, int par2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init() {
		toggles.add(new GuiToggleButton(super.width-70, super.yPos,65, 15, g,"Custom Tags",Settings.useCustomTags));
		spinners.add(new GuiFontSpinner(super.xPos,super.yPos+20,super.width,15, g, 1,"Tag Color",Settings.tagColor));
		spinners.add(new GuiFontSpinner(super.xPos,super.yPos+40,super.width,15, g, 2,"Tag Modifier",Settings.tagModifier));
		toggles.add(new GuiToggleButton(super.width-70, super.yPos+60, 65, 15, g,"Clan Tags",Settings.useClanTags));
		spinners.add(new GuiFontSpinner(super.xPos,super.yPos+80,super.width,15, g, 1,"Clan Tag Color",Settings.clanColor));
		spinners.add(new GuiFontSpinner(super.xPos,super.yPos+100,super.width,15, g, 2,"Clan Tag Modifier",Settings.clanModifier));
		
		cancel =new GuiButton(super.xPos+5, super.yPos+118, 65, 15, g,"   Cancel");
		save  = new GuiButton(super.width-70, super.yPos+118, 65, 15, g,"     Save");
	}

}
