package simplexity.expandedexperience.configs;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import simplexity.expandedexperience.ExpandedExperience;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class ConfigHandler {

    private static ConfigHandler instance;
    private final Logger logger = ExpandedExperience.getInstance().getLogger();

    private final HashMap<Material, Double> brewingXpMap = new HashMap<>();
    private final HashMap<Material, Double> farmingXpMap = new HashMap<>();
    private final HashSet<Material> validFarmingTools = new HashSet<>();
    private final HashMap<EntityType, Double> shearingXpMap = new HashMap<>();
    private final HashMap<Material, Double> archeologyXpMap = new HashMap<>();
    private final HashMap<Integer, Double> fortuneBoostXpMap = new HashMap<>();
    private final HashMap<Integer, Double> lootingBoostXpMap = new HashMap<>();
    private Double barterXp = 0.0;
    private boolean brewingXpEnabled, farmingXpEnabled, farmingXpRequiresTool,
            shearingXpEnabled, archeologyXpEnabled, miscXpEnabled, fortuneBoostEnabled,
            lootingBoostEnabled;

    public ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    public void reloadConfigValues() {
        ExpandedExperience.getInstance().reloadConfig();
        FileConfiguration config = ExpandedExperience.getInstance().getConfig();
        brewingXpEnabled = config.getBoolean("brewing.xp-enabled", true);
        farmingXpEnabled = config.getBoolean("farming.xp-enabled", true);
        farmingXpRequiresTool = config.getBoolean("farming.require-tool", true);
        shearingXpEnabled = config.getBoolean("shearing.xp-enabled", true);
        archeologyXpEnabled = config.getBoolean("archeology.xp-enabled", true);
        miscXpEnabled = config.getBoolean("misc.xp-enabled", true);
        fortuneBoostEnabled = config.getBoolean("fortune-boost.enabled", true);
        lootingBoostEnabled = config.getBoolean("looting-boost.enabled", true);
        barterXp = config.getDouble("misc.xp-amounts.piglin-barter", 0.1);
        ConfigurationSection brewingXpSection = config.getConfigurationSection("brewing.xp-amounts");
        ConfigurationSection farmingXpSection = config.getConfigurationSection("farming.xp-amounts");
        List<String> toolStringList = config.getStringList("farming.valid-tools");
        ConfigurationSection shearingXpSection = config.getConfigurationSection("shearing.xp-amounts");
        ConfigurationSection archeologyXpSection = config.getConfigurationSection("archeology.xp-amounts");
        ConfigurationSection fortuneBoostSection = config.getConfigurationSection("fortune-boost.level-multipliers");
        ConfigurationSection lootingBoostSection = config.getConfigurationSection("looting-boost.level-multipliers");
        validateMaterialMap(brewingXpSection, brewingXpMap);
        validateMaterialMap(farmingXpSection, farmingXpMap);
        validateMaterialMap(archeologyXpSection, archeologyXpMap);
        validateMaterialSet(toolStringList, validFarmingTools);
        validateEntityMap(shearingXpSection, shearingXpMap);
        validateIntegerDoubleMap(fortuneBoostSection, fortuneBoostXpMap);
        validateIntegerDoubleMap(lootingBoostSection, lootingBoostXpMap);
    }


    private void validateMaterialMap(ConfigurationSection config, Map<Material, Double> mapToFill) {
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

    private void validateIntegerDoubleMap(ConfigurationSection config, HashMap<Integer, Double> mapToFill) {
        mapToFill.clear();
        if (config == null) {
            logger.warning("Problem reloading xp configurations, please check that your configurations are valid and that you did not use TAB instead of SPACE");
            return;
        }
        Set<String> mapKeys = config.getKeys(false);

        for (String key : mapKeys) {
            try {
                Integer level = Integer.parseInt(key);
                Double multiplier = config.getDouble(key, 1.0);
                mapToFill.put(level, multiplier);
            } catch (NumberFormatException e) {
                logger.warning("Issue trying to get configuration from " + config + " please check your syntax");
            }
        }
    }

    private void validateEntityMap(ConfigurationSection config, Map<EntityType, Double> mapToFill) {
        mapToFill.clear();
        if (config == null) {
            logger.warning("Problem reloading xp configurations, please check that your configurations are valid and that you did not use TAB instead of SPACE");
            return;
        }

        Set<String> mapKeys = config.getKeys(false);

        for (String key : mapKeys) {
            try {
                EntityType entity = EntityType.valueOf(key.toUpperCase());
                mapToFill.put(entity, config.getDouble(key));
            } catch (IllegalArgumentException e) {
                logger.warning(key + " is not a valid entity, please check your configuration and provide a valid material");
            }
        }
    }

    private void validateMaterialSet(List<String> stringList, HashSet<Material> setToFill) {
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

    public HashMap<Material, Double> getBrewingMaterialMap() {
        return brewingXpMap;
    }

    public HashMap<Material, Double> getFarmingMaterialMap() {
        return farmingXpMap;
    }

    public HashMap<EntityType, Double> getShearingMap() {
        return shearingXpMap;
    }

    public HashMap<Material, Double> getArcheologyMap() {
        return archeologyXpMap;
    }

    public HashMap<Integer, Double> getFortuneBoostXpMap() {
        return fortuneBoostXpMap;
    }

    public HashMap<Integer, Double> getLootingBoostXpMap() {
        return lootingBoostXpMap;
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

    public Double getBarterXp() {
        return barterXp;
    }

    public boolean isShearingXpEnabled() {
        return shearingXpEnabled;
    }

    public boolean isArcheologyXpEnabled() {
        return archeologyXpEnabled;
    }

    public boolean isMiscXpEnabled() {
        return miscXpEnabled;
    }

    public boolean isFortuneBoostEnabled() {
        return fortuneBoostEnabled;
    }

    public boolean isLootingBoostEnabled() {
        return lootingBoostEnabled;
    }
}
