package simplexity.expandedexperience.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import simplexity.expandedexperience.logic.ExperienceHandler;

public class EntityDeathListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDeath(EntityDeathEvent deathEvent) {
        LivingEntity entity = deathEvent.getEntity();
        Player player = entity.getKiller();
        if (player == null) return;
        int droppingXp = deathEvent.getDroppedExp();
        if (droppingXp == 0) return;
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon.isEmpty()) return;
        double newXp = ExperienceHandler.getInstance().getLootingXp(droppingXp, weapon);
        if (newXp == (double) droppingXp) return;
        deathEvent.setDroppedExp((int) newXp);
    }


}
