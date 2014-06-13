package com.fuego.Battlechatter.Gui.Elements;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.fuego.Battlechatter.Gui.GuiMenuScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class GuiTextbox extends GuiElement
{
	private boolean isSelected=false;
	private String text="";
	private long lastUpdated = System.currentTimeMillis();
	private final long drawTime= 500;
	private boolean isDrawn=false;
	private final char[] allowedCharacters = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9','_','.'};
	private String backgroundText="";
	private int maxLength=16;

	public GuiTextbox(int xPos, int yPos, int width, int height,int maxLength, GuiMenuScreen g,String backgroundText) {
		super(xPos, yPos, width, height, g);
		this.backgroundText = backgroundText;
		this.maxLength = maxLength;
	
	}
	public GuiTextbox(int xPos, int yPos, int width, int height,int maxLength, GuiMenuScreen g,String backgroundText,String text) {
		super(xPos, yPos, width, height, g);
		this.backgroundText = backgroundText;
		this.text = text;
		this.maxLength = maxLength;
		
	}
	@Override
	public void draw(ScaledResolution screen) {
		g.drawBorderedRect(super.xPos, super.yPos,super.xPos+super.width, super.height, 2, 0x80FFFFFF,0x99202020);
		
		
		if(text.length()>0 || isSelected)
			g.drawString(g.getFontRenderer(), getTrimmedText()+drawMarker(), super.xPos+3, super.yPos+4, 0x99FFFFFF);
		else
			g.drawString(g.getFontRenderer(), backgroundText, super.xPos+3, super.yPos+4, 0x99707070);
	}

	@Override
	public boolean handleMouseInput(int mouseX, int mouseY, int mouseButton,
			int direction) {
		if(super.contains(mouseX, mouseY))//if within
		{
			if(direction ==0)//if down
			{
				isSelected=true;
				lastUpdated = System.currentTimeMillis();
			}
		}
		else
		{
			isSelected=false;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char par1, int par2) {

		if(isSelected)
		{
			if(par2==14)
			{
				if(text.length()>0)
				{
					text = text.substring(0, text.length()-1);
				}
			}
			else if(text.length()<maxLength)
			{
				if(contains(par1))
				{
					text += String.valueOf(par1);
				}
			}
		}

		return false;
	}

	@Override
	public void init() {


	}
	private String drawMarker()
	{
		if(!isSelected)
		{
			return "";
		}
		if(lastUpdated+drawTime<System.currentTimeMillis())
		{//Change the state
			isDrawn = !isDrawn;
			lastUpdated = System.currentTimeMillis();
		}
		//draw the current state
		if(isDrawn)
		{
			return "|";
		}
		else
		{
			return "";
		}
	}
	private boolean contains(char par1)
	{
		for(int i =0; i < allowedCharacters.length;i++)
		{
			if(par1 == allowedCharacters[i])
			{
				return true;
			}
		}
		return false;
	}
	public void deleteText()
	{
		this.text = "";
	}
	public String getText()
	{
		return this.text;
	}
	public void setText(String s)
	{
		this.text=s;
	}
	private String getTrimmedText()
	{
		String result="";
		if(text.length()>21)
		{
			for(int i =text.length()-1;i>=text.length()-21;i--)
				result= text.charAt(i)+result;
		}
		else
		{
			return text;
		}
		return result;
	}
}
