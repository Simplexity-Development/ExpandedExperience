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

    /**
     * Calculates the new xp value for fortune xp boost. This will return the original value if fortune is not present
     * on the used item. If fortune is present and the config has fortune boost disabled, and the fortune level has not
     * been configured (i.e. fortune 10 or something) the multiplier will be calculated by (level * 0.5) + 1
     *
     * @param originalXp Original xp value that was set to drop in the event
     * @param itemUsed Item used to break the block
     * @return new xp value to spawn
     */
    public int getFortuneXp(int originalXp, ItemStack itemUsed){
        if (!ConfigHandler.getInstance().isFortuneBoostEnabled()) return originalXp;
        Map<Enchantment, Integer> enchants = itemUsed.getEnchantments();
        if (enchants.isEmpty() || !enchants.containsKey(Enchantment.FORTUNE)) return originalXp;
        int level = enchants.get(Enchantment.FORTUNE);
        Double multiplier = ConfigHandler.getInstance().getFortuneBoostXpMap().get(level);
        if (multiplier == null) multiplier = (level * 0.5) + 1;
        double newXp = multiplier * originalXp;
        return (int) newXp;
    }

    /**
     * Calculates the new xp value for looting xp boost. This will return the original value if looting is not present
     * on the used item. If looting is present and the config has looting boost disabled, and the looting level has not
     * been configured (i.e. looting 10 or something) the multiplier will be calculated by (level * 0.5) + 1
     *
     * @param originalXp Original xp value that was set to drop in the event
     * @param itemUsed Item used to break the block
     * @return new xp value to spawn
     */

    public int getLootingXp(int originalXp, ItemStack itemUsed){
        if (!ConfigHandler.getInstance().isLootingBoostEnabled()) return originalXp;
        Map<Enchantment, Integer> enchants = itemUsed.getEnchantments();
        if (enchants.isEmpty() || !enchants.containsKey(Enchantment.LOOTING)) return originalXp;
        int level = enchants.get(Enchantment.LOOTING);
        Double multiplier = ConfigHandler.getInstance().getLootingBoostXpMap().get(level);
        if (multiplier == null) multiplier = (level * 0.5) + 1;
        double newXp = multiplier * originalXp;
        return (int) newXp;
    }

    private void spawnXpOrb(Location location, int xpValue) {
        ExperienceOrb expOrb = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
        expOrb.setExperience(xpValue);
    }
}
