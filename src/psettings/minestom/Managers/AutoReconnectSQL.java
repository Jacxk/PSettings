package psettings.minestom.Managers;

import org.bukkit.scheduler.BukkitRunnable;
import psettings.minestom.PSettings;

public class AutoReconnectSQL extends BukkitRunnable {

    private PSettings plugin;

    public AutoReconnectSQL(PSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.mysqlSetup();
    }
}
