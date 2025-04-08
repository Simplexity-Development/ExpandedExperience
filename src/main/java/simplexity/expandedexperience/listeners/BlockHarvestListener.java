package simplexity.expandedexperience.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import simplexity.expandedexperience.configs.ConfigHandler;
import simplexity.expandedexperience.logic.FarmingHandler;

import java.util.ArrayList;

public class BlockHarvestListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockHarvest(PlayerHarvestBlockEvent harvestEvent) {
        if (!ConfigHandler.getInstance().isFarmingXpEnabled()) return;
        ArrayList<Material> harvestedMaterials = new ArrayList<>();
        for (ItemStack item : harvestEvent.getItemsHarvested()) {
            if (item == null) continue;
            Material itemMaterial = item.getType();
            if (ConfigHandler.getInstance().getFarmingMaterialMap().containsKey(itemMaterial)) {
                harvestedMaterials.add(itemMaterial);
            }
        }
        if (harvestedMaterials.isEmpty()) return;
        FarmingHandler.handleFarmingMaterials(harvestedMaterials, harvestEvent.getPlayer(), harvestEvent.getHarvestedBlock().getLocation());
    }
}
