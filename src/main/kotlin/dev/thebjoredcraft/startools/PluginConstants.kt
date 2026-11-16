package dev.thebjoredcraft.startools

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFactory
import org.bukkit.inventory.ItemType
import org.bukkit.persistence.PersistentDataType
import org.checkerframework.checker.units.qual.min

object PluginConstants {
    val PICKAXE_KEY = NamespacedKey(plugin, "pickaxe")
    val AXE_KEY = NamespacedKey(plugin, "axe")
    val SHOVEL_KEY = NamespacedKey(plugin, "shovel")

    private val miniMessage = MiniMessage.miniMessage()

    val PICKAXE = ItemType.NETHERITE_PICKAXE.createItemStack {
        it.displayName(miniMessage.deserialize("<#B42C51>Star Pickaxe"))
        it.persistentDataContainer.set(PICKAXE_KEY, PersistentDataType.BYTE, 1)
    }

    val AXE = ItemType.NETHERITE_AXE.createItemStack {
        it.displayName(miniMessage.deserialize("<#B42C51>Star Axe"))
        it.persistentDataContainer.set(PICKAXE_KEY, PersistentDataType.BYTE, 1)
    }

    val SHOVEL = ItemType.NETHERITE_SHOVEL.createItemStack {
        it.displayName(miniMessage.deserialize("<#B42C51>Star Shovel"))
        it.persistentDataContainer.set(PICKAXE_KEY, PersistentDataType.BYTE, 1)
    }
}