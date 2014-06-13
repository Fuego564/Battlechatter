package com.fuego.Battlechatter.Gui.NotificationManager;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import com.fuego.Battlechatter.Gui.GuiOverlay;
import com.fuego.Battlechatter.Util.Settings;

public class Notification 
{
	private String s;
	private long creationTime = System.currentTimeMillis();
	private long animationTime = 250;
	
	private long duration=3000;
	private long fadeTime=2500;
	private long startPosition = 0;
	private long endPosition=15;
	private long distance = endPosition-startPosition;
	private int lastIndex=0;
	private int testnum =0;
	private int testnum2 = 0;
	public Notification (String s)
	{
		this.s = s;
	}
	public String getText()
	{
		return s;
	}
	public long getCreationTime()
	{
		return creationTime;
	}

	public void draw(ScaledResolution screen,GuiOverlay g, FontRenderer f,int index) 
	{
		int animPosition = getAnimatedPosition();
		g.drawBorderedRect(screen.getScaledWidth()/2-100,(index*15)+animPosition -4, screen.getScaledWidth()/2+100, index*15+animPosition+10, 1, 0x99808080, 0x99000000);
		f.drawStringWithShadow(Settings.radarColor+s, screen.getScaledWidth()/2-f.getStringWidth(s)/2, index*15+animPosition, 0x99FFFFFF);
	}
	public boolean hasAged()
	{
		return creationTime+fadeTime<System.currentTimeMillis();
	}
	public int getAnimatedPosition()
	{
		int result = (int)endPosition;



		if(!isDoneAnimating())//start position +percentage of time completed
		{
			long time =  System.currentTimeMillis();

			float test = distance*(time - creationTime)/animationTime;

			result = (int)(test+startPosition);
		}
		else if(hasAged())
		{

				long time =  System.currentTimeMillis();

				float test = distance*(time -(creationTime+fadeTime))/animationTime;

				result = (int)(endPosition-test);
		}


		return result;	
	}
	public boolean isDoneAnimating()
	{
		return System.currentTimeMillis() > creationTime+animationTime;
	}
	public boolean hasDied()
	{
		return creationTime+duration<System.currentTimeMillis()+animationTime;
	}
}
