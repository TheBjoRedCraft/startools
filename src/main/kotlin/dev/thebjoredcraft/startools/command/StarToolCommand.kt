package dev.thebjoredcraft.startools.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.thebjoredcraft.startools.plugin
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.entity.Player

object StarToolCommand {
    fun register() = plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
        event.registrar().register(
            Commands.literal("startools")
                .requires {
                    it.sender.hasPermission("startools.command") ||
                            it.sender.hasPermission("startools.command.give")
                }
                .then(Commands.literal("give")
                        .requires { it.sender.hasPermission("startools.command.give") }
                        .then(Commands.argument("players", ArgumentTypes.players())
                                .then(Commands.literal("pickaxe").executes {
                                    val players = it.getArgument("players", PlayerSelectorArgumentResolver::class.java).resolve(it.source)

                                    players.forEach { it.give() }
                                    Command.SINGLE_SUCCESS
                                })
                                .then(Commands.literal("axe").executes {
                                    val players = it.getArgument("players", PlayerSelectorArgumentResolver::class.java).resolve(it.source)
                                    Command.SINGLE_SUCCESS
                                })
                                .then(Commands.literal("shovel").executes {
                                    val players = it.getArgument("players", PlayerSelectorArgumentResolver::class.java).resolve(it.source)
                                    Command.SINGLE_SUCCESS
                                })
                        )
                )
                .build()
        )
    }
}