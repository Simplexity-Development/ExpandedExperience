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
import org.bukkit.event.inventory.InventoryMoveItemEvent;
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
    public void onBrew(BrewEvent brewEvent){
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
        if (!ConfigHandler.getInstance().getMaterialMap().containsKey(ingredient)){
            logger.info("configured map does not contain ingredient");
            return;
        }
        ExperienceHandler.getInstance().addIngredientInformationToBrewer(brewingStand, ingredient);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTakePotion(InventoryMoveItemEvent moveItemEvent){
        Inventory invFrom = moveItemEvent.getSource();
        Inventory invTo = moveItemEvent.getDestination();
        ItemStack itemMoved = moveItemEvent.getItem();
        Location invLocation = invFrom.getLocation();
        if (!(invFrom instanceof BrewerInventory)) return;
        if (ConfigHandler.getInstance().getMaterialMap().containsKey(itemMoved.getType())) return;
        if (!(invTo instanceof PlayerInventory)) return;
        if (invLocation == null) return;
        if (!(invLocation.getBlock().getState() instanceof BrewingStand brewingStand)) return;
        ExperienceHandler.getInstance().summonExpForBrewer(brewingStand);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBrewerBreak(BlockBreakEvent blockBreakEvent){
        Block brokenBlock = blockBreakEvent.getBlock();
        if (!(brokenBlock.getState(false) instanceof BrewingStand brewingStand)) return;
        ExperienceHandler.getInstance().summonExpForBrewer(brewingStand);
    }
}
