package com.fuego.Battlechatter.Gui.IRC;

import com.fuego.Battlechatter.Util.Settings;
import com.fuego.Battlechatter.Util.BattleAwareness.BattleAwareness;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.User;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


/**
 * 
 * Replaces GuiChat.java
 *
 */
@SideOnly(Side.CLIENT)
public class IrcChat extends GuiScreen
{

	private boolean isIrc=false;
	public static BotMain ircMain;
	public static IrcBot ircBot;
	private static Thread thread;

	private static final Logger logger = LogManager.getLogger();
	private String field_146410_g = "";
	/**
	 * keeps position of which chat message you will select when you press up, (does not increase for duplicated
	 * messages sent immediately after each other)
	 */
	private int sentHistoryCursor = -1;
	private boolean field_146417_i;
	private boolean field_146414_r;
	private int field_146413_s;
	private List field_146412_t = new ArrayList();
	/** used to pass around the URI to various dialogues and to the host os */
	private URI clickedURI;
	/** Chat entry field */
	protected GuiTextField inputField;
	/**
	 * is the text that appears when you press the chat key and the input box appears pre-filled
	 */
	private String defaultInputFieldText = "";
	private static final String __OBFID = "CL_00000682";

	public IrcChat() {}

	public IrcChat(String par1Str)
	{
		this.defaultInputFieldText = par1Str;
	}

	public  IrcChat(boolean isIrc)
	{
		this.isIrc=isIrc;
	}
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
		this.inputField = new GuiTextField(this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
		this.inputField.setMaxStringLength(100);
		this.inputField.setEnableBackgroundDrawing(false);
		this.inputField.setFocused(true);
		this.inputField.setText(this.defaultInputFieldText);
		this.inputField.setCanLoseFocus(false);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
		this.mc.ingameGUI.getChatGUI().resetScroll();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		this.inputField.updateCursorCounter();
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2)
	{
		this.field_146414_r = false;

		if (par2 == 15)
		{
			this.func_146404_p_();//complete playername
		}
		else
		{
			this.field_146417_i = false;
		}

		if (par2 == 1)
		{
			this.mc.displayGuiScreen((GuiScreen)null);
		}
		else if (par2 != 28 && par2 != 156)
		{
			if (par2 == 200)
			{
				this.getSentHistory(-1);
			}
			else if (par2 == 208)
			{
				this.getSentHistory(1);
			}
			else if (par2 == 201)
			{
				this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().func_146232_i() - 1);
			}
			else if (par2 == 209)
			{
				this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().func_146232_i() + 1);
			}
			else
			{
				this.inputField.textboxKeyTyped(par1, par2);
			}
		}
		else//if key == enter
		{
			String s = this.inputField.getText().trim();//the text in the inputField

			if(isIrc)
			{
				if(s.startsWith("/"))
				{
					if(s.startsWith("/help"))
					{
						this.addChatMessage("@----------------Help Menu----------------@");
						this.addChatMessage("/join [<server name> <channel>] - Joins IRC ");
						this.addChatMessage("/leave - Disconnects from IRC");
						this.addChatMessage("/channel #<channel name>- Moves to specified channel");
						this.addChatMessage("/me <action>- Performs an emote");
					}
					else if(s.startsWith("/join"))
					{

						String[] s1 = s.split(" ");
						String server;
						String channel;

						if(s1.length==1)
						{
							server = Settings.ircServer;
							channel = Settings.ircChannel;
						}
						else if(s1.length==3)
						{
							server = s1[1];
							channel = s1[2];
						}
						else
						{
							return;
						}
						if(ircMain==null)
						{
							ircMain = new BotMain(server, formatChannel(channel) ,mc.thePlayer.getDisplayName(), mc);
							this.ircBot = ircMain.getIrcBot();
							thread = new Thread(ircMain);
							thread.start();
							this.addChatMessage("Connecting To IRC...");
							mc.displayGuiScreen(null);
						}
						else if(!server.equalsIgnoreCase(ircBot.getServer())&& !channel.equalsIgnoreCase(ircBot.getChannels()[0]))
						{
							try {
								ircBot.connect(server);
								ircBot.joinChannel(channel);
								BattleAwareness.clear();
							} catch (NickAlreadyInUseException e) {
								e.printStackTrace();
							} catch (IOException e) {		
								e.printStackTrace();
							} catch (IrcException e) {
								e.printStackTrace();
							}

						}
						else
						{
							this.addChatMessage("Already connected to this IRC!");
						}


					}
					else if(s.startsWith("/leave"))
					{
						if(ircBot!=null)
						{
							ircBot.disconnect();
							try {
								thread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							ircBot.dispose();
							ircMain = null;
							ircBot = null;
							BattleAwareness.clear();
							this.addChatMessage("Leaving IRC...");
						}
						else
						{
							this.addChatMessage("Not connected to an IRC!");
						}

					}
					else if(s.startsWith("/channel"))
					{
						if(ircBot!=null  && ircMain.connected()&& ircBot.getChannels().length>0)
						{
							String[] s1 = s.split(" ");
							String channel;

							if(s1.length==2)
							{
								channel = s1[1];
								ircBot.joinChannel(channel);
								BattleAwareness.clear();
							}
							else
							{
								this.addChatMessage("No channel specified!");
							}
						}
						else
						{
							this.addChatMessage("Not connected to an IRC!");
						}
					}
					else if(s.startsWith("/me"))
					{
						if(ircBot!=null  && ircMain.connected()&& ircBot.getChannels().length>0)
						{
							//ircBot.sendMessage(ircBot.getChannels()[0], s);
							String[] s1 = s.split(" ",2);
							String action;

							if(s1.length==2)
							{
								action = s1[1];
								ircBot.sendAction(ircBot.getChannels()[0], action);
								ircBot.onAction(mc.thePlayer.getDisplayName(), "", "", "", action);
							}
							else
							{
								this.addChatMessage("No action given!");
							}
						}
						else
						{
							this.addChatMessage("You must connect to a server first!");
						}
					}
				}
				else// if is sending message
				{

					if(ircBot != null && ircMain.connected() && ircBot.getChannels().length>0)
					{
						if (s.length() > 0)
						{
							ircBot.sendMessage(ircBot.getChannels()[0], s);
							ircBot.onMessage(ircBot.getChannels()[0], mc.thePlayer.getDisplayName(), "", "", s);
						}
					}
					else
					{
						this.addChatMessage("You must connect to a server first!");
					}
				}
				mc.displayGuiScreen(null);
			}
			else
			{
				if (s.length() > 0)
				{
					this.func_146403_a(s);
				}
			}
			this.mc.displayGuiScreen((GuiScreen)null);
		}
	}

	public void func_146403_a(String p_146403_1_)
	{
		this.mc.ingameGUI.getChatGUI().addToSentMessages(p_146403_1_);
		if (ClientCommandHandler.instance.executeCommand(mc.thePlayer, p_146403_1_) == 1) return;
		this.mc.thePlayer.sendChatMessage(p_146403_1_);
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput()
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0)
		{
			if (i > 1)
			{
				i = 1;
			}

			if (i < -1)
			{
				i = -1;
			}

			if (!isShiftKeyDown())
			{
				i *= 7;
			}

			this.mc.ingameGUI.getChatGUI().scroll(i);
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3)
	{
		if (par3 == 0 && this.mc.gameSettings.chatLinks)
		{
			IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().func_146236_a(Mouse.getX(), Mouse.getY());

			if (ichatcomponent != null)
			{
				ClickEvent clickevent = ichatcomponent.getChatStyle().getChatClickEvent();

				if (clickevent != null)
				{
					if (isShiftKeyDown())
					{
						this.inputField.writeText(ichatcomponent.getUnformattedTextForChat());
					}
					else
					{
						URI uri;

						if (clickevent.getAction() == ClickEvent.Action.OPEN_URL)
						{
							try
							{
								uri = new URI(clickevent.getValue());

								if (this.mc.gameSettings.chatLinksPrompt)
								{
									this.clickedURI = uri;
									this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 0, false));
								}
								else
								{
									this.func_146407_a(uri);
								}
							}
							catch (URISyntaxException urisyntaxexception)
							{
								logger.error("Can\'t open url for " + clickevent, urisyntaxexception);
							}
						}
						else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE)
						{
							uri = (new File(clickevent.getValue())).toURI();
							this.func_146407_a(uri);
						}
						else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND)
						{
							this.inputField.setText(clickevent.getValue());
						}
						else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
						{
							this.func_146403_a(clickevent.getValue());
						}
						else
						{
							logger.error("Don\'t know how to handle " + clickevent);
						}
					}

					return;
				}
			}
		}

		this.inputField.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}

	public void confirmClicked(boolean par1, int par2)
	{
		if (par2 == 0)
		{
			if (par1)
			{
				this.func_146407_a(this.clickedURI);
			}

			this.clickedURI = null;
			this.mc.displayGuiScreen(this);
		}
	}

	private void func_146407_a(URI p_146407_1_)
	{
		try
		{
			Class oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
			oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {p_146407_1_});
		}
		catch (Throwable throwable)
		{
			logger.error("Couldn\'t open link", throwable);
		}
	}

	public void func_146404_p_()
	{
		String s1;

		if (this.field_146417_i)
		{
			this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());

			if (this.field_146413_s >= this.field_146412_t.size())
			{
				this.field_146413_s = 0;
			}
		}
		else
		{
			int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
			this.field_146412_t.clear();
			this.field_146413_s = 0;
			String s = this.inputField.getText().substring(i).toLowerCase();
			s1 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
			this.func_146405_a(s1, s);

			if (this.field_146412_t.isEmpty())
			{
				return;
			}

			this.field_146417_i = true;
			this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
		}

		if (this.field_146412_t.size() > 1)
		{
			StringBuilder stringbuilder = new StringBuilder();

			for (Iterator iterator = this.field_146412_t.iterator(); iterator.hasNext(); stringbuilder.append(s1))
			{
				s1 = (String)iterator.next();

				if (stringbuilder.length() > 0)
				{
					stringbuilder.append(", ");
				}
			}

			this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
		}

		this.inputField.writeText(EnumChatFormatting.getTextWithoutFormattingCodes((String)this.field_146412_t.get(this.field_146413_s++)));
	}

	private void func_146405_a(String p_146405_1_, String p_146405_2_)
	{
		if (p_146405_1_.length() >= 1)
		{
			ClientCommandHandler.instance.autoComplete(p_146405_1_, p_146405_2_);
			this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_));
			this.field_146414_r = true;
		}
	}

	/**
	 * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
	 * message from the current cursor position
	 */
	public void getSentHistory(int p_146402_1_)
	{
		int j = this.sentHistoryCursor + p_146402_1_;
		int k = this.mc.ingameGUI.getChatGUI().getSentMessages().size();

		if (j < 0)
		{
			j = 0;
		}

		if (j > k)
		{
			j = k;
		}

		if (j != this.sentHistoryCursor)
		{
			if (j == k)
			{
				this.sentHistoryCursor = k;
				this.inputField.setText(this.field_146410_g);
			}
			else
			{
				if (this.sentHistoryCursor == k)
				{
					this.field_146410_g = this.inputField.getText();
				}

				this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(j));
				this.sentHistoryCursor = j;
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3)
	{
		drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		this.inputField.drawTextBox();


		if(isIrc)
		{
			if(ircBot != null && ircMain.connected()&& ircBot.getChannels().length>0)
			{

				String s1 ="Server: "+ircBot.getServer();
				String s2 ="Channel: "+ircBot.getChannels()[0];

				drawRect(2, this.height-25, fontRendererObj.getStringWidth(s1)+10+fontRendererObj.getStringWidth(s2)+2,this.height-15 ,0x99000000);
				fontRendererObj.drawStringWithShadow(s1, 5, this.height-24, 0xFFFFFFFF);
				fontRendererObj.drawStringWithShadow(s2, fontRendererObj.getStringWidth(s1)+10, this.height-24, 0xFFFFFFFF);

				User[] users = ircBot.getUsers(ircBot.getChannels()[0]);
				for(int i =0; i < users.length;i++)
				{
					fontRendererObj.drawStringWithShadow(users[i].getNick(), 330, this.height-24-14*(i+1), 0xFFFFFFFF);
				}
			}
			else
			{
				String s1 = "Server: N/A";
				String s2 ="Channel: N/A";

				drawRect(3, this.height-26, fontRendererObj.getStringWidth(s1)+10+fontRendererObj.getStringWidth(s2)+2,this.height-15 ,0x99000000);
				fontRendererObj.drawStringWithShadow(s1, 5, this.height-24, 0xFFFFFFFF);
				fontRendererObj.drawStringWithShadow(s2, fontRendererObj.getStringWidth(s1)+10, this.height-24, 0xFFFFFFFF);
			}
		}











		IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().func_146236_a(Mouse.getX(), Mouse.getY());

		if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null)
		{
			HoverEvent hoverevent = ichatcomponent.getChatStyle().getChatHoverEvent();

			if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM)
			{
				ItemStack itemstack = null;

				try
				{
					NBTBase nbtbase = JsonToNBT.func_150315_a(hoverevent.getValue().getUnformattedText());

					if (nbtbase != null && nbtbase instanceof NBTTagCompound)
					{
						itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbtbase);
					}
				}
				catch (NBTException nbtexception)
				{
					;
				}

				if (itemstack != null)
				{
					this.renderToolTip(itemstack, par1, par2);
				}
				else
				{
					this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", par1, par2);
				}
			}
			else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT)
			{
				this.drawCreativeTabHoveringText(hoverevent.getValue().getFormattedText(), par1, par2);
			}
			else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT)
			{
				StatBase statbase = StatList.func_151177_a(hoverevent.getValue().getUnformattedText());

				if (statbase != null)
				{
					IChatComponent ichatcomponent1 = statbase.func_150951_e();
					ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic"), new Object[0]);
					chatcomponenttranslation.getChatStyle().setItalic(Boolean.valueOf(true));
					String s = statbase instanceof Achievement ? ((Achievement)statbase).getDescription() : null;
					ArrayList arraylist = Lists.newArrayList(new String[] {ichatcomponent1.getFormattedText(), chatcomponenttranslation.getFormattedText()});

					if (s != null)
					{
						arraylist.addAll(this.fontRendererObj.listFormattedStringToWidth(s, 150));
					}

					this.func_146283_a(arraylist, par1, par2);
				}
				else
				{
					this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", par1, par2);
				}
			}

			GL11.glDisable(GL11.GL_LIGHTING);
		}

		super.drawScreen(par1, par2, par3);
	}

	public void func_146406_a(String[] p_146406_1_)
	{
		if (this.field_146414_r)
		{
			this.field_146417_i = false;
			this.field_146412_t.clear();
			String[] astring1 = p_146406_1_;
			int i = p_146406_1_.length;

			String[] complete = ClientCommandHandler.instance.latestAutoComplete;
			if (complete != null)
			{
				astring1 = ObjectArrays.concat(complete, astring1, String.class);
				i = astring1.length;
			}

			for (int j = 0; j < i; ++j)
			{
				String s = astring1[j];

				if (s.length() > 0)
				{
					this.field_146412_t.add(s);
				}
			}

			if (this.field_146412_t.size() > 0)
			{
				this.field_146417_i = true;
				this.func_146404_p_();
			}
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	public void addChatMessage(String s)
	{
		mc.thePlayer.addChatMessage(new ChatComponentText(s));
	}

	private String formatChannel(String s)
	{
		String result = s;

		if(!s.startsWith("#"))
		{
			return "#"+result;
		}

		return result;
	}
}