package simplexity.expandedexperience;

import org.bukkit.plugin.java.JavaPlugin;
import simplexity.expandedexperience.commands.ExpReload;
import simplexity.expandedexperience.configs.ConfigHandler;
import simplexity.expandedexperience.listeners.BarterListener;
import simplexity.expandedexperience.listeners.BlockBreakListener;
import simplexity.expandedexperience.listeners.BlockDropItemListener;
import simplexity.expandedexperience.listeners.BlockHarvestListener;
import simplexity.expandedexperience.listeners.BrewingListener;
import simplexity.expandedexperience.listeners.InventoryClickListener;
import simplexity.expandedexperience.listeners.ShearingListener;

public final class ExpandedExperience extends JavaPlugin {

    private static ExpandedExperience instance;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        ConfigHandler.getInstance().reloadConfigValues();
        this.getCommand("exp-reload").setExecutor(new ExpReload());
        loadListeners();
    }

    private void loadListeners(){
        this.getServer().getPluginManager().registerEvents(new BarterListener(), this);
        this.getServer().getPluginManager().registerEvents(new BrewingListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockDropItemListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockHarvestListener(), this);
        this.getServer().getPluginManager().registerEvents(new ShearingListener(), this);
    }

    public static ExpandedExperience getInstance() {
        return instance;
    }

}
