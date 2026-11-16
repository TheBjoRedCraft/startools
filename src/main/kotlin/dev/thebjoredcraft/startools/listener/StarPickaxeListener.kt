package dev.thebjoredcraft.startools.listener

import dev.thebjoredcraft.startools.PluginConstants
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.util.Vector
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.roundToInt

object StarPickaxeListener : Listener {
    private val processingBlocks = ConcurrentHashMap.newKeySet<Block>()

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.block in processingBlocks) {
            return
        }

        val player = event.player
        val itemInHand = player.inventory.itemInMainHand

        if (!itemInHand.persistentDataContainer.has(PluginConstants.PICKAXE_KEY)) {
            return
        }

        val direction = player.location.direction
        val right = direction.clone().crossProduct(Vector(0, 1, 0)).normalize()
        val up = right.clone().crossProduct(direction).normalize()

        val blocks = buildList {
            for (x in -1..1) {
                for (y in -1..1) {
                    val block = event.block.getRelative(
                        right.x.roundToInt() * x + up.x.roundToInt() * y,
                        right.y.roundToInt() * x + up.y.roundToInt() * y,
                        right.z.roundToInt() * x + up.z.roundToInt() * y
                    )

                    if (block != event.block) {
                        add(block)
                    }
                }
            }
        }

        val validBlocks = mutableListOf<Block>()

        blocks.forEach { block ->
            processingBlocks.add(block)

            val blockEvent = BlockBreakEvent(block, player)
            Bukkit.getPluginManager().callEvent(blockEvent)

            if (!blockEvent.isCancelled) {
                validBlocks.add(block)
            }
        }

        validBlocks.forEach { block ->
            block.breakNaturally(itemInHand)

            block.world.spawnParticle(
                Particle.DUST,
                block.location,
                10,
                Particle.DustOptions(Color.fromRGB(180, 44, 81), 1f)
            )

            player.playSound(
                block.location,
                Sound.BLOCK_AMETHYST_BLOCK_BREAK,
                1f,
                1f
            )
        }

        blocks.forEach(processingBlocks::remove)
    }
}