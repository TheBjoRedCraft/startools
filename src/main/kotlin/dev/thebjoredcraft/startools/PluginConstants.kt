package dev.thebjoredcraft.startools

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemType
import org.bukkit.persistence.PersistentDataType

const val PREFIX = "<dark_gray>» <#eb1e56>Startools <dark_gray>|<white>"

@Suppress("UnstableApiUsage")
object PluginConstants {
    private val miniMessage = MiniMessage.miniMessage()

    val PICKAXE_KEY = NamespacedKey(plugin, "pickaxe")
    val AXE_KEY = NamespacedKey(plugin, "axe")
    val SHOVEL_KEY = NamespacedKey(plugin, "shovel")

    val PICKAXE = ItemType.NETHERITE_PICKAXE.createItemStack {
        it.customName(miniMessage.deserialize("<!i><#B42C51>Star Pickaxe"))
        it.persistentDataContainer.set(PICKAXE_KEY, PersistentDataType.BYTE, 1)
    }

    val AXE = ItemType.NETHERITE_AXE.createItemStack {
        it.customName(miniMessage.deserialize("<!i><#B42C51>Star Axe"))
        it.persistentDataContainer.set(PICKAXE_KEY, PersistentDataType.BYTE, 1)
    }

    val SHOVEL = ItemType.NETHERITE_SHOVEL.createItemStack {
        it.customName(miniMessage.deserialize("<!i><#B42C51>Star Shovel"))
        it.persistentDataContainer.set(PICKAXE_KEY, PersistentDataType.BYTE, 1)
    }
}