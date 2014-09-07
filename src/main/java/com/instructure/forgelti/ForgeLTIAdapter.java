package com.instructure.forgelti;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.instructure.minecraftlti.Assignment;
import com.instructure.minecraftlti.MinecraftLTIAdapter;

import cpw.mods.fml.relauncher.FMLInjectionData;

public class ForgeLTIAdapter implements MinecraftLTIAdapter {
	private ForgeLTI plugin;
    public static final File minecraftDir = (File) FMLInjectionData.data()[6];
	
	public ForgeLTIAdapter(ForgeLTI plugin) {
		this.plugin = plugin;
	}

	@Override
	public Path getStorageDirectory() {
		Path privatePath = Paths.get(minecraftDir.toString(), "mods", "ForgeLTI");
	    privatePath.toFile().mkdir();
		return privatePath;
	}
	
	@Override
	public Logger getLogger() {
		return plugin.logger;
	}

	@Override
	public String getPlayerTp(UUID uuid) {
		EntityPlayerMP p = plugin.getPlayer(uuid);
	    return String.format("/tp %.0f %.0f %.0f", p.posX, p.posY, p.posZ);
	}

	@Override
	public String getServerAddress() {
		MinecraftServer server = MinecraftServer.getServer();
		String address = server.getHostname();
	    int port = server.getPort();
	    if (port != 25565) {
	        address += ":"+port;
	      }
	      return address;
	}

	@Override
	public boolean isPlayerPresent(UUID uuid) {
	    return plugin.getPlayer(uuid) != null;
	}

	@Override
	public void sendPlayerError(UUID uuid, String msg) {
		plugin.sendPlayerError(plugin.getPlayer(uuid), msg);
	}

	@Override
	public void sendPlayerMessage(UUID uuid, String msg) {
		plugin.sendPlayerMessage(plugin.getPlayer(uuid), msg);
	}

	@Override
	public void setAssignmentLocation(UUID uuid, Assignment a) {
		EntityPlayerMP p = plugin.getPlayer(uuid);
		String worldName = p.worldObj.getWorldInfo().getWorldName();
		a.setLocation(worldName, p.posX, p.posY, p.posZ, p.cameraPitch, p.cameraYaw);

	}

	public World getWorld(String name) {
		for (World w: MinecraftServer.getServer().worldServers) {
			if (w.getWorldInfo().getWorldName() == name) {
				return w;
			}
		}
		return null;
	}
	
	@Override
	public void teleportPlayer(UUID uuid, Assignment a) {
		EntityPlayerMP p = plugin.getPlayer(uuid);
		// p.setPositionAndUpdate(a.getX(), a.getY(), a.getZ());
		p.setPositionAndRotation(a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch());
	}
}
