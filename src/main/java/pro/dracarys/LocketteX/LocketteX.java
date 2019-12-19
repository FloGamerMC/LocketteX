package pro.dracarys.LocketteX;

import com.licel.stringer.annotations.secured;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pro.dracarys.LocketteX.commands.MainCommand;
import pro.dracarys.LocketteX.config.Config;
import pro.dracarys.LocketteX.config.file.ConfigManager;
import pro.dracarys.LocketteX.hooks.HookManager;
import pro.dracarys.LocketteX.listener.*;
import pro.dracarys.LocketteX.utils.Util;

public class LocketteX extends JavaPlugin {

    public static LocketteX plugin;

    public static boolean UseEconomy = false;
    public static Economy econ = null;

    private ConfigManager configManager;

    public static LocketteX getInstance() {
        return plugin;
    }

    public boolean isUsingEconomy() {
        return UseEconomy;
    }

    @Override
    public void onEnable() {
        plugin = this;
        configManager = ConfigManager.getInstance();
        loadConfig();
        checkServerVersion();
        PluginCommand cmd = this.getCommand("lockettex");
        MainCommand executor = new MainCommand();
        if (cmd != null) {
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        }
        printPluginInfo();
        HookManager.getInstance().loadHooks();
        registerListeners(new InventoryOpen(), new BlockBreak(), new BlockPlace(), new SignChange());
        if (Config.USE_INV_MOVE.getOption()) registerListeners(new InventoryMoveItem());
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        HookManager.getInstance().getEnabledHooks().clear();
        plugin = null;
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    @secured
    private void printPluginInfo() {
        Util.sendConsole("&f▆ &f▆ &f▆&f▆ &f▆&f▆&f▆&f▆ &f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆ &f▆&f▆&f▆&f▆ &f▆&f▆ &f▆ &f▆");
        Util.sendConsole(" ");
        Util.sendConsole("&f➤  &c" + getDescription().getName() + " &7v" + getDescription().getVersion() + "&a Enabled ✔");
        Util.sendConsole("&f➤  &f&o" + getDescription().getDescription());
        Util.sendConsole("&f➤ &eMade with &4❤ &eby &f" + getDescription().getAuthors().get(0));
        if (getDescription().getVersion().contains("-DEV"))
            Util.sendConsole("&f➤ &cThis is a BETA, report any unexpected behaviour to the Author!");
        Util.sendConsole(" ");
        Util.sendConsole("&f▆ &f▆ &f▆&f▆ &f▆&f▆&f▆&f▆ &f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆&f▆ &f▆&f▆&f▆&f▆ &f▆&f▆ &f▆ &f▆");
    }

    public void loadConfig() {
        configManager.getFileMap().get("config").init();
        configManager.getFileMap().get("messages").init();
    }

    private static int ver;

    private void checkServerVersion() {
        ver = Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].replace("1_", "").substring(1).replaceAll("_R\\d", ""));
    }

    public static int getServerVersion() {
        return ver;
    }
}
