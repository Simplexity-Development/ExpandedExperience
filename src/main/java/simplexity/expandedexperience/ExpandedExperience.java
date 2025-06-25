package simplexity.expandedexperience;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.expandedexperience.commands.ExpReload;
import simplexity.expandedexperience.configs.ConfigHandler;
import simplexity.expandedexperience.listeners.BarterListener;
import simplexity.expandedexperience.listeners.BlockBreakListener;
import simplexity.expandedexperience.listeners.BlockDropItemListener;
import simplexity.expandedexperience.listeners.BlockHarvestListener;
import simplexity.expandedexperience.listeners.BrewingListener;
import simplexity.expandedexperience.listeners.EntityDeathListener;
import simplexity.expandedexperience.listeners.InventoryClickListener;
import simplexity.expandedexperience.listeners.ShearingListener;
import simplexity.expandedexperience.util.Constants;

@SuppressWarnings("UnstableApiUsage")
public final class ExpandedExperience extends JavaPlugin {

    private static ExpandedExperience instance;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConfigHandler.getInstance().reloadConfigValues();
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(ExpReload.createCommand());
        });
        getServer().getPluginManager().addPermission(Constants.RELOAD);
        loadListeners();
    }

    private void loadListeners() {
        this.getServer().getPluginManager().registerEvents(new BarterListener(), this);
        this.getServer().getPluginManager().registerEvents(new BrewingListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockDropItemListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockHarvestListener(), this);
        this.getServer().getPluginManager().registerEvents(new ShearingListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
    }

    public static ExpandedExperience getInstance() {
        return instance;
    }

}
