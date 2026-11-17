package dev.thebjoredcraft.startools.itemupdate

import dev.thebjoredcraft.startools.PluginConstants
import dev.thebjoredcraft.startools.plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerJoinEvent

object ItemStatusListener : Listener {
    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
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
            plugin.logger.info("Updated startool ${newItem.type} for player ${event.player.name}")
        }
    }

    @EventHandler
    fun onPickup(event: PlayerAttemptPickupItemEvent) {
        val item = event.item.itemStack

        if (!PluginConstants.isStarTool(item)) {
            return
        }

        if (ItemUpdater.isNewestItem(item)) {
            return
        }

        val newItem = ItemUpdater.updateItem(item)
        event.item.itemStack = newItem
        plugin.logger.info("Updated startool ${newItem.type} for player ${event.player.name}")
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        updatePlayerInventory(event.player)
    }
    

    private fun updatePlayerInventory(player: Player) {
        val inventory = player.inventory

        inventory.contents.forEachIndexed { index, item ->
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
            inventory.setItem(index, newItem)
            plugin.logger.info("Updated startool ${newItem.type} for player ${player.name}")
        }
    }

    fun updatePlayerInventories() = Bukkit.getOnlinePlayers().forEach { player ->
        updatePlayerInventory(player)
    }
}