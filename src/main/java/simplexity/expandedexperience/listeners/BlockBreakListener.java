package simplexity.expandedexperience.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import simplexity.expandedexperience.logic.ExperienceHandler;
import simplexity.expandedexperience.configs.ConfigHandler;
import simplexity.expandedexperience.logic.FarmingHandler;

public class BlockBreakListener implements Listener {


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        Block brokenBlock = blockBreakEvent.getBlock();
        if (brokenBlock.getState(false) instanceof BrewingStand brewingStand) {
            if (!ConfigHandler.getInstance().isBrewingXpEnabled()) return;
            ExperienceHandler.getInstance().summonExpForBrewer(brewingStand);
            return;
        }
        if (ConfigHandler.getInstance().getFarmingMaterialMap().containsKey(brokenBlock.getType())) {
            if (!ConfigHandler.getInstance().isFarmingXpEnabled()) return;
            FarmingHandler.handleFarmingBlock(brokenBlock, blockBreakEvent.getPlayer());
        }
    }
}
