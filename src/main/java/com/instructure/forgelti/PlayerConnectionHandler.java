package com.instructure.forgelti;

import com.instructure.minecraftlti.User;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class PlayerConnectionHandler {
	private ForgeLTI plugin;
	public PlayerConnectionHandler(ForgeLTI plugin) {
		this.plugin = plugin;
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
	    User user = User.byUuid(event.player.getUniqueID());
	    if (user == null) {return;}
	    user.setStartDate();
	    user.save();
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedOutEvent event) {
	    User user = User.byUuid(event.player.getUniqueID());
	    if (user == null) {return;}
	    user.handleQuit();
	}
}
