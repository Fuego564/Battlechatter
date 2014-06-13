package com.fuego.Battlechatter.Main;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import com.fuego.Battlechatter.Gui.GuiOverlay;


import com.fuego.Battlechatter.Util.FriendsList;
import com.fuego.Battlechatter.Util.Settings;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = Battlechatter.MODID, version = Battlechatter.VERSION)
public class Battlechatter
{
    public static final String MODID = "battlechatter";
    public static final String VERSION = "1.0";
    
    @Instance(MODID)
    public static Battlechatter instance;
    private static GuiOverlay renderer;
    private Settings settings;
    private FriendsList friendsList;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	settings = new Settings();
    	friendsList = new FriendsList();
    	
    	renderer = new  GuiOverlay(Minecraft.getMinecraft());
    	
    	MinecraftForge.EVENT_BUS.register(renderer);
    	
    	FMLCommonHandler.instance().bus().register(renderer);
   
    }
    public static GuiOverlay getOverlay()
    {
    	return renderer;
    }

}