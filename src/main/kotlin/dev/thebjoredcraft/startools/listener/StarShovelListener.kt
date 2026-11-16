package dev.thebjoredcraft.startools.listener

import dev.thebjoredcraft.startools.PluginConstants
import dev.thebjoredcraft.startools.config
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Tag
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import java.util.concurrent.ConcurrentHashMap

object StarShovelListener : Listener {
    private val processingBlocks = ConcurrentHashMap.newKeySet<Block>()

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!config.starShovel.enabled) {
            return
        }

        if (event.block in processingBlocks) {
            return
        }

        val player = event.player
        val itemInHand = player.inventory.itemInMainHand

        if (!itemInHand.persistentDataContainer.has(PluginConstants.SHOVEL_KEY)) {
            return
        }

        if (!Tag.MINEABLE_SHOVEL.isTagged(event.block.type) || !event.block.isPreferredTool(
                itemInHand
            )
        ) {
            return
        }

        val hitFace = player.rayTraceBlocks(6.0)?.hitBlockFace ?: return

        val blocks = buildList {
            for (x in -1..1) {
                for (y in -1..1) {
                    if (x == 0 && y == 0) {
                        continue
                    }

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
            Tag.MINEABLE_SHOVEL.isTagged(it.type) &&
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
                Particle.DustOptions(config.starShovel.breakParticleColor.toBukkitColor(), 1f)
            )

            player.playSound(config.starShovel.breakSound.toSound(), block.location)
        }

        blocks.forEach(processingBlocks::remove)
    }
}