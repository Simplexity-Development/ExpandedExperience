package simplexity.expandedexperience.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import simplexity.expandedexperience.configs.ConfigHandler;

public class ExpReload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        ConfigHandler.getInstance().reloadConfigValues();
        sender.sendMessage("Expanded Experience Config Reloaded");
        return false;
    }
}
