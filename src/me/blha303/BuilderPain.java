package me.blha303;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;
public class BuilderPain extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	public static BuilderPain plugin;
	public static Permission permission = null;
    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

	@Override
	public void onEnable() {
		if (!setupPermissions()) {
			log.severe(String.format("[%s] Disabled. Vault is missing!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
        getServer().getPluginManager().registerEvents(new myListener(), this);
		saveConfig();
		setupPermissions();
        log.info(String.format("[%s] Enabled version %s", getDescription().getName(), getDescription().getVersion()));
	}
    
    // http://stackoverflow.com/a/2275030
    public boolean contains(String haystack, String needle) {
    	  haystack = haystack == null ? "" : haystack;
    	  needle = needle == null ? "" : needle;
    	  return haystack.toLowerCase().contains(needle.toLowerCase());
    }
    
    public class myListener implements Listener{
    	
    	@EventHandler
    	public void blockPlace(BlockPlaceEvent event){
    		Player p = event.getPlayer();
    		String bl = event.getBlockPlaced().getType().toString().toLowerCase();
    		Random random = new Random();
    		int Chance = random.nextInt(100);
    		if (Chance >= getConfig().getInt("chanceofhurt")) {
        		if (!contains(getConfig().getString("nopain"), bl)) {
        			if (!permission.has(p, "builderpain.deny")) {
            			p.damage(getConfig().getInt("damage"));
            			p.sendMessage(getConfig().getString("dmgmessage"));
        			}

        		}
    		}
    	}
    }
    
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}