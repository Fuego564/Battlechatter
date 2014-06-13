package com.fuego.Battlechatter.Gui.Radar;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.fuego.Battlechatter.Gui.GuiOverlay;
import com.fuego.Battlechatter.Gui.Radar.RadarElement;
import com.fuego.Battlechatter.Util.FriendsList;
import com.fuego.Battlechatter.Util.BattleAwareness.BattleAwareness;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiRadar 
{
	private List<RadarElement> elements = new ArrayList<RadarElement>();
	private Minecraft mc;

	private GuiOverlay g;

	public GuiRadar(Minecraft mc, GuiOverlay guiOverlay)
	{
		this.mc = mc;
		this.g = guiOverlay;

	}


	public void draw(ScaledResolution screen, FontRenderer f)
	{		
		List<String>players = new ArrayList<String>();

		for( Object o : mc.theWorld.playerEntities)//add all elements
		{
			Entity entity = (Entity)o;

			if(entity instanceof EntityPlayer)
			{
				EntityPlayer e = (EntityPlayer) entity;

				if(!e.getDisplayName().equalsIgnoreCase(mc.thePlayer.getDisplayName())&& FriendsList.contains(e.getDisplayName()))
				{
					players.add(e.getDisplayName());

					if(indexOfEntity( e)==-1)
					{
						elements.add(new RadarElement(e,g));
						
						int index = BattleAwareness.indexOf(e.getDisplayName());

						if(index !=-1)
						{
							elements.get(elements.size()-1).setHealth(BattleAwareness.getHealth(index));
						}
						else
						{
							elements.get(elements.size()-1).setHealth(-1);
						}
					}
				}
			}
		}

		for(int i = elements.size()-1; i >=0;i--)//Draw all elements and remove all missing elements
		{
			if(players.contains(elements.get(i).getDisplayName()))
			{
				elements.get(i).draw(screen, f, i);
				
				int index = BattleAwareness.indexOf(elements.get(i).getDisplayName());
				
				if(index!=-1)
				{
					elements.get(i).setHealth(BattleAwareness.getHealth(index));
				}
				else
				{
					elements.get(i).setHealth(-1);
				}
			}
			else
			{
				elements.remove(i);
			}
		}
		//draw all elements that arent within render
		
		for(int i=0; i < BattleAwareness.size();i++)
		{
			if(!containsUser(BattleAwareness.getName(i)))
			{
				BattleAwareness.getEntity(i).draw(screen, f, i+elements.size());
			}
		}


	}
	private int indexOfEntity(EntityPlayer e)
	{
		for(int i = 0; i < elements.size();i++)
		{
			if(e.getDisplayName().equalsIgnoreCase(elements.get(i).getDisplayName()))
			{
				return i;
			}
		}
		return -1;
	}
	private boolean containsUser(String s)
	{
		for(int i=0;i<elements.size();i++)
			if(elements.get(i).getDisplayName().equalsIgnoreCase(s))
				return true;
		return false;
	}

}
