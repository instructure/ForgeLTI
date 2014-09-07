package com.instructure.forgelti;

import java.util.List;

import com.instructure.minecraftlti.User;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class AssignmentCommand extends CommandBase {
	private ForgeLTI plugin;
	public AssignmentCommand(ForgeLTI plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getCommandName() {
		return "assignment";
	}
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/assignment [begin|set|submit]";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender instanceof EntityPlayerMP;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		EntityPlayerMP player = (EntityPlayerMP)sender;

		if (args.length < 1) {
			plugin.sendPlayerError(player, "Format: /assignment [begin|submit|set]");
			return;
		}
		String effect = args[0];

		User user = User.byUuid(player.getUniqueID());
		if (user == null) {
			plugin.sendPlayerError(player, "You are not associated with an LTI user.");
			return;
		}

		try {
			user.assignmentAction(effect);
		} catch (IllegalStateException e) {
			plugin.sendPlayerError(player, e.getMessage());
		}
	}
}