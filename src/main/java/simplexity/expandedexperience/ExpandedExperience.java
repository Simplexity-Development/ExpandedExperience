package simplexity.expandedexperience;

import org.bukkit.plugin.java.JavaPlugin;
import simplexity.expandedexperience.configs.ConfigHandler;

public final class ExpandedExperience extends JavaPlugin {

    private static ExpandedExperience instance;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        ConfigHandler.getInstance().reloadConfigValues();
        this.getServer().getPluginManager().registerEvents(new BrewingListener(), this);
    }

    public static ExpandedExperience getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
