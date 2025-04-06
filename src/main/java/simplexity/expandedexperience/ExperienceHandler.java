package simplexity.expandedexperience;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplexity.expandedexperience.configs.ConfigHandler;

import java.util.Map;
import java.util.logging.Logger;

public class ExperienceHandler {

    private static ExperienceHandler instance;

    private final Logger logger = ExpandedExperience.getInstance().getLogger();
    public static final NamespacedKey expKey = new NamespacedKey(ExpandedExperience.getInstance(), "ingredients-used");
    public static final NamespacedKey leftoverExp = new NamespacedKey(ExpandedExperience.getInstance(), "leftover-exp");

    public ExperienceHandler() {
    }

    public static ExperienceHandler getInstance() {
        if (instance == null) instance = new ExperienceHandler();
        return instance;
    }

    public void addIngredientInformationToBrewer(BrewingStand brewingStand, Material ingredient) {
        PersistentDataContainer standPdc = brewingStand.getPersistentDataContainer();
        logger.info("pdc: " + standPdc.getKeys());
        if (!ConfigHandler.getInstance().getMaterialMap().containsKey(ingredient)) return;
        PersistentDataUtil.incrementIngredientUseCount(standPdc, expKey, ingredient);
    }

    public void summonExpForBrewer(BrewingStand brewingStand) {
        PersistentDataContainer brewingPdc = brewingStand.getPersistentDataContainer();
        Map<Material, Double> xpValues = ConfigHandler.getInstance().getMaterialMap();
        Map<Material, Integer> storedInfo = PersistentDataUtil.loadMapFromPdc(brewingPdc, expKey);
        double calculatedXp = brewingPdc.getOrDefault(leftoverExp, PersistentDataType.DOUBLE, 0.0);
        for (Material material : storedInfo.keySet()) {
            Integer timesUsed = storedInfo.get(material);
            double xpForMaterial = timesUsed * xpValues.get(material);
            calculatedXp = calculatedXp + xpForMaterial;
        }
        Double overflowExp = calculatedXp % 1.0;
        int xpToSpawn = (int) calculatedXp;
        brewingPdc.set(leftoverExp, PersistentDataType.DOUBLE, overflowExp);
        Location location = brewingStand.getLocation();
        ExperienceOrb expOrb = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
        expOrb.setExperience(xpToSpawn);
        logger.info("spawning orb equal to " + xpToSpawn + " experience, " + overflowExp + " experience left over");
        PersistentDataUtil.removeMapFromPdc(brewingStand.getPersistentDataContainer(), expKey);
    }
}
