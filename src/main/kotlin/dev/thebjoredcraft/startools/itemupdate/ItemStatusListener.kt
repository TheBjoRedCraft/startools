package dev.thebjoredcraft.startools.itemupdate

import dev.thebjoredcraft.startools.PluginConstants
import dev.thebjoredcraft.startools.plugin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent

object ItemStatusListener : Listener {
    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val player = event.player as? Player ?: return

        event.inventory.contents.forEachIndexed { index, item ->
            if (item == null || item.type.isAir) {
                return@forEachIndexed
            }

            if (!PluginConstants.isStarTool(item)) {
                return@forEachIndexed
            }

            if (ItemUpdater.isNewestItem(item)) {
                return@forEachIndexed
            }

            val newItem = ItemUpdater.updateItem(item)
            event.inventory.setItem(index, newItem)
            plugin.logger.info("Updated startool ${newItem.type} for player ${player.name}")
        }
    }
}