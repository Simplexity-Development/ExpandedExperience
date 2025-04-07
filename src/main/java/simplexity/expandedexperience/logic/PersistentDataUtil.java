package simplexity.expandedexperience.logic;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplexity.expandedexperience.ExpandedExperience;

import java.util.Arrays;
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
        String[] mapEntries = pdcData.split(";");
        for (String entry : mapEntries) {
            String[] entryParts = entry.split("=", 2);
            if (entryParts.length != 2) {
                logger.warning("Issue deserializing information from PDC: Parts not equal to 2.");
                logger.warning("'entry' String: " + entry);
                logger.warning("parts: " + Arrays.toString(entryParts));
                continue;
            }
            Material material = Material.getMaterial(entryParts[0]);
            if (material == null) {
                logger.warning("Issue deserializing information from PDC: Material is null");
                logger.warning("'entry' String: " + entry);
                logger.warning("Material string: " + entryParts[0]);
                continue;
            }
            int timesSmelted;
            try {
                timesSmelted = Integer.parseInt(entryParts[1]);
            } catch (NumberFormatException e) {
                logger.warning("Issue deserializing information from PDC: Times Smelted is null, Material: " + material);
                logger.warning("'entry' String: " + entry);
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
