package com.instructure.forgelti;

import java.util.List;

import com.instructure.minecraftlti.User;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class RegisterCommand extends CommandBase {
	private ForgeLTI plugin;
	public RegisterCommand(ForgeLTI plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getCommandName() {
		return "register";
	}
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/register [token]";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer)sender;
		if (args.length != 1) {
			plugin.sendPlayerError(player, "Token required");
			return;
		}
		
	    User user = User.byToken(args[0]);
	    if (user == null) {
	      plugin.sendPlayerError(player, "Invalid token.");
	      return;
	    }
	    
	    user.register(player.getUniqueID());
	    plugin.sendPlayerMessage(player, "Registered.");
	}
}