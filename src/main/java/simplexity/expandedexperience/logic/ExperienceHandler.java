package simplexity.expandedexperience.logic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplexity.expandedexperience.ExpandedExperience;
import simplexity.expandedexperience.configs.ConfigHandler;

import java.util.Map;
import java.util.logging.Logger;

public class ExperienceHandler {

    private static ExperienceHandler instance;

    private final Logger logger = ExpandedExperience.getInstance().getLogger();
    public static final NamespacedKey expKey = new NamespacedKey(ExpandedExperience.getInstance(), "ingredients-used");
    public static final NamespacedKey leftoverExp = new NamespacedKey(ExpandedExperience.getInstance(), "leftover-exp");
    public static final NamespacedKey harvestExp = new NamespacedKey(ExpandedExperience.getInstance(), "harvest-xp");


    public ExperienceHandler() {
    }

    public static ExperienceHandler getInstance() {
        if (instance == null) instance = new ExperienceHandler();
        return instance;
    }

    public void addIngredientInformationToBrewer(BrewingStand brewingStand, Material ingredient) {
        PersistentDataContainer standPdc = brewingStand.getPersistentDataContainer();
        if (!ConfigHandler.getInstance().getBrewingMaterialMap().containsKey(ingredient)) return;
        PersistentDataUtil.incrementIngredientUseCount(standPdc, expKey, ingredient);
    }

    public void summonExpForBrewer(BrewingStand brewingStand) {
        PersistentDataContainer brewingPdc = brewingStand.getPersistentDataContainer();
        Map<Material, Double> xpValues = ConfigHandler.getInstance().getBrewingMaterialMap();
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
        if (xpToSpawn < 1) return;
        spawnXpOrb(brewingStand.getLocation(), xpToSpawn);
        PersistentDataUtil.removeMapFromPdc(brewingStand.getPersistentDataContainer(), expKey);
    }

    public void handlePlayerHarvestXp(Player player, Material material, Location brokenBlockLocation){
        PersistentDataContainer playerPdc = player.getPersistentDataContainer();
        Double currentXpLevel = playerPdc.getOrDefault(harvestExp, PersistentDataType.DOUBLE, 0.0);
        Double amountToAdd = ConfigHandler.getInstance().getFarmingMaterialMap().get(material);
        double combined = currentXpLevel + amountToAdd;
        if (combined < 1) {
            playerPdc.set(harvestExp, PersistentDataType.DOUBLE, combined);
            return;
        }
        double leftover = combined % 1.0;
        int xpToSpawn = (int) combined;
        playerPdc.set(harvestExp, PersistentDataType.DOUBLE, leftover);
        spawnXpOrb(brokenBlockLocation, xpToSpawn);
    }

    private void spawnXpOrb(Location location, int xpValue){
        ExperienceOrb expOrb = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
        expOrb.setExperience(xpValue);
    }
}
