package me.virustotal.localuuidcache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.Tag;

public class UUIDApi {

	private static HashMap<UUID, String> ids = new HashMap<UUID, String>();

	protected static void loadUUIDS(final File[] files)
	{
		ids = new HashMap<UUID, String>();
		for(File file : files)
		{
			if(isUUID(file.getName()))
			{
				try {
					FileInputStream fStream = new FileInputStream(file);
					NBTInputStream stream = new NBTInputStream(fStream);
					Tag playerData = stream.readTag();
					stream.close();
					fStream.close();
					final CompoundTag playerDataCompound = (CompoundTag)playerData;
					if(playerDataCompound.getValue().get("bukkit") != null)
					{
						final CompoundTag bukkit = (CompoundTag) playerDataCompound.getValue().get("bukkit");
						if(bukkit.getValue().get("lastKnownName") != null) 
						{
							final Tag name = bukkit.getValue().get("lastKnownName");
							final String lastKnownName = (String) name.getValue();
							final UUID uuid = UUID.fromString(file.getName().substring(0, file.getName().indexOf(".")));
							ids.put(uuid, lastKnownName);
						}
					}
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
		LocalUUIDCache.getInstance().logger.log(Level.INFO, ids.values().size() + " uuids loaded");
	}

	protected static boolean isUUID(String name)
	{
		if(name.length() >= 8)
		{
			if(name.contains("-"))
			{
				if(name.indexOf("-") == 8)
				{
					return true;
				}
			}
		}
		return false;
	}		


	protected static void addUUID(final Player player)
	{
		ids.put(player.getUniqueId(), player.getName());
	}

	protected static void updateName(final Player player)
	{
		UUID uuid = player.getUniqueId();
		ids.remove(uuid);
		ids.put(uuid, player.getName());
	}

	protected static void clearData()
	{
		ids = null;
	}

	/*Gets the UUID from a player's name
	 */
	public static UUID uuidFromName(final String name)
	{

		Collection<String> names = ids.values();
		Set<UUID> uuids = ids.keySet();
		UUID[] uuidArray = uuids.toArray(new UUID[uuids.size()]);
		String[] nameArray = names.toArray(new String[names.size()]);
		for(int i = 0; i < nameArray.length; i++)
		{
			if(nameArray[i].equalsIgnoreCase(name))
				return uuidArray[i];
		}

		return null;
	}

	/*Returns a player's name from a String uuid
	 * 
	 */
	public static String nameFromUUID(final String uuid)
	{
		return UUIDApi.nameFromUUID(UUID.fromString(uuid));
	}

	/*Returns a player's name from a UUID
	 * 
	 */
	public static String nameFromUUID(final UUID uuid)
	{
		return ids.get(uuid);
	}

	/*Checks to see if a UUID is already cached
	 * 
	 */
	public static boolean containsUUID(final UUID uuid)
	{
		return ids.containsKey(uuid);
	}

}
