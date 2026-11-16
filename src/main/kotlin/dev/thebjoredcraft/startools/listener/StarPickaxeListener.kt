package dev.thebjoredcraft.startools.listener

import dev.thebjoredcraft.startools.PluginConstants
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
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

        val rightBlock = Vector(
            right.x.roundToInt().toDouble(),
            right.y.roundToInt().toDouble(),
            right.z.roundToInt().toDouble()
        )

        val upBlock = Vector(
            up.x.roundToInt().toDouble(),
            up.y.roundToInt().toDouble(),
            up.z.roundToInt().toDouble()
        )

        val hitFace = player.rayTraceBlocks(6.0)?.hitBlockFace ?: return

        val blocks = buildList {
            for (x in -1..1) {
                for (y in -1..1) {
                    if (x == 0 && y == 0) continue

                    val block = when (hitFace) {
                        BlockFace.NORTH, BlockFace.SOUTH ->
                            event.block.getRelative(x, y, 0)

                        BlockFace.EAST, BlockFace.WEST ->
                            event.block.getRelative(0, y, x)

                        BlockFace.UP, BlockFace.DOWN ->
                            event.block.getRelative(x, 0, y)

                        else -> continue
                    }

                    add(block)
                }
            }
        }.filter {
            Tag.MINEABLE_PICKAXE.isTagged(it.type) &&
                    it.isPreferredTool(itemInHand)
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
                block.location.clone().add(0.5, 0.5, 0.5),
                10,
                Particle.DustOptions(Color.fromRGB(180, 44, 81), 1f)
            )

            player.playSound(
                block.location,
                Sound.BLOCK_AMETHYST_BLOCK_STEP,
                1f,
                1f
            )
        }

        blocks.forEach(processingBlocks::remove)
    }
}