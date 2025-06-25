package simplexity.expandedexperience.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import simplexity.expandedexperience.configs.ConfigHandler;
import simplexity.expandedexperience.util.Constants;

@SuppressWarnings("UnstableApiUsage")
public class ExpReload {
    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("exp-reload")
                .requires(css -> css.getSender().hasPermission(Constants.RELOAD))
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    ConfigHandler.getInstance().reloadConfigValues();
                    sender.sendRichMessage("Expanded Experience was reloaded");
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
