package dev.thebjoredcraft.startools

import dev.thebjoredcraft.startools.itemupdate.printHash
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

const val PREFIX = "<dark_gray>» <#eb1e56>Startools <dark_gray>|<white>"
val miniMessage = MiniMessage.miniMessage()

@Suppress("UnstableApiUsage")
object PluginConstants {
    val PICKAXE_KEY = NamespacedKey(plugin, "pickaxe")
    val AXE_KEY = NamespacedKey(plugin, "axe")
    val SHOVEL_KEY = NamespacedKey(plugin, "shovel")

    val PICKAXE
        get() = config.starPickaxe.item.toItemStack().apply {
            editPersistentDataContainer {
                it.set(PICKAXE_KEY, PersistentDataType.BYTE, 1)
            }
        }.printHash()

    val AXE
        get() = config.starAxe.item.toItemStack().apply {
            editPersistentDataContainer {
                it.set(AXE_KEY, PersistentDataType.BYTE, 1)
            }
        }.printHash()

    val SHOVEL
        get() = config.starShovel.item.toItemStack().apply {
            editPersistentDataContainer {
                it.set(SHOVEL_KEY, PersistentDataType.BYTE, 1)
            }
        }.printHash()


    fun isStarTool(itemStack: ItemStack): Boolean {
        return itemStack.persistentDataContainer.has(PICKAXE_KEY) ||
                itemStack.persistentDataContainer.has(AXE_KEY) ||
                itemStack.persistentDataContainer.has(SHOVEL_KEY)
    }
}