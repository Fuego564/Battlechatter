package com.fuego.Battlechatter.Util.BattleAwareness;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiOverlay;
import com.fuego.Battlechatter.Main.Battlechatter;
import com.fuego.Battlechatter.Util.Settings;

public class AwareEntity 
{
	private String name;
	private int health;
	private int xPos;
	private int zPos;
	
	
	private GuiOverlay g;
	private final long creationTime = System.currentTimeMillis();
	private final long animationTime = 500;
	private final long endPosition=2;
	private final long startPosition = -100;
	private final long distance = endPosition - startPosition;

	
	public AwareEntity(String name,int xPos, int zPos,int health)
	{
		this.name= name;
		this.xPos = xPos;
		this.zPos = zPos;
		this.health = health;
		this.g = Battlechatter.getOverlay();
		
	}
	
	public String getName()
	{
		return this.name;
	}
	public int getHealth()
	{
		return this.health;
	}
	public int getxPos()
	{
		return this.xPos;
	}
	public int getzPos()
	{
		return this.zPos;
	}
	
	public void setHealth(int h)
	{
		this.health = h;
	}
	public void setxPos(int x)
	{
		this.xPos = x;
	}
	public void setzPos(int z)
	{
		this.zPos = z;
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
			String s= Settings.radarColor+name+"["+xPos+","+zPos+"]"+getHealth(health);
			g.drawSideGradientRect(animPosition-2, 10+index*15-3, animPosition+155, 10+index*15+10, 0x00000000, 0xFF000000);
			f.drawStringWithShadow(s, animPosition,10+index*15, 16777215);
		}
		else
		{
			String s=getHealth(health)+ Settings.radarColor+name+"["+xPos+","+zPos+"]";
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
			String s = Settings.radarColor+name+" ["+distance+"]"+getHealth(health);
			g.drawSideGradientRect(animPosition-2, 10+index*15-3, animPosition+125, 10+index*15+10, 0x00000000, 0xFF000000);
			f.drawStringWithShadow(s, animPosition,10+index*15, 16777215);
		}
		else
		{
			String s = getHealth(health)+Settings.radarColor+name+" ["+distance+"]";
			g.drawSideGradientRect(screen.getScaledWidth()-120-animPosition-2, 10+index*15-3, screen.getScaledWidth()-animPosition+125, 10+index*15+10, 0xFF000000, 0x00000000);
			f.drawStringWithShadow(s, screen.getScaledWidth()-f.getStringWidth(s)+f.getStringWidth(getHealth(health))/2-2-animPosition,10+index*15, 16777215);
			}
	}
	public String getDisplayName()
	{
		return name;
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

		int distance = (int)Math.sqrt((p.posX - (double)xPos)*(p.posX - (double)xPos)+(p.posZ - (double) zPos)*(p.posZ -(double) zPos));

		return distance;
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
