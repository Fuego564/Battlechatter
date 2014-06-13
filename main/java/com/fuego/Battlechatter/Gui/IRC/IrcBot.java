package com.fuego.Battlechatter.Gui.IRC;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.fuego.Battlechatter.Gui.NotificationManager.NotificationManager;
import com.fuego.Battlechatter.Util.Settings;
import com.fuego.Battlechatter.Util.BattleAwareness.AwareEntity;
import com.fuego.Battlechatter.Util.BattleAwareness.BattleAwareness;

public class IrcBot extends PircBot
{

	private final String username;
	private Minecraft mc;
	private String channel;
	private String server;
	public IrcBot(String username, Minecraft mc,String server, String channel)
	{
		this.username = username;
		this.mc = mc;
		this.server = server;
		this.channel = channel;
		this.setName(username);
	}
	@Override
	protected void onMessage(String channel, String sender, String login, String hostname,String message)
	{
		if(message.startsWith("BA%"))
			onBattleAwareMessage(sender,message);
		else
			addChatMessage(Settings.ircSenderColor+sender+": "+Settings.ircMessageColor+message);
	}
	@Override
	protected void  onPrivateMessage(String sender,String login, String hostname, String message)
	{
		addChatMessage(Settings.ircSenderColor+sender+":[] "+Settings.ircMessageColor+message);
	}
	@Override
	protected void onAction(String sender, String login, String hostname, String target, String action) 
	{
		addChatMessage(Settings.ircSenderColor+sender+" "+Settings.ircMessageColor+action);
	}

	@Override
	protected void onJoin(String channel, String sender, String login, String hostname)
	{
		addChatMessage(Settings.ircSenderColor+sender+Settings.ircMessageColor+" joined "+channel);
	}
	@Override
	protected void onPart(String channel, String sender, String login, String hostname) 
	{
		addChatMessage(Settings.ircSenderColor+sender+Settings.ircMessageColor+" left "+channel);
		handleDC(sender);
	}
	protected void onQuit(String sourceNick,String sourceLogin, String sourceHostName,String reason)
	{
		addChatMessage(Settings.ircSenderColor+sourceNick+Settings.ircMessageColor+" disconnected.");
		handleDC(sourceNick);
	}
	protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason)
	{
		addChatMessage(Settings.ircSenderColor+recipientNick+Settings.ircMessageColor+" was kicked by "+ Settings.ircSenderColor+kickerNick+Settings.ircMessageColor+" for "+reason);
		handleDC(recipientNick);
	}

	protected void onNickChange(String oldN, String login, String hostname, String newNick)
	{
		addChatMessage(Settings.ircSenderColor+oldN+Settings.ircMessageColor+"is now known as "+Settings.ircSenderColor+newNick);
		handleDC(oldN);
	}

	private void addChatMessage(String s)
	{
		mc.thePlayer.addChatMessage(new ChatComponentText(Settings.ircTagColor+"[IRC]"+s));
	}
	private void onBattleAwareMessage(String user, String message)
	{
		
		String[] s1 = message.split("%");

		if(s1[1].equalsIgnoreCase("false"))
		{
			BattleAwareness.removeEntity(BattleAwareness.indexOf(user));
			return;
		}
		
		
		int entityX=0;
		int entityZ=0;
		int entityHealth=0;
		
		try
		{
			entityX = Integer.parseInt(s1[1]);
			entityZ = Integer.parseInt(s1[2]);
			entityHealth = Integer.parseInt(s1[3]);
		}
		catch(Exception e)
		{
			addChatMessage("Danger please report" + e);
			return;
		}

		int index= BattleAwareness.indexOf(user);

		if(index!=-1)
		{
			BattleAwareness.replaceEntity(index, entityX, entityZ,entityHealth);
		}
		else
		{
			BattleAwareness.addEntity(new AwareEntity(user,entityX, entityZ,entityHealth));
		}

	}
	private void handleDC(String user)
	{
		int index = BattleAwareness.indexOf(user);

		if(index!=-1)
		{
			BattleAwareness.removeEntity(index);
		}
	}
}
