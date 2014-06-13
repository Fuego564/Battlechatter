package com.fuego.Battlechatter.Gui.Radar;

import org.lwjgl.opengl.GL11;

import com.fuego.Battlechatter.Gui.GuiOverlay;
import com.fuego.Battlechatter.Util.Settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class RadarElement
{
	private EntityPlayer e;
	
	private String username;
	private GuiOverlay g;
	private final long creationTime = System.currentTimeMillis();
	private final long animationTime = 500;
	private final long endPosition=2;
	private final long startPosition = -100;
	private final long distance = endPosition - startPosition;
	private int health=-1;

	public RadarElement(EntityPlayer e, GuiOverlay g)
	{
		this.e = e;
		this.g = g;
	}
	public void draw(ScaledResolution screen, FontRenderer f,int index)
	{
		if(Settings.radarType==0)
			drawLocationRadar( screen,  f, index);
		else if(Settings.radarType==1)
			drawDistanceRadar(screen,f,index);

	}


	public void drawLocationRadar(ScaledResolution screen, FontRenderer f,int index)
	{
		int animPosition =getAnimationPosition();
		
		if(Settings.radarSide==0)
		{
			String s= Settings.radarColor+e.getDisplayName()+"["+(int)e.posX+","+(int)e.posZ+"]"+getHealth(health);
			g.drawSideGradientRect(animPosition-2, 10+index*15-3, animPosition+155, 10+index*15+10, 0x00000000, 0xFF000000);
			f.drawStringWithShadow(s, animPosition,10+index*15, 16777215);
		}
		else
		{
			String s = getHealth(health)+Settings.radarColor+e.getDisplayName()+"["+(int)e.posX+","+(int)e.posZ+"]";
			g.drawSideGradientRect(screen.getScaledWidth()-150-animPosition-2, 10+index*15-3, screen.getScaledWidth()-animPosition+125, 10+index*15+10, 0xFF000000, 0x00000000);
			f.drawStringWithShadow(s, screen.getScaledWidth()-f.getStringWidth(s)+f.getStringWidth(getHealth(health))/2-2-animPosition,10+index*15, 16777215);
		}
	}
	public void drawDistanceRadar(ScaledResolution screen, FontRenderer f,int index)
	{
		int animPosition =getAnimationPosition();
		int distance = distanceFromEntity();
		
		if(Settings.radarSide==0)
		{
			String s = Settings.radarColor+e.getDisplayName()+" ["+distance+"]"+getHealth(health);
			g.drawSideGradientRect(animPosition-2, 10+index*15-3, animPosition+125, 10+index*15+10, 0x00000000, 0xFF000000);
			f.drawStringWithShadow(s, animPosition,10+index*15, 16777215);
		}
		else
		{
			String s =getHealth(health)+ Settings.radarColor+e.getDisplayName()+" ["+distance+"]";
			g.drawSideGradientRect(screen.getScaledWidth()-120-animPosition-2, 10+index*15-3, screen.getScaledWidth()-animPosition+125, 10+index*15+10, 0xFF000000, 0x00000000);
			f.drawStringWithShadow(s, screen.getScaledWidth()-f.getStringWidth(s)+f.getStringWidth(getHealth(health))/2-2-animPosition,10+index*15, 16777215);
		}
	}
	public String getDisplayName()
	{
		return e.getDisplayName();
	}
	public boolean isDoneAnimating()
	{
		return System.currentTimeMillis() > creationTime+animationTime;
	}
	public int getAnimationPosition()
	{
		int result = (int)endPosition;



		if(!isDoneAnimating())//start position +percentage of time completed
		{
			long time =  System.currentTimeMillis();

			float test = distance*(time - creationTime)/animationTime;

			result = (int)(test+startPosition);
		}


		return result;	
	}
	private int distanceFromEntity()
	{
		EntityClientPlayerMP p = Minecraft.getMinecraft().thePlayer;

		int distance = (int)Math.sqrt((p.posX - e.posX)*(p.posX - e.posX)+(p.posZ - e.posZ)*(p.posZ -e.posZ)+(p.posY-1-e.posY)*(p.posY-1-e.posY));

		return distance;
	}
	public void setHealth(int i)
	{
		this.health = i;
	}
	private String getHealth(int h)
	{
		String result = getHealthColor(h);
		
		for(int i =0; i< h;i++)
		{
			result+="\u275A";
		}
		
		return result;
	}
	private String getHealthColor(int h)
	{
		if(h==5)
			return "\2472";
		else if(h==4)
			return "\247a";
		else if(h==3)
			return "\247e";
		else if (h==2)
			return "\2476";
		else if (h==1)
			return "\2474";
		else
			return "";
	}
}
