package simplexity.expandedexperience.listeners;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import simplexity.expandedexperience.ExpandedExperience;
import simplexity.expandedexperience.logic.ExperienceHandler;

import java.util.logging.Logger;

@SuppressWarnings("UnstableApiUsage")
public class EntityDeathListener implements Listener {
/*
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDeath(EntityDeathEvent deathEvent) {
        DamageSource source = deathEvent.getDamageSource();
        Entity causingEntity = source.getCausingEntity();
        Entity directEntity = source.getDirectEntity();
        Logger logger = ExpandedExperience.getInstance().getLogger();
        if (causingEntity == null && directEntity == null) {
            logger.info("causing entity and direct entity null, returning");
            return;
        }
        Player player = null;
        if (causingEntity instanceof Player) {
            player = (Player) causingEntity;
        }
        if (directEntity instanceof Player) {
            player = (Player) directEntity;
        }
        if (player == null) {
            logger.info("player is still null, returning");
            return;
        }
        int droppingXp = deathEvent.getDroppedExp();
        if (droppingXp == 0) {
            logger.info("dropping xp is 0, returning");
            return;
        }
        ItemStack itemUsed = player.getInventory().getItemInMainHand();
        if (itemUsed.isEmpty()){
            logger.info("No item in main hand, returning");
            return;
        }
        int newXp = ExperienceHandler.getInstance().getLootingXp(droppingXp, itemUsed);
        if (newXp == droppingXp) {
            logger.info("new xp and dropped xp are the same, returning");
            return;
        }
        deathEvent.setDroppedExp(newXp);
    }

 */
}
