package com.fuego.Battlechatter.Util.BattleAwareness;

import java.util.ArrayList;
import java.util.List;

public class BattleAwareness 
{

	private static List<AwareEntity> awareEntities = new ArrayList<AwareEntity>();
	
	public BattleAwareness()
	{
		
	}
	public static AwareEntity getEntity(int i)
	{
		return awareEntities.get(i);
	}
	public static void addEntity(AwareEntity e)
	{
		awareEntities.add(e);
	}
	public static void removeEntity(int index)
	{
		awareEntities.remove(index);
	}
	public static void replaceEntity(int i, int x, int z , int health)
	{
		awareEntities.get(i).setxPos(x);
		awareEntities.get(i).setzPos(z);
		awareEntities.get(i).setHealth(health);
	}
	public static boolean contains(String s)
	{
		for(int i =0; i < awareEntities.size();i++)
			if(awareEntities.get(i).getName().equalsIgnoreCase(s))
				return true;
		return false;
	}
	public static int getHealth(int i)
	{
		return awareEntities.get(i).getHealth();
	}
	public static String getName(int i)
	{
		return awareEntities.get(i).getName();
	}
	public static int getxPos(int i)
	{
		return awareEntities.get(i).getxPos();
	}
	public static int getzPos(int i)
	{
		return awareEntities.get(i).getzPos();
	}
	public static int indexOf(String s)
	{
		for(int i =0; i< awareEntities.size();i++)
		{
			if(awareEntities.get(i).getName().equalsIgnoreCase(s))
				return i;
		}
		
		
		return -1;
	}

	public static void clear()
	{
		awareEntities.clear();
	}
	public static int size()
	{
		return awareEntities.size();
	}
}
