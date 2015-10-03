package me.virustotal.localuuidcache;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LocalUUIDCache extends JavaPlugin {
	
	private File playerDataFolder;
	private static LocalUUIDCache plugin;
	public Logger logger;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		logger = this.getLogger();
		this.playerDataFolder = new File(Bukkit.getWorlds().get(0).getWorldFolder().getPath(),"playerdata");
		Bukkit.getPluginManager().registerEvents(new LoginListener(), this);
		UUIDApi.loadUUIDS(this.playerDataFolder.listFiles());
	}
	
	@Override
	public void onDisable()
	{
		UUIDApi.clearData();
	}
	
	protected static LocalUUIDCache getInstance()
	{
		return plugin;
	}

}
