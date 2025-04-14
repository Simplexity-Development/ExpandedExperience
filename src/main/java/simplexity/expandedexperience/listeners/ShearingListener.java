package simplexity.expandedexperience.listeners;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import simplexity.expandedexperience.configs.ConfigHandler;
import simplexity.expandedexperience.logic.ExperienceHandler;

public class ShearingListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onShear(PlayerShearEntityEvent shearEvent){
        if (!ConfigHandler.getInstance().isShearingXpEnabled()) return;
        EntityType shearedEntity = shearEvent.getEntity().getType();
        if (!ConfigHandler.getInstance().getShearingMap().containsKey(shearedEntity)) return;
        Double xpToAdd = ConfigHandler.getInstance().getShearingMap().get(shearedEntity);
        Location entityLocation = shearEvent.getEntity().getLocation();
        Player player = shearEvent.getPlayer();
        ExperienceHandler.getInstance().handleXp(player.getPersistentDataContainer(), xpToAdd, entityLocation);
    }
}
