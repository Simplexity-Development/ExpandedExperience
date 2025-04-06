package simplexity.expandedexperience.configs;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import simplexity.expandedexperience.ExpandedExperience;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ConfigHandler {

    private static ConfigHandler instance;
    private final Logger logger = ExpandedExperience.getInstance().getLogger();

    private final HashMap<Material, Double> brewingXpMap = new HashMap<>();

    public ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    public void reloadConfigValues(){
        ExpandedExperience.getInstance().reloadConfig();
        FileConfiguration config = ExpandedExperience.getInstance().getConfig();
        reloadBrewingValues(config);

    }

    public void reloadBrewingValues(FileConfiguration config){
        ConfigurationSection brewingSection = config.getConfigurationSection("brewing-xp-amounts");
        if (brewingSection == null) {
            logger.warning("Problem reloading brewing xp configuration, please make sure that the configuration is valid and that SPACE was used and not TAB");
            return;
        }

        brewingXpMap.clear();
        Set<String> keys = brewingSection.getKeys(false);

        for (String itemName : keys) {
            Material itemMaterial = Material.getMaterial(itemName.toUpperCase());
            if (itemMaterial == null) {
                logger.warning("Unable to load the item " + itemName + ", no material found by this name");
                continue;
            }
            Double xpAmount = brewingSection.getDouble(itemName);
            brewingXpMap.put(itemMaterial, xpAmount);
        }
    }

    public Map<Material, Double> getMaterialMap(){
        return brewingXpMap;
    }




}
