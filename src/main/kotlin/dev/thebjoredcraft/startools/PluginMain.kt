package dev.thebjoredcraft.startools

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.thebjoredcraft.railway.config.RailwayConfigFormat
import dev.thebjoredcraft.railway.config.manager.ConfigManager
import dev.thebjoredcraft.startools.config.Config
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PluginMain::class.java)

val config by lazy {
    ConfigManager.create(plugin.dataPath.resolve("config.yml"), RailwayConfigFormat.YAML, Config()).load()
}

class PluginMain : SuspendingJavaPlugin() {
}