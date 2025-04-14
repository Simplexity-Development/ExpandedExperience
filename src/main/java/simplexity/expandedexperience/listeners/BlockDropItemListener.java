package simplexity.expandedexperience.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import simplexity.expandedexperience.configs.ConfigHandler;
import simplexity.expandedexperience.logic.ExperienceHandler;

public class BlockDropItemListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent blockEvent){
        if (!ConfigHandler.getInstance().isArcheologyXpEnabled()) return;
        Material blockType = blockEvent.getBlockState().getBlock().getType();
        if (!ConfigHandler.getInstance().getArcheologyMap().containsKey(blockType)) return;
        Double xpToAdd = ConfigHandler.getInstance().getArcheologyMap().get(blockType);
        Player player = blockEvent.getPlayer();
        Location location = blockEvent.getBlock().getLocation();
        ExperienceHandler.getInstance().handleXp(player.getPersistentDataContainer(), xpToAdd, location);
    }
}
