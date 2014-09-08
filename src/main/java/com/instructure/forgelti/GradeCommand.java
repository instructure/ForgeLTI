package com.instructure.forgelti;

import java.util.UUID;

import com.instructure.minecraftlti.Assignment;
import com.instructure.minecraftlti.User;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

public class GradeCommand extends CommandBase {
	private ForgeLTI plugin;
	public GradeCommand(ForgeLTI plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getCommandName() {
		return "grade";
	}
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/grade [player] [grade]";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return !(sender instanceof EntityPlayerMP);
	}
	
	@Override
	public void processCommand(ICommandSender icommandsender, String[] args) {
		if (args.length != 2) {
			plugin.logger.warning("Grade command: player name and grade required");
			return;
		}
		String playerName = args[0];
		EntityPlayerMP player = null;
		if (playerName.equals("@p")) {
			ChunkCoordinates coords = icommandsender.getPlayerCoordinates();
			player = getClosestPlayer(coords);
		} else {
			player = getPlayer(playerName);
		}
		
		User user = User.byUuid(player.getUniqueID());
	    if (user == null) {
	        plugin.logger.warning("Graded player not associated with an LTI user.");
	    	return;
	    }
	    
	    user.grade(args[1]);
	}

	private EntityPlayerMP getClosestPlayer(ChunkCoordinates coords) {
		EntityPlayerMP result = null;
		double bestDistance = Double.MAX_VALUE;
    	for (EntityPlayerMP player : plugin.getPlayers()) {
    		double distance = player.getDistanceSq(coords.posX, coords.posY, coords.posZ);
    		if (distance < bestDistance) {
    			result = player;
    			bestDistance = distance;
    		}
        }
        return result;
	}
	
    
    public EntityPlayerMP getPlayer(String name) {
    	for (EntityPlayerMP player : plugin.getPlayers()) {
            if (name.equals(player.getGameProfile().getName())) {
                return player;
            }
        }
        return null;
    }
}