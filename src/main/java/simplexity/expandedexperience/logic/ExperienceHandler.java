package simplexity.expandedexperience.logic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BrewingStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplexity.expandedexperience.ExpandedExperience;
import simplexity.expandedexperience.configs.ConfigHandler;

import java.util.Map;
import java.util.logging.Logger;

public class ExperienceHandler {

    private static ExperienceHandler instance;

    public static final NamespacedKey INGREDIENTS_USED = new NamespacedKey(ExpandedExperience.getInstance(), "ingredients-used");
    public static final NamespacedKey LEFTOVER_BLOCK_EXP = new NamespacedKey(ExpandedExperience.getInstance(), "leftover-exp");
    public static final NamespacedKey PLAYER_EXP = new NamespacedKey(ExpandedExperience.getInstance(), "player-xp");


    public ExperienceHandler() {
    }

    public static ExperienceHandler getInstance() {
        if (instance == null) instance = new ExperienceHandler();
        return instance;
    }

    public void addIngredientInformationToBrewer(BrewingStand brewingStand, Material ingredient) {
        PersistentDataContainer standPdc = brewingStand.getPersistentDataContainer();
        if (!ConfigHandler.getInstance().getBrewingMaterialMap().containsKey(ingredient)) return;
        PersistentDataUtil.incrementIngredientUseCount(standPdc, INGREDIENTS_USED, ingredient);
    }

    /**
     * Handles the spawning of stored experience on a brewing stand. Brewing stand experience is stored as 'ingredients used'
     * and when broken or an item is removed, the configured amounts are multiplied by how many times that ingredient was used
     *
     * @param brewingStand BrewingStand block
     */

    public void summonExpForBrewer(BrewingStand brewingStand) {
        if (!ConfigHandler.getInstance().isBrewingXpEnabled()) return;
        PersistentDataContainer brewingPdc = brewingStand.getPersistentDataContainer();
        Map<Material, Double> xpValues = ConfigHandler.getInstance().getBrewingMaterialMap();
        Map<Material, Integer> storedInfo = PersistentDataUtil.loadMapFromPdc(brewingPdc, INGREDIENTS_USED);
        double calculatedXp = brewingPdc.getOrDefault(LEFTOVER_BLOCK_EXP, PersistentDataType.DOUBLE, 0.0);
        for (Material material : storedInfo.keySet()) {
            Integer timesUsed = storedInfo.get(material);
            double xpForMaterial = timesUsed * xpValues.get(material);
            calculatedXp += xpForMaterial;
        }
        int xpToSpawn = (int) calculatedXp;
        if (xpToSpawn < 1) {
            brewingPdc.set(LEFTOVER_BLOCK_EXP, PersistentDataType.DOUBLE, calculatedXp);
            return;
        }
        calculatedXp -= xpToSpawn;
        brewingPdc.set(LEFTOVER_BLOCK_EXP, PersistentDataType.DOUBLE, calculatedXp);
        spawnXpOrb(brewingStand.getLocation(), xpToSpawn);
        PersistentDataUtil.removeMapFromPdc(brewingStand.getPersistentDataContainer(), INGREDIENTS_USED);
    }

    /**
     * Handles storage and spawning of experience orbs. Adds new xp to the stored amount, and leaves the overflow.
     * Input is a Player, or an entity to store the xp on if a player is not available.
     *
     * @param pdc           Persistent Data Container
     * @param xpToAdd       Amount of xp to add to the stored xp
     * @param locationForXp Location that experience orbs should be spawned if the amount is over 1
     */
    public void handleXp(PersistentDataContainer pdc, Double xpToAdd, Location locationForXp) {
        Double currentXpLevel = pdc.getOrDefault(PLAYER_EXP, PersistentDataType.DOUBLE, 0.0);
        double combined = currentXpLevel + xpToAdd;
        if (combined < 1) {
            pdc.set(PLAYER_EXP, PersistentDataType.DOUBLE, combined);
            return;
        }
        int xpToSpawn = (int) combined;
        double leftover = combined - xpToSpawn;
        pdc.set(PLAYER_EXP, PersistentDataType.DOUBLE, leftover);
        spawnXpOrb(locationForXp, xpToSpawn);
    }

    public int getFortuneXp(int originalXp, ItemStack itemUsed){
        Logger logger = ExpandedExperience.getInstance().getLogger();
        if (!ConfigHandler.getInstance().isFortuneBoostEnabled()) return originalXp;
        Map<Enchantment, Integer> enchants = itemUsed.getEnchantments();
        logger.info(enchants.keySet().toString());
        if (enchants.isEmpty() || !enchants.containsKey(Enchantment.FORTUNE)) return originalXp;
        int level = enchants.get(Enchantment.FORTUNE);
        Double multiplier = ConfigHandler.getInstance().getFortuneBoostXpMap().get(level);
        if (multiplier == null) return originalXp;
        double newXp = multiplier * originalXp;
        return (int) newXp;
    }

    private void spawnXpOrb(Location location, int xpValue) {
        ExperienceOrb expOrb = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
        expOrb.setExperience(xpValue);
    }
}
