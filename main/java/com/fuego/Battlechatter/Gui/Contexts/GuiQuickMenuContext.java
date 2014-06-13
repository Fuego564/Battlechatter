package com.fuego.Battlechatter.Gui.Contexts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;
import com.fuego.Battlechatter.Gui.Elements.GuiButton;
import com.fuego.Battlechatter.Gui.Elements.GuiElement;
import com.fuego.Battlechatter.Gui.Elements.GuiToggleButton;
import com.fuego.Battlechatter.Gui.IRC.IrcChat;
import com.fuego.Battlechatter.Util.Settings;

public class GuiQuickMenuContext extends GuiElement
{
	private List<GuiToggleButton> toggles = new ArrayList<GuiToggleButton>();
	private long lastSent=0;
	private long delay=1000;
	public GuiQuickMenuContext(int xPos, int yPos, int width, int height,
			GuiMenuScreen g) {
		super(xPos, yPos, width, height, g);
		init();
	}

	@Override
	public void draw(ScaledResolution screen) {
		//g.drawWBorderedRect(super.xPos, super.yPos, super.width,super.height, 0,0x99000000, 0x99000000);
		for(int i =0; i <toggles.size();i++)
		{
			toggles.get(i).draw(screen);
		}

	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {
		for(int i =0; i <toggles.size();i++)
		{
			if(toggles.get(i).handleMouseInput(mouseX, mouseY, mouseButton, direction))
			{
				if(i==0)
				{
					g.getOverlay().setRadar(toggles.get(i).getState());
				}
				else if(i==1)//nameTags
				{
					Settings.tempNameTags = toggles.get(i).getState();
				}
				else if(i==2)//friendly fire
				{
					Settings.tempFriendlyFire = toggles.get(i).getState();
				}
				else if(i==3)//battleawareness
				{
					if(IrcChat.ircBot!=null && IrcChat.ircMain.connected() && IrcChat.ircBot.isConnected()&& lastSent<System.currentTimeMillis()+delay)
					{
						Settings.tempHealthAwareness = toggles.get(i).getState();
						
						if(toggles.get(i).getState())
						{
							IrcChat.ircBot.sendMessage(IrcChat.ircBot.getChannels()[0], "BA%false");
						}
						
					}
					else
						toggles.get(i).setState(false);
				}
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char par1, int par2) {

		return false;
	}

	@Override
	public void init() {
		System.out.println("TEST");
		toggles.add(new GuiToggleButton(super.width-70, super.yPos,    65, 15, g,"Radar",g.getOverlay().isRadarEnabled()));
		toggles.add(new GuiToggleButton(super.width-70, super.yPos+20, 65, 15, g,"Name Tags",Settings.tempNameTags));
		toggles.add(new GuiToggleButton(super.width-70, super.yPos+40, 65, 15, g,"Friendly Fire",Settings.tempFriendlyFire));
		toggles.add(new GuiToggleButton(super.width-70, super.yPos+60, 65, 15, g,"Battle Aware",Settings.tempHealthAwareness));
	}

}
