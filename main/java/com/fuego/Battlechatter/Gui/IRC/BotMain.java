package com.fuego.Battlechatter.Gui.IRC;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class BotMain extends PircBot implements Runnable
{

	private String server;
	private String channel;
	private String username;
	private Minecraft mc;
	public static IrcBot ircbot;
	private boolean isConnected=false;
	/**
	 * 
	 * @param server
	 * @param channel
	 * @param username
	 * @param mc
	 */
	public BotMain(String server, String channel,String username,Minecraft mc)
	{
		this.server = server;
		this.channel = channel;
		this.username = username;
		this.mc = mc;
		ircbot = new IrcBot(username,mc,server,channel);
	}
	
	@Override
	public void run() {
		try
		{
			ircbot.setVerbose(true);
			ircbot.connect(server);
			ircbot.joinChannel(channel);
			isConnected=true;
		}
		catch(IOException e)
		{
			isConnected=false;
			this.addChatMessage("Connection error: "+ e);
		}
		catch(NickAlreadyInUseException e)
		{
			isConnected=false;
			this.addChatMessage("The username given is already in use.");
		}
		catch(IrcException e)
		{
			isConnected=false;
			this.addChatMessage("IRC ERROR:"+e);
		}
		
	}
	
	private void addChatMessage(String s)
	{
		mc.thePlayer.addChatMessage(new ChatComponentText(s));
	}
	
	public boolean connected()
	{
		return isConnected;
	}
	public String server()
	{
		return server;
	}
	public String channel()
	{
		return channel;
	}
	public IrcBot getIrcBot()
	{
		return ircbot;
		
	}
	
}
