package dev.thebjoredcraft.startools.itemupdate

import dev.thebjoredcraft.startools.PluginConstants
import dev.thebjoredcraft.startools.config
import dev.thebjoredcraft.startools.plugin
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.security.MessageDigest

object ItemUpdater {
    private val hasher = MessageDigest.getInstance("SHA-256")
    val HASH_KEY = NamespacedKey(plugin, "hash")

    var pickaxeHash = "UNINITIALIZED"
    var axeHash = "UNINITIALIZED"
    var shovelHash = "UNINITIALIZED"


    fun refreshHashes() {
        pickaxeHash = config.starPickaxe.item.toItemStack().generateHash()
        axeHash = config.starAxe.item.toItemStack().generateHash()
        shovelHash = config.starShovel.item.toItemStack().generateHash()
    }

    fun printHash(itemStack: ItemStack) = itemStack.apply {
        editPersistentDataContainer {
            it.set(HASH_KEY, PersistentDataType.STRING, itemStack.generateHash())
        }
    }

    fun isNewestItem(itemStack: ItemStack): Boolean {
        val hash =
            itemStack.itemMeta?.persistentDataContainer?.get(HASH_KEY, PersistentDataType.STRING)
        val isPickaxe = itemStack.persistentDataContainer.has(PluginConstants.PICKAXE_KEY)
        val isAxe = itemStack.persistentDataContainer.has(PluginConstants.AXE_KEY)
        val isShovel = itemStack.persistentDataContainer.has(PluginConstants.SHOVEL_KEY)

        return when {
            isPickaxe -> hash == pickaxeHash
            isAxe -> hash == axeHash
            isShovel -> hash == shovelHash
            else -> true
        }
    }

    fun ItemStack.generateHash(): String {
        val meta = itemMeta

        val data = buildString {
            append(type.key.asString())
            append('|')
            append(meta.hasDisplayName())
            append('|')
            append(meta.displayName()?.toString().orEmpty())
            append('|')
            append(meta.lore()?.joinToString("\n") { it.toString() }.orEmpty())
            append('|')

            meta.enchants.toSortedMap(compareBy { it.key.asString() })
                .forEach { (enchantment, level) ->
                    append(enchantment.key.asString())
                    append(':')
                    append(level)
                    append(';')
                }
        }

        return hasher
            .digest(data.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }
}