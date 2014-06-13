package com.fuego.Battlechatter.Input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;
import com.fuego.Battlechatter.Gui.GuiOverlay;
import com.fuego.Battlechatter.Gui.IRC.IrcChat;
import com.fuego.Battlechatter.Gui.NotificationManager.NotificationManager;
import com.fuego.Battlechatter.Util.Settings;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
/**
 * The primary inputHandler for battlechatter
 * @author Fuego
 *
 */
public class InputHandler 
{
	private GuiOverlay overlay;
	private KeyBinding menuBind;
	private KeyBinding radarBind;
	private KeyBinding ircBind;

	public InputHandler(GuiOverlay overlay)
	{
		this.overlay = overlay;
	//	this.settings = settings;
		initBinds();
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {

		if(Minecraft.getMinecraft().inGameHasFocus)
		{
			if(menuBind.isPressed())
			{
				Minecraft.getMinecraft().displayGuiScreen(new GuiMenuScreen(overlay));
			}
			else if( radarBind.isPressed())
			{
				overlay.toggleRadar();
				NotificationManager.addNotification("Radar is "+translateRadarState(overlay.isRadarEnabled()));
			}
			else if(ircBind.isPressed())
			{
				Minecraft.getMinecraft().displayGuiScreen(new IrcChat(true));
			}
		} 
	}
	/**
	 * Initializes the keybinds to be listened for during gameplay
	 */
	public void initBinds()
	{
		menuBind = new KeyBinding("key.menuBind",Settings.menu_key, "key.Fuego.battlechatter");
		ClientRegistry.registerKeyBinding(menuBind);
		radarBind = new KeyBinding("key.radarBind", Settings.radar_key,"key.Fuego.battlechatter");
		ClientRegistry.registerKeyBinding(radarBind);
		ircBind = new KeyBinding("key.ircBind",Settings.irc_key,"key.Fuego.battlechatter");
		ClientRegistry.registerKeyBinding(ircBind);
		
	}
	private String translateRadarState(boolean state)
	{
		if(state)
		{
			return "enabled";
		}
		else
		{
			return "disabled";
		}
	}
}