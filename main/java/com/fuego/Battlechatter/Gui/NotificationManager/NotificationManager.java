package com.fuego.Battlechatter.Gui.NotificationManager;

import java.util.ArrayList;
import java.util.List;

import com.fuego.Battlechatter.Gui.GuiOverlay;
import com.fuego.Battlechatter.Util.FriendsList;
import com.fuego.Battlechatter.Util.Settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;

public class NotificationManager 
{
	private Minecraft mc;
	private GuiOverlay overlay;
	private static List<Notification> notes = new ArrayList<Notification>();	
	private List<String> friendsOnline = new ArrayList<String>();
	private boolean isFirstCheck=true;
	private int lastSize=-1;
	public NotificationManager(Minecraft mc, GuiOverlay overlay)
	{
		this.mc = mc;
		this.overlay = overlay;
	}

	public static void addNotification(String message)
	{
		notes.add(new Notification(message));
	}
	public void draw(ScaledResolution screen, FontRenderer f)
	{
		for(int i=notes.size()-1;i>=0;i--)//remove old notes
		{
			if(notes.get(i).hasDied())
			{
				notes.remove(i);
			}
		}
		for(int i=notes.size()-1;i>=0;i--)//remove old notes
		{
			notes.get(i).draw(screen,overlay, f, i);
		}
	}
	public void update()
	{
		if(Settings.allowNotifications)
		{
			try
			{
				NetHandlerPlayClient net = this.mc.thePlayer.sendQueue;

				List<GuiPlayerInfo> pl = net.playerInfoList;	
				List<String> playerList = new ArrayList<String>();
				List<String> notificationQueue = new ArrayList<String>();

				if(lastSize!=pl.size())
				{
					for(int i=0;i<pl.size();i++)
					{
						if(!pl.get(i).name.equalsIgnoreCase(mc.thePlayer.getDisplayName()))
							playerList.add(pl.get(i).name);
					}


					for(int i =0;i<playerList.size();i++)//Checks for new friends on the server
					{
						if(!friendsOnline.contains(playerList.get(i)) && FriendsList.containsEnding(playerList.get(i)))
						{
							friendsOnline.add(playerList.get(i));		
							notificationQueue.add(playerList.get(i)+" joined the server!");
						}
					}
					lastSize = playerList.size();


					for(int i=friendsOnline.size()-1;i>=0;i--)//removes people who left
					{
						if(!playerList.contains(friendsOnline.get(i)))
						{
							addNotification(friendsOnline.get(i)+" left the server!");

							friendsOnline.remove(i);
						}
					}
					if(!isFirstCheck)
					{
						for(int i=0; i<notificationQueue.size();i++)
						{
							addNotification(notificationQueue.get(i));
						}
					}
					else
					{
						isFirstCheck = false;
					}
				}
			}
			catch(NullPointerException e)
			{
				wipe();
			}
		}
	}

	public void wipe() 
	{
		isFirstCheck = true;
		friendsOnline.clear();
		lastSize=-1;
	}


	/**
	 * How to prevent mass join/leave notification spam.
	 * check if first time checking for friends on this server. if so ignore all joins.
	 * On leave wipe the list of friends on server.
	 */
}
