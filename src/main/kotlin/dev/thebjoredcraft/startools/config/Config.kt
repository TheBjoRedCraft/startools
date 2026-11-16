package dev.thebjoredcraft.startools.config

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.Color
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
        val cooldown: Long = 0L,
        val breakParticleColor: RGBColor = RGBColor(180, 44, 81),
        val breakSound: SoundConfig = SoundConfig(soundKey = "minecraft:block.amethyst_block.step")
    )

    @ConfigSerializable
    data class StarShovelConfig(
        val enabled: Boolean = true,
        val cooldown: Long = 0L,
        val breakParticleColor: RGBColor = RGBColor(180, 44, 81),
        val breakSound: SoundConfig = SoundConfig(soundKey = "minecraft:block.amethyst_block.step")
    )

    @ConfigSerializable
    data class StarAxeConfig(
        val enabled: Boolean = true,
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
}
