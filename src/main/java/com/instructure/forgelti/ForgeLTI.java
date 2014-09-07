package com.instructure.forgelti;

import java.security.Permission;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.instructure.minecraftlti.MinecraftLTI;

import net.minecraft.command.CommandHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ForgeLTI.MODID, name = ForgeLTI.MODNAME, version = ForgeLTI.VERSION, acceptableRemoteVersions = "*")
public class ForgeLTI
{
    public static final String MODID = "ForgeLTI";
    public static final String MODNAME = MODID;
    public static final String VERSION = "0.4.0";
    
    private ForgeLTIAdapter adapter;
    private MinecraftLTI lti;
    public static SimpleNetworkWrapper network;
    public static final Logger logger = Logger.getLogger(ForgeLTI.MODID);
    
    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
    	adapter = new ForgeLTIAdapter(this);
    	lti = new MinecraftLTI(adapter);
    	
        CommandHandler ch = (CommandHandler) MinecraftServer.getServer().getCommandManager();
	    ch.registerCommand(new RegisterCommand(this));
	    ch.registerCommand(new AssignmentCommand(this));
	    ch.registerCommand(new GradeCommand(this));
	    
	    MinecraftForge.EVENT_BUS.register(new PlayerConnectionHandler(this));
	    
	    // TODO: listen to books
	    // https://github.com/Bukkit/CraftBukkit/blob/master/src/main/java/net/minecraft/server/PlayerConnection.java#L1656
	    // http://cazzar.net/minecraft/tutorials/Forge-SimpleImpl-Tutorial/
    }
    
    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
    	lti.close();
    }
	
    public List<EntityPlayerMP> getPlayers() {
    	return MinecraftServer.getServer().getConfigurationManager().playerEntityList;
    }
    
    public EntityPlayerMP getPlayer(UUID uuid) {
    	for (EntityPlayerMP player : getPlayers()) {
            if (uuid.equals(player.getUniqueID())) {
                return player;
            }
        }
        return null;
    }
    
    public void sendPlayerMessage(EntityPlayerMP player, String msg) {
    	player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + msg));
    }
       
    public void sendPlayerError(EntityPlayerMP player, String msg) {
    	player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + msg));
    }
    
    private class MySecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            return;
        }
    }
}
