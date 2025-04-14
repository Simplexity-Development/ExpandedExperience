package simplexity.expandedexperience.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.persistence.PersistentDataContainer;
import simplexity.expandedexperience.configs.ConfigHandler;
import simplexity.expandedexperience.logic.ExperienceHandler;

public class BarterListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPiglinBarter(PiglinBarterEvent barterEvent){
        if (!ConfigHandler.getInstance().isMiscXpEnabled()) return;
        Double xpToAdd = ConfigHandler.getInstance().getBarterXp();
        PersistentDataContainer pdc = barterEvent.getEntity().getPersistentDataContainer();
        Location location = barterEvent.getEntity().getLocation();
        ExperienceHandler.getInstance().handleXp(pdc, xpToAdd, location);
    }

}
