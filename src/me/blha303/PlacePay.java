package me.blha303;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
public class PlacePay extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	public static PlacePay plugin;
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
    public class myListener implements Listener{
    	
    	@EventHandler
    	public void blockPlace(BlockPlaceEvent event){
    		Player p = event.getPlayer();
    		String pl = p.getName();
    		econ.withdrawPlayer(pl, getConfig().getDouble("placecost"));
    	}
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}