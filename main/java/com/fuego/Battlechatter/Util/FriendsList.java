package com.fuego.Battlechatter.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fuego.Battlechatter.Gui.NotificationManager.NotificationManager;

public class FriendsList 
{
	private static List<String> friendsList = new ArrayList<String>();

	private static File file = new File("Battlechatter/FriendsList.txt");


	public FriendsList()
	{
		if(file.exists())
		{
			load();
		}
		else
		{
			setupDefaults();
		}
	}
	
	private void setupDefaults()
	{
		friendsList.add("Fuego_Tortuga");
		save();
	}
	@SuppressWarnings("resource")
	public void load()
	{
		try
		{
			
			BufferedReader reader = new BufferedReader(new FileReader(file));	

			while(reader.ready())
			{
				String s = reader.readLine();

				if(s.startsWith("Friend:"))
				{
					friendsList.add(s.split(":")[1]); 
				}
			}
			Collections.sort(friendsList);

		}
		catch(Exception e)
		{

		}
	}
	private static void save()
	{
		try
		{
			PrintWriter writer = new PrintWriter(new FileWriter(file));

			for(int i=0; i< friendsList.size();i++)
			{
				writer.println("Friend:"+friendsList.get(i));
			}
			writer.close();

		}
		catch(Exception e)
		{

		}
	}
	public static void addEntry(String s)
	{
		NotificationManager.addNotification(s+" added to friends.");
		friendsList.add(s);
		Collections.sort(friendsList);
		save();
	}
	public static boolean contains(String s)
	{
		for(int i =0; i < friendsList.size();i++)
			if(friendsList.get(i).equalsIgnoreCase(s))
				return true;
		return false;
	}
	public static boolean containsEnding(String s)
	{
		for(int i =0; i < friendsList.size();i++)
			if(friendsList.get(i).toLowerCase().endsWith(s.toLowerCase()))
				return true;
		return false;
	}
	public static boolean containsStarting(String s)
	{
		for(int i =0; i < friendsList.size();i++)
			if(friendsList.get(i).toLowerCase().startsWith(s.toLowerCase()))
				return true;
		return false;
	}
	public static String get(int i)
	{
		return friendsList.get(i);
	}
	public static boolean containsInstance(String s)
	{
		for(int i =0; i < friendsList.size();i++)
			if(friendsList.get(i).toLowerCase().contains(s.toLowerCase()))
				return true;
		return false;
	}
	public static void remove(int i)
	{
		NotificationManager.addNotification(friendsList.get(i)+" removed from friends.");
		friendsList.remove(i);
		save();
	}
	public static int size()
	{
		return friendsList.size();
	}
}
