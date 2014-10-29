package lu.blocks.scary_block.blocksvanish;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.earth2me.essentials.IEssentials;

public class BlocksVanish extends JavaPlugin {
	
	public static final String mc = "§f";
	public static final String pc = "§8[Vanish] " + mc;
	public static final String sc = "§a";

	public static IEssentials iess;
	public static boolean pex;

	public static File vanishfile = new File("plugins/BlocksVanish/vanishfile.yml");
	public static FileConfiguration vanishconfig = YamlConfiguration.loadConfiguration(vanishfile);
	
	public void onDisable() {
		Vanish.Instance().saveVanishPlayers();
	}
	
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		
		if(pm.isPluginEnabled("PermissionsEx")){
			pex = true;
			System.out.println("PermissionsEx wurde geladen.");
		} else {
			pex = false;
			System.out.println("PermissionsEx wurde nicht gefunden.");
		}
		if(pm.isPluginEnabled("Essentials")){
			iess = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
			System.out.println("Essentials wurde geladen.");
		} else {
			iess = null;
			System.out.println("Essentials wurde nicht gefunden.");
		}
		
		this.getCommand("vanish").setExecutor(new VanishCommand());
		pm.registerEvents(new VanishListener(), this);
		Vanish.Instance().loadVanishPlayers();
	}
	
	public static String getPexPrefix(Player p) {
		return ChatColor
				.translateAlternateColorCodes('&', BlocksVanish.pex ? PermissionsEx.getUser(p).getPrefix() : "");
	}
}
