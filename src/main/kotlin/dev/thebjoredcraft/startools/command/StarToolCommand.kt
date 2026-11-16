package dev.thebjoredcraft.startools.command

import com.mojang.brigadier.Command
import dev.thebjoredcraft.startools.PREFIX
import dev.thebjoredcraft.startools.PluginConstants
import dev.thebjoredcraft.startools.plugin
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents

object StarToolCommand {
    fun register() =
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(
                Commands.literal("startools")
                    .requires {
                        it.sender.hasPermission("startools.command") ||
                                it.sender.hasPermission("startools.command.give")
                    }
                    .then(
                        Commands.literal("give")
                            .requires { it.sender.hasPermission("startools.command.give") }
                            .then(
                                Commands.argument("players", ArgumentTypes.players())
                                    .then(Commands.literal("pickaxe").executes {
                                        val players = it.getArgument(
                                            "players",
                                            PlayerSelectorArgumentResolver::class.java
                                        ).resolve(it.source)

                                        players.forEach { target -> target.give(PluginConstants.PICKAXE) }
                                        it.source.sender.sendRichMessage("$PREFIX Gave Star Pickaxe to <#eb1e56>${players.size} player(s)")
                                        Command.SINGLE_SUCCESS
                                    })
                                    .then(Commands.literal("axe").executes {
                                        val players = it.getArgument(
                                            "players",
                                            PlayerSelectorArgumentResolver::class.java
                                        ).resolve(it.source)

                                        players.forEach { target -> target.give(PluginConstants.AXE) }
                                        it.source.sender.sendRichMessage("$PREFIX Gave Star Axe to <#eb1e56>${players.size} player(s)")
                                        Command.SINGLE_SUCCESS
                                    })
                                    .then(Commands.literal("shovel").executes {
                                        val players = it.getArgument(
                                            "players",
                                            PlayerSelectorArgumentResolver::class.java
                                        ).resolve(it.source)

                                        players.forEach { target -> target.give(PluginConstants.SHOVEL) }
                                        it.source.sender.sendRichMessage("$PREFIX Gave Star Shovel to <#eb1e56>${players.size} player(s)")
                                        Command.SINGLE_SUCCESS
                                    })
                            )
                    )
                    .then(Commands.literal("reload").executes {
                        plugin.configManager.reload()
                        it.source.sender.sendRichMessage("$PREFIX Reloaded config")
                        Command.SINGLE_SUCCESS
                    }.build())
                    .build()
            )
        }
}