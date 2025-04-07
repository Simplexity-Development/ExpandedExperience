package simplexity.expandedexperience;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionBrewer;
import simplexity.expandedexperience.configs.ConfigHandler;

import java.util.logging.Logger;

@SuppressWarnings("UnstableApiUsage")
public class BrewingListener implements Listener {

    Logger logger = ExpandedExperience.getInstance().getLogger();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBrew(BrewEvent brewEvent) {
        Block block = brewEvent.getBlock();
        if (!(block.getState(false) instanceof BrewingStand brewingStand)) {
            logger.info("Block isn't a brewing stand: " + block.getBlockData());
            return;
        }
        ItemStack ingredientItem = brewEvent.getContents().getIngredient();
        if (ingredientItem == null) {
            logger.info("No ingredient");
            return;
        }
        Material ingredient = ingredientItem.getType();
        if (!ConfigHandler.getInstance().getMaterialMap().containsKey(ingredient)) {
            logger.info("configured map does not contain ingredient");
            return;
        }
        ExperienceHandler.getInstance().addIngredientInformationToBrewer(brewingStand, ingredient);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTakePotion(InventoryClickEvent inventoryClickEvent) {
        Inventory clickedInv = inventoryClickEvent.getClickedInventory();
        InventoryType.SlotType slotType = inventoryClickEvent.getSlotType();
        ItemStack itemMoved = inventoryClickEvent.getCurrentItem();
        logger.info("-----start of inv click check----");
        logger.info("Click:" + inventoryClickEvent.getClick());
        if (itemMoved == null || itemMoved.getType().isAir()) {
            itemMoved = inventoryClickEvent.getCursor();
        }
        if (!(clickedInv instanceof BrewerInventory)) {
            logger.info("Not a brewer inventory");
            return;
        }
        if (slotType.equals(InventoryType.SlotType.FUEL)) {
            logger.info("clicked fuel");
            return;
        }
        if (ConfigHandler.getInstance().getMaterialMap().containsKey(itemMoved.getType())) {
            logger.info("Item is an ingredient");
            return;
        }
        Location invLocation = clickedInv.getLocation();
        if (invLocation == null ) {
            logger.info("Location is null");
            return;
        }
        if (!itemMoved.hasData(DataComponentTypes.POTION_CONTENTS)) {
            logger.info("not a potion");
            return;
        }

        if (!(invLocation.getBlock().getState(false) instanceof BrewingStand brewingStand)) {
            logger.info("Block is not a brewing stand");
            return;
        }
        logger.info("-----end of inv click check----");
        ExperienceHandler.getInstance().summonExpForBrewer(brewingStand);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBrewerBreak(BlockBreakEvent blockBreakEvent) {
        Block brokenBlock = blockBreakEvent.getBlock();
        if (!(brokenBlock.getState(false) instanceof BrewingStand brewingStand)) return;
        ExperienceHandler.getInstance().summonExpForBrewer(brewingStand);
    }
}
