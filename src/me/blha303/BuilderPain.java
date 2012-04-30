package me.blha303;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
public class BuilderPain extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	public static BuilderPain plugin;
    public static Economy econ = null;
    public static Permission perms = null;
    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			log.severe(String.format("[%s] Disabled. Vault is missing!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
        getServer().getPluginManager().registerEvents(new myListener(), this);
		saveConfig();
		setupPermissions();
        log.info(String.format("[%s] Enabled version %s", getDescription().getName(), getDescription().getVersion()));
	}

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
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
    		if (Chance >= 0) {
        		if (!contains(getConfig().getString("nopain"), bl)) {
        			p.damage(getConfig().getInt("damage"));
        		}
    		}
    	}
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}