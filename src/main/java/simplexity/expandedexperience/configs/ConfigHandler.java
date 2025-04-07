package simplexity.expandedexperience.configs;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import simplexity.expandedexperience.ExpandedExperience;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ConfigHandler {

    private static ConfigHandler instance;
    private final Logger logger = ExpandedExperience.getInstance().getLogger();

    private final HashMap<Material, Double> brewingXpMap = new HashMap<>();
    private final HashMap<Material, Double> farmingXpMap = new HashMap<>();
    private final HashSet<Material> validFarmingTools = new HashSet<>();
    private boolean brewingXpEnabled, farmingXpEnabled, farmingXpRequiresTool;

    public ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    public void reloadConfigValues(){
        ExpandedExperience.getInstance().reloadConfig();
        FileConfiguration config = ExpandedExperience.getInstance().getConfig();
        brewingXpEnabled = config.getBoolean("brewing.xp-enabled", true);
        farmingXpEnabled = config.getBoolean("farming.xp-enabled", true);
        farmingXpRequiresTool = config.getBoolean("farming.require-tool", true);
        ConfigurationSection brewingXpSection = config.getConfigurationSection("brewing.xp-amounts");
        ConfigurationSection farmingXpSection = config.getConfigurationSection("farming.xp-amounts");
        List<String> toolStringList = config.getStringList("farming.valid-tools");
        validateMap(brewingXpSection, brewingXpMap);
        validateMap(farmingXpSection, farmingXpMap);
        validateMaterialSet(toolStringList, validFarmingTools);
    }


    private void validateMap(ConfigurationSection config, Map<Material, Double> mapToFill) {
        mapToFill.clear();
        if (config == null) {
            logger.warning("Problem reloading xp configurations, please check that your configurations are valid and that you did not use TAB instead of SPACE");
            return;
        }
        Set<String> mapKeys = config.getKeys(false);

        for (String key : mapKeys) {
            Material material = Material.getMaterial(key.toUpperCase());
            if (material == null) {
                logger.warning(key + " is not a valid material, please check your configuration and provide a valid material");
                continue;
            }
            Double xpAmount = config.getDouble(key);
            mapToFill.put(material, xpAmount);
        }
    }

    private void validateMaterialSet(List<String> stringList, HashSet<Material> setToFill){
        if (!setToFill.isEmpty()) setToFill.clear();
        for (String key : stringList) {
            Material material = Material.getMaterial(key.toUpperCase());
            if (material == null) {
                logger.warning(key + " is not a valid material, please check your configuration and provide a valid material");
                continue;
            }
            setToFill.add(material);
        }
    }

    public HashMap<Material, Double> getBrewingMaterialMap(){
        return brewingXpMap;
    }

    public HashMap<Material, Double> getFarmingMaterialMap(){
        return farmingXpMap;
    }


    public boolean isBrewingXpEnabled() {
        return brewingXpEnabled;
    }

    public boolean isFarmingXpEnabled() {
        return farmingXpEnabled;
    }

    public boolean isFarmingXpRequiresTool() {
        return farmingXpRequiresTool;
    }

    public HashSet<Material> getValidFarmingTools() {
        return validFarmingTools;
    }
}
