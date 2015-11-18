package me.virustotal.localuuidcache;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class LoginListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent e)
	{
		if(e.getResult() == Result.ALLOWED)
		{
			Player player = e.getPlayer();
			UUID uuid = player.getUniqueId();
			if(!UUIDApi.containsUUID(uuid))
			{
				UUIDApi.addUUID(player);
			}
			else if(!UUIDApi.nameFromUUID(uuid).equals(player.getName()))
			{
				UUIDApi.updateName(player);
			}
		}
	}
}