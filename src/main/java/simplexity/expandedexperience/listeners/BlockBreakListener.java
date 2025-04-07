package simplexity.expandedexperience.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import simplexity.expandedexperience.ExperienceHandler;
import simplexity.expandedexperience.configs.ConfigHandler;

public class BlockBreakListener implements Listener {


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBrewerBreak(BlockBreakEvent blockBreakEvent) {
        Block brokenBlock = blockBreakEvent.getBlock();
        if (ConfigHandler.getInstance().getFarmingMaterialMap().containsKey(brokenBlock.getType())) {
            ExperienceHandler.getInstance().handlePlayerHarvestXp(blockBreakEvent.getPlayer(), brokenBlock.getType(), brokenBlock.getLocation());
            return;
        }
        if (!(brokenBlock.getState(false) instanceof BrewingStand brewingStand)) return;
        ExperienceHandler.getInstance().summonExpForBrewer(brewingStand);
    }
}
