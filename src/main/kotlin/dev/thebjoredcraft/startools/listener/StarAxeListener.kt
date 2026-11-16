package dev.thebjoredcraft.startools.listener

import dev.thebjoredcraft.startools.PluginConstants
import dev.thebjoredcraft.startools.config
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Tag
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object StarAxeListener : Listener {
    private val processingBlocks = ConcurrentHashMap.newKeySet<Block>()

    private val logTags = listOf(
        Tag.LOGS,
        Tag.CRIMSON_STEMS,
        Tag.WARPED_STEMS
    )

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!config.starAxe.enabled) {
            return
        }

        if (event.block in processingBlocks) {
            return
        }

        val player = event.player
        val item = player.inventory.itemInMainHand

        if (!item.persistentDataContainer.has(PluginConstants.AXE_KEY)) {
            return
        }
        if (!isLog(event.block)) {
            return
        }

        val logs = findConnectedLogs(event.block)

        logs.forEach { block ->
            processingBlocks.add(block)

            try {
                val blockEvent = BlockBreakEvent(block, player)
                Bukkit.getPluginManager().callEvent(blockEvent)

                if (blockEvent.isCancelled || block.type == Material.AIR) {
                    return@forEach
                }

                block.breakNaturally(item)

                block.world.spawnParticle(
                    Particle.DUST,
                    block.location.clone().add(0.5, 0.5, 0.5),
                    10,
                    Particle.DustOptions(config.starAxe.breakParticleColor.toBukkitColor(), 1f)
                )

                player.playSound(config.starAxe.breakSound.toSound(), block.location)
            } finally {
                processingBlocks.remove(block)
            }
        }
    }

    private fun findConnectedLogs(start: Block): List<Block> {
        val visited = HashSet<Block>()
        val queue = ArrayDeque<Block>()

        visited.add(start)
        queue.add(start)

        while (queue.isNotEmpty() && visited.size < config.starAxe.maxBlocks + 1) {
            val current = queue.removeFirst()

            for (face in BlockFace.entries) {
                val relative = current.getRelative(face)

                if (!visited.add(relative)) {
                    continue
                }

                if (!isLog(relative)) {
                    visited.remove(relative)
                    continue
                }

                queue.add(relative)

                if (visited.size >= config.starAxe.maxBlocks + 1) {
                    break
                }
            }
        }

        return visited.filter { it != start }
    }

    private fun isLog(block: Block) = logTags.any { it.isTagged(block.type) }
}
