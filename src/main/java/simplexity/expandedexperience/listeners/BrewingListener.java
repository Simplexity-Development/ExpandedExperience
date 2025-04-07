package simplexity.expandedexperience.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import simplexity.expandedexperience.ExpandedExperience;
import simplexity.expandedexperience.ExperienceHandler;
import simplexity.expandedexperience.configs.ConfigHandler;

import java.util.logging.Logger;

public class BrewingListener implements Listener {

    Logger logger = ExpandedExperience.getInstance().getLogger();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBrew(BrewEvent brewEvent) {
        Block block = brewEvent.getBlock();
        ItemStack ingredientItem = brewEvent.getContents().getIngredient();
        if (!(block.getState(false) instanceof BrewingStand brewingStand)) return;
        if (ingredientItem == null) return;
        Material ingredient = ingredientItem.getType();
        if (!ConfigHandler.getInstance().getBrewingMaterialMap().containsKey(ingredient)) return;
        ExperienceHandler.getInstance().addIngredientInformationToBrewer(brewingStand, ingredient);
    }


}
