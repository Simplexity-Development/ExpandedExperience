package simplexity.expandedexperience.logic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import simplexity.expandedexperience.configs.ConfigHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class FarmingHandler {


    private static final HashMap<Material, Material> melonsAndStems = new HashMap<>() {{
        put(Material.MELON, Material.ATTACHED_MELON_STEM);
        put(Material.PUMPKIN, Material.ATTACHED_PUMPKIN_STEM);
    }};

    private static final Set<BlockFace> facesToGet = Set.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);

    public static void handleFarmingBlock(@NotNull Block block, Player player) {
        Material blockMaterial = block.getType();
        if (ConfigHandler.getInstance().isFarmingXpRequiresTool() && !usingValidTool(player)) return;
        if (melonsAndStems.containsKey(blockMaterial) && !isAttachedToStem(block, melonsAndStems.get(blockMaterial)))
            return;
        ExperienceHandler.getInstance().handlePlayerHarvestXp(player, blockMaterial, block.getLocation());
    }

    public static void handleFarmingMaterials(List<Material> materialList, Player player, Location location) {
        if (ConfigHandler.getInstance().isFarmingXpRequiresTool() && !usingValidTool(player)) return;
        for (Material material : materialList) {
            ExperienceHandler.getInstance().handlePlayerHarvestXp(player, material, location);
        }
    }


    private static boolean isAttachedToStem(@NotNull Block block, Material stemType) {
        for (BlockFace face : facesToGet) {
            Block adjacentBlock = block.getRelative(face);
            if (!adjacentBlock.getType().equals(stemType)) continue;
            if (!(adjacentBlock.getBlockData() instanceof Directional directional)) continue;
            if (!directional.getFacing().getOppositeFace().equals(face)) continue;
            return true;
        }
        return false;
    }

    private static boolean usingValidTool(Player player) {
        ItemStack toolInMainHand = player.getInventory().getItemInMainHand();
        if (toolInMainHand.isEmpty()) return false;
        return ConfigHandler.getInstance().getValidFarmingTools().contains(toolInMainHand.getType());
    }

}
