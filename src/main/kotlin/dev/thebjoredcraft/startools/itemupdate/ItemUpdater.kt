package dev.thebjoredcraft.startools.itemupdate

import dev.thebjoredcraft.startools.PluginConstants
import dev.thebjoredcraft.startools.plugin
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType
import java.security.MessageDigest


fun ItemStack.printHash(): ItemStack {
    return ItemUpdater.printHash(this)
}

object ItemUpdater {
    private val hasher = MessageDigest.getInstance("SHA-256")
    val HASH_KEY = NamespacedKey(plugin, "hash")

    var pickaxeHash = "UNINITIALIZED"
    var axeHash = "UNINITIALIZED"
    var shovelHash = "UNINITIALIZED"

    fun refreshHashes() {
        pickaxeHash = PluginConstants.PICKAXE.generateHash()
        axeHash = PluginConstants.AXE.generateHash()
        shovelHash = PluginConstants.SHOVEL.generateHash()
    }

    fun printHash(itemStack: ItemStack): ItemStack {
        itemStack.editPersistentDataContainer {
            it.set(HASH_KEY, PersistentDataType.STRING, itemStack.generateHash())
        }

        return itemStack
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

    fun updateItem(oldItem: ItemStack): ItemStack {
        val newItem = when {
            oldItem.persistentDataContainer.has(PluginConstants.PICKAXE_KEY) ->
                PluginConstants.PICKAXE

            oldItem.persistentDataContainer.has(PluginConstants.AXE_KEY) ->
                PluginConstants.AXE

            oldItem.persistentDataContainer.has(PluginConstants.SHOVEL_KEY) ->
                PluginConstants.SHOVEL

            else -> return oldItem
        }

        val oldMeta = oldItem.itemMeta
        val newMeta = newItem.itemMeta

        if (oldMeta is Damageable &&
            newMeta is Damageable
        ) {
            newMeta.damage = oldMeta.damage
        }

        newItem.itemMeta = newMeta

        return printHash(newItem)
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