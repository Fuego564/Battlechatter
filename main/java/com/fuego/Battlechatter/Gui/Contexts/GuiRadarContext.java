package com.fuego.Battlechatter.Gui.Contexts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;
import com.fuego.Battlechatter.Gui.Elements.GuiButton;
import com.fuego.Battlechatter.Gui.Elements.GuiElement;
import com.fuego.Battlechatter.Gui.Elements.GuiMultivalueButton;
import com.fuego.Battlechatter.Gui.Elements.GuiFontSpinner;
import com.fuego.Battlechatter.Gui.Elements.GuiToggleButton;
import com.fuego.Battlechatter.Gui.NotificationManager.NotificationManager;
import com.fuego.Battlechatter.Util.Settings;

public class GuiRadarContext extends GuiElement
{
	private List<GuiMultivalueButton> buttons = new ArrayList<GuiMultivalueButton>();
	private GuiButton cancel;
	private GuiButton save;
	private GuiFontSpinner spinner;
	public GuiRadarContext(int xPos, int yPos, int width, int height, GuiMenuScreen g) {
		super(xPos, yPos, width, height, g);
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {
		cancel.draw(screen);
		save.draw(screen);

		for(int i =0; i <buttons.size();i++)
		{
			buttons.get(i).draw(screen);
		}
		spinner.draw(screen);

	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {
		for(int i =0; i <buttons.size();i++)
		{
			if(buttons.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
			{

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
		Settings.radarType = buttons.get(0).getSelection();
		Settings.radarSide = buttons.get(1).getSelection();
		Settings.radarColor = spinner.getSelection();
		Settings.save();
		NotificationManager.addNotification("Saved Radar Settings");
	}
	
	
	@Override
	public boolean keyTyped(char par1, int par2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init() {
		cancel =new GuiButton(super.xPos+5, super.yPos+118, 65, 15, g,"   Cancel");
		save  = new GuiButton(super.width-70, super.yPos+118, 65, 15, g,"     Save");


		buttons.add(new GuiMultivalueButton(super.width-70, super.yPos,65, 15, g,"Type",new String[]{"Coordinates","  Distance"},Settings.radarType));
		buttons.add(new GuiMultivalueButton(super.width-70, super.yPos+20,65, 15, g,"Side",new String[]{"     Left","     Right"},Settings.radarSide));
		spinner = new GuiFontSpinner(super.xPos,super.yPos+40,super.width,15, g, 1,"Font Color",Settings.radarColor);
	}

}
