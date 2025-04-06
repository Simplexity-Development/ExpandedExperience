package simplexity.expandedexperience;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PersistentDataUtil {
    private static final Logger logger = ExpandedExperience.getInstance().getLogger();


    private static void saveMapToPdc(PersistentDataContainer pdc, NamespacedKey key, Map<Material, Integer> map) {
        StringBuilder mapStringBuilder = new StringBuilder();
        for (Material material : map.keySet()) {
            if (material == null) continue;
            String materialName = material.toString();
            mapStringBuilder.append(materialName)
                    .append("=")
                    .append(map.get(material))
                    .append(";");
        }
        pdc.set(key, PersistentDataType.STRING, mapStringBuilder.toString());
    }

    public static Map<Material, Integer> loadMapFromPdc(PersistentDataContainer pdc, NamespacedKey key) {
        String pdcData = pdc.getOrDefault(key, PersistentDataType.STRING, "");
        Map<Material, Integer> materialMap = new HashMap<>();
        if (pdcData.isEmpty() || pdcData.isBlank()) return materialMap;
        logger.info("pdc Data: " + pdcData);
        String[] mapEntries = pdcData.split(";");
        logger.info("Map Entries: ");
        for (String entry : mapEntries) {
            logger.info(entry);
            String[] entryParts = entry.split("=", 2);
            if (entryParts.length != 2) {
                logger.warning("Issue deserializing information from PDC: Parts not equal to 2");
                continue;
            }
            logger.info("Material: " + entryParts[0]);
            Material material = Material.getMaterial(entryParts[0]);
            if (material == null) {
                logger.warning("Issue deserializing information from PDC: Material is null");
                continue;
            }
            logger.info("Times smelted: " + entryParts[1]);
            int timesSmelted;
            try {
                timesSmelted = Integer.parseInt(entryParts[1]);
            } catch (NumberFormatException e) {
                logger.warning("Issue deserializing information from PDC: Times Smelted is null, Material: " + material);
                continue;
            }
            materialMap.put(material, timesSmelted);
        }
        return materialMap;
    }

    public static void incrementIngredientUseCount(PersistentDataContainer pdc, NamespacedKey key, Material ingredient) {
        Map<Material, Integer> map = loadMapFromPdc(pdc, key);
        Integer currentCount = 0;
        if (map.containsKey(ingredient)) currentCount = map.get(ingredient);
        currentCount++;
        map.put(ingredient, currentCount);
        saveMapToPdc(pdc, key, map);
    }

    public static void removeMapFromPdc(PersistentDataContainer pdc, NamespacedKey key) {
        pdc.remove(key);
    }
}
