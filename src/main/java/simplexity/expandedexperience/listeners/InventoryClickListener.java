package simplexity.expandedexperience.listeners;

import org.bukkit.Location;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import simplexity.expandedexperience.logic.ExperienceHandler;

public class InventoryClickListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTakePotion(InventoryClickEvent inventoryClickEvent) {
        Inventory clickedInv = inventoryClickEvent.getClickedInventory();
        InventoryType.SlotType slotType = inventoryClickEvent.getSlotType();
        if (!(clickedInv instanceof BrewerInventory)) return;
        if (slotType.equals(InventoryType.SlotType.FUEL)) return;
        Location invLocation = clickedInv.getLocation();
        if (invLocation == null) return;
        if (!(invLocation.getBlock().getState(false) instanceof BrewingStand brewingStand)) return;
        ExperienceHandler.getInstance().summonExpForBrewer(brewingStand);
    }
}
