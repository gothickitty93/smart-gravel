package com.kittydiaz.smartgravel;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;

public class SmartGravelPlugin extends JavaPlugin implements Listener {

    private NamespacedKey key;

    @Override
    public void onEnable() {
        key = new NamespacedKey(this, "smartgravel");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("SmartGravelPlugin enabled!");
    }

    public ItemStack createSmartGravelItem() {
        ItemStack gravel = new ItemStack(Material.GRAVEL);
        ItemMeta meta = gravel.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GRAY + "Smart Gravel");
            meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            gravel.setItemMeta(meta);
        }
        return gravel;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item == null) return;
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE)) return;

        Block placedBlock = event.getBlockPlaced();

        fillDownwards(placedBlock);
    }

    private void fillDownwards(Block startBlock) {
        Block current = startBlock.getRelative(0, -1, 0);

        while (current.getType() == Material.AIR || current.getType() == Material.WATER) {
            current.setType(Material.GRAVEL);
            current = current.getRelative(0, -1, 0);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("smartgravel")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can get Smart Gravel.");
                return true;
            }
            Player player = (Player) sender;
            player.getInventory().addItem(createSmartGravelItem());
            player.sendMessage(ChatColor.GREEN + "You got some Smart Gravel!");
            return true;
        }
        return false;
    }
}
