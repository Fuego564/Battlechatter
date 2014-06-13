package com.fuego.Battlechatter.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Settings
{


	public static int menu_key= 50;
	public static int radar_key = 19;
	public static int irc_key = 21;

	public static boolean useCustomTags = true;
	public static String tagColor = "\247a";
	public static String tagModifier = "\247l";
	public static boolean useClanTags = true;
	public static String clanColor = "\247e";
	public static String clanModifier = "\247r";

	public static int radarType = 0;
	public static int radarSide = 0;
	public static String radarColor = "\247a";

	public static boolean friendlyFireDefault = true;
	public static boolean allowNotifications = true;

	public static String clanTag="Battlechatter";

	public static String ircServer="irc.esper.net";
	public static String ircChannel="battlechatterdev";
	public static String ircTagColor="\247a";
	public static String ircSenderColor = "\247b";
	public static String ircMessageColor="\247a";
	
	public static boolean tempHealthAwareness=false;
	public static boolean tempFriendlyFire=false;
	public static boolean tempNameTags=false;
	private File settings;

	public Settings()
	{
		settings = new File("Battlechatter/settings.txt");

		if(settings.exists())
		{
			System.out.println("Found");
			readSettings();
		}
		else
		{
			setupDefaults();
		}
	}


	@SuppressWarnings("resource")
	public void readSettings()
	{

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(settings));

			while(reader.ready())
			{
				String line = reader.readLine();

				if(line.startsWith("menu_key"))
				{
					String s= line.split(":")[1];
					Settings.menu_key = Integer.parseInt(s);
				}
				else if(line.startsWith("radar_key"))
				{
					String s= line.split(":")[1];
					Settings.radar_key = Integer.parseInt(s);
				}
				else if(line.startsWith("irc_key"))
				{
					String s= line.split(":")[1];
					Settings.irc_key = Integer.parseInt(s);
				}
				else if(line.startsWith("usecustomTags"))
				{
					String s= line.split(":")[1];
					Settings.useCustomTags = Boolean.parseBoolean(s);
					tempNameTags = Settings.useCustomTags;
				}
				else if(line.startsWith("tagColor"))
				{
					String s= line.split(":")[1];
					Settings.tagColor = s;
				}
				else if(line.startsWith("tagModifier"))
				{
					String s= line.split(":")[1];
					Settings.tagModifier = s;
				}
				else if( line.startsWith("useClanTags"))
				{
					String s= line.split(":")[1];
					Settings.useClanTags = Boolean.parseBoolean(s);
				}
				else if(line.startsWith("clanColor"))
				{
					String s= line.split(":")[1];
					Settings.clanColor = s;
				}
				else if(line.startsWith("clanModifier"))
				{
					String s= line.split(":")[1];
					Settings.clanModifier = s;
				}
				else if(line.startsWith("radarType"))
				{
					String s= line.split(":")[1];
					Settings.radarType = Integer.parseInt(s);
				}
				else if(line.startsWith("isRadarOnLeft"))
				{
					String s= line.split(":")[1];
					Settings.radarSide = Integer.parseInt(s);
				}
				else if(line.startsWith("radarColor"))
				{
					String s= line.split(":")[1];
					Settings.radarColor =s;
				}
				else if(line.startsWith("friendlyFireDefault"))
				{
					String s= line.split(":")[1];
					Settings.friendlyFireDefault= Boolean.parseBoolean(s);
					Settings.tempFriendlyFire = friendlyFireDefault;
				}
				else if(line.startsWith("allowNotifications"))
				{
					String s= line.split(":")[1];
					Settings.allowNotifications = Boolean.parseBoolean(s);
				}
				else if(line.startsWith("clanTag"))
				{
					String s= line.split(":")[1];
					Settings.clanTag = s;
				}
				else if(line.startsWith("ircServer"))
				{
					String s= line.split(":")[1];
					Settings.ircServer = s;
				}
				else if(line.startsWith("ircChannel"))
				{
					String s= line.split(":")[1];
					Settings.ircChannel = s;
				}
				else if(line.startsWith("ircTagColor"))
				{
					String s= line.split(":")[1];
					Settings.ircTagColor = s;
				}
				else if(line.startsWith("ircSenderColor"))
				{
					String s= line.split(":")[1];
					Settings.ircSenderColor = s;
				}
				else if(line.startsWith("ircMessageColor"))
				{
					String s= line.split(":")[1];
					Settings.ircMessageColor = s;
				}
			}
		}
		catch(Exception e)
		{
			
		}
	}

	public static void save()
	{
		try
		{
			PrintWriter writer = new PrintWriter(new FileWriter("Battlechatter/settings.txt"));

			writer.println("menu_key:"+menu_key);
			writer.println("radar_key:"+radar_key);
			writer.println("irc_key:"+irc_key);
			writer.println("usecustomTags:"+useCustomTags);
			writer.println("tagColor:"+tagColor);
			writer.println("tagModifier:"+tagModifier);
			writer.println("useClanTags:"+useClanTags);
			writer.println("clanColor:"+clanColor);
			writer.println("clanModifier:"+clanModifier);
			writer.println("radarType:"+radarType);
			writer.println("isRadarOnLeft:"+radarSide);
			writer.println("radarColor:"+radarColor);
			writer.println("friendlyFireDefault:"+friendlyFireDefault);
			writer.println("allowNotifications:"+allowNotifications);
			writer.println("ircServer:"+ircServer);
			writer.println("ircChannel:"+ircChannel);
			writer.println("ircTagColor:"+ircTagColor);
			writer.println("ircSenderColor:"+ircSenderColor);
			writer.println("ircMessageColor:"+ircMessageColor);
			writer.close();
		}
		catch(Exception e)
		{
			
		}
	}
	private void setupDefaults()
	{
		System.out.println("Writing"+settings.getAbsolutePath());

		try
		{

			File file = new File("Battlechatter");
			if(!file.exists())
				file.mkdir();

			PrintWriter writer = new PrintWriter(new FileWriter(settings));

			writer.println("menu_key:"+menu_key);
			writer.println("radar_key:"+radar_key);
			writer.println("irc_key:"+irc_key);
			writer.println("usecustomTags:"+useCustomTags);
			writer.println("tagColor:"+tagColor);
			writer.println("tagModifier:"+tagModifier);
			writer.println("useClanTags:"+useClanTags);
			writer.println("clanColor:"+clanColor);
			writer.println("clanModifier:"+clanModifier);
			writer.println("radarType:"+radarType);
			writer.println("isRadarOnLeft:"+radarSide);
			writer.println("radarColor:"+radarColor);
			writer.println("friendlyFireDefault:"+friendlyFireDefault);
			writer.println("allowNotifications:"+allowNotifications);
			writer.println("clanTag:"+clanTag);
			writer.println("ircServer:"+ircServer);
			writer.println("ircChannel:"+ircChannel);
			writer.println("ircTagColor:"+ircTagColor);
			writer.println("ircSenderColor:"+ircSenderColor);
			writer.println("ircMessageColor:"+ircMessageColor);
			writer.close();
		}
		catch(Exception e)
		{

		}
	}
}
