package dev.thebjoredcraft.startools.config

import dev.thebjoredcraft.startools.miniMessage
import dev.thebjoredcraft.startools.plugin
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Color
import org.bukkit.Material
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class Config(
    val starPickaxe: StarPickaxeConfig = StarPickaxeConfig(),
    val starShovel: StarShovelConfig = StarShovelConfig(),
    val starAxe: StarAxeConfig = StarAxeConfig()
) {
    @ConfigSerializable
    data class StarPickaxeConfig(
        val enabled: Boolean = true,
        val item: ItemConfig = ItemConfig(
            material = Material.NETHERITE_PICKAXE,
            name = "<!i><#B42C51>Star Pickaxe",
            lore = listOf(),
            enchantments = mapOf()
        ),
        val cooldown: Long = 0L,
        val breakParticleColor: RGBColor = RGBColor(180, 44, 81),
        val breakSound: SoundConfig = SoundConfig(soundKey = "minecraft:block.amethyst_block.step")
    )

    @ConfigSerializable
    data class StarShovelConfig(
        val enabled: Boolean = true,
        val item: ItemConfig = ItemConfig(
            material = Material.NETHERITE_SHOVEL,
            name = "<!i><#B42C51>Star Shovel",
            lore = listOf(),
            enchantments = mapOf()
        ),
        val cooldown: Long = 0L,
        val breakParticleColor: RGBColor = RGBColor(180, 44, 81),
        val breakSound: SoundConfig = SoundConfig(soundKey = "minecraft:block.amethyst_block.step")
    )

    @ConfigSerializable
    data class StarAxeConfig(
        val enabled: Boolean = true,
        val item: ItemConfig = ItemConfig(
            material = Material.NETHERITE_AXE,
            name = "<!i><#B42C51>Star Axe",
            lore = listOf(),
            enchantments = mapOf()
        ),
        val cooldown: Long = 0L,
        val maxBlocks: Int = 50,
        val breakParticleColor: RGBColor = RGBColor(180, 44, 81),
        val breakSound: SoundConfig = SoundConfig(soundKey = "minecraft:block.amethyst_block.step")
    )

    @ConfigSerializable
    data class SoundConfig(
        val soundKey: String = "",
        val soundVolume: Float = 1f,
        val soundPitch: Float = 1f
    ) {
        fun toSound() = Sound.sound(Key.key(soundKey), Sound.Source.BLOCK, soundVolume, soundPitch)
    }

    @ConfigSerializable
    data class RGBColor(
        val red: Int = 0,
        val green: Int = 0,
        val blue: Int = 0
    ) {
        fun toBukkitColor() = Color.fromRGB(red, green, blue)
    }


    @ConfigSerializable
    data class ItemConfig(
        val material: Material = Material.NETHERITE_PICKAXE,
        val name: String = "<#B42C51>Test Pickaxe",
        val lore: List<String> = listOf("<dark_gray>Some Text"),
        val enchantments: Map<String, Int> = mapOf(
            "minecraft:unbreaking" to 3,
            "minecraft:efficiency" to 5
        )
    ) {
        @Transient
        private val enchantmentRegistry =
            RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)

        fun toItemStack() = material.asItemType()?.createItemStack()?.apply {
            itemMeta = itemMeta.apply {
                customName(miniMessage.deserialize("<!i>$name"))
                lore(this@ItemConfig.lore.map { miniMessage.deserialize("<!i>$it") })

                this@ItemConfig.enchantments.forEach {
                    enchantmentRegistry.get(Key.key(it.key))?.let { enchantment ->
                        addEnchant(enchantment, it.value, true)
                    } ?: run {
                        plugin.logger.warning("Enchantment ${it.key} is not a valid enchantment")
                    }
                }
            }
        } ?: error("Material $material is not a valid item type")
    }
}
