package lu.blocks.scary_block.blocksvanish;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Vanish {
	
	private static Vanish instance = new Vanish();
	
	public static Vanish Instance(){
		return instance;
	}

	public static HashSet<String> vanishedPlayer = new HashSet<String>();

	/**
	 * Vanishstatus umschalten
	 * 
	 * @param p
	 *            Spieler von dem der Vanishstatus umgeschaltet werden soll.
	 */
	public void toggleVanish(Player p) {
		if (Vanish.vanishedPlayer.contains(p.getUniqueId() + ""))
			unVanish(p);
		else
			doVanish(p);

	}

	/**
	 * Vanishstatus setzen
	 * 
	 * @param p
	 *            Der Spieler dessen Vanishstatus bearbeitet werden soll.
	 * @param b
	 *            Der Boolean der definiert wie der Vanishstatus sein soll.
	 */
	public void setVanish(Player p, Boolean b) {
		if (b)
			doVanish(p);
		else
			unVanish(p);
	}

	/**
	 * Einen Spieler vor andern Spielern verstecken und die jeweiligen
	 * Nachrichten an alle Spieler schicken.
	 * 
	 * @param p
	 *            Der Spieler der Vanish gemacht werden soll.
	 */
	public void doVanish(Player p) {
		String prefix = BlocksVanish.pc;
		for (Player players : Bukkit.getServer().getOnlinePlayers()) {
			if (players.hasPermission("blocks.vanish"))
				continue;
			if ((p != null) && (p.isOnline()) && (players != null)
					&& (players.isOnline()))
				players.hidePlayer(p);
		}
		Vanish.vanishedPlayer.add(p.getUniqueId() + "");
		setVanishScoreboard(p);
		if (BlocksVanish.iess != null)
			EssentialsUtils.Instance().setVanish(p, true);
		p.sendMessage(prefix + "Du bist nun im Vanish!");
		sendVanishPlayerMessage(p);
	}

	/**
	 * Einen Spieler wieder sichtbar machen für alle und die jeweiligen
	 * Nachrichten an alle Spieler schicken.
	 * 
	 * @param p
	 *            Der Spieler der sichtbar gemacht werden soll.
	 */
	public void unVanish(Player p) {
		for (Player players : Bukkit.getServer().getOnlinePlayers()) {
			if ((p != null) && (p.isOnline()) && (players != null)
					&& (players.isOnline()))
				players.showPlayer(p);
		}
		if (Vanish.vanishedPlayer.contains(p.getUniqueId() + ""))
			Vanish.vanishedPlayer.remove(p.getUniqueId() + "");
		if (BlocksVanish.iess != null) {
			EssentialsUtils.Instance().setVanish(p, false);
		}
		if (canVanish(p)) {
			String prefix = BlocksVanish.pc;
			p.sendMessage(prefix + "Du bist nun nicht mehr im Vanish!");
			sendunVanishPlayerMessage(p);
		}
		setVanishScoreboard(p);
	}

	/**
	 * OfflinePlayer unvanishen
	 * 
	 * @param op
	 *            Der Offlineplayer
	 */
	public void unVanish(OfflinePlayer op) {
		if (Vanish.vanishedPlayer.contains(op.getUniqueId() + ""))
			Vanish.vanishedPlayer.remove(op.getUniqueId() + "");
		if (BlocksVanish.iess != null) {
			EssentialsUtils.Instance().setVanish(op, false);
		}
	}

	/**
	 * Den andern Spielern melden dass ein Spieler nicht mehr im Vanish ist oder
	 * anzeigen als ob er grad gejoint ist.
	 * 
	 * @param p
	 *            Der Spieler über den die Nachricht geschickt werden soll.
	 */
	public void sendunVanishPlayerMessage(Player p) {
		String join = "§7* &d§7 hat das Spiel betreten.";
		String deVanish = BlocksVanish.pc + BlocksVanish.getPexPrefix(p) + BlocksVanish.mc
				+ " ist nun wieder aus dem Vanish raus!";

		for (Player players : Bukkit.getServer().getOnlinePlayers()) {
			if (players == p)
				continue;
			if (players.hasPermission("blocks.vanish"))
				players.sendMessage(deVanish);
			else
				players.sendMessage(join);
		}
	}

	/**
	 * Den andern Spielern mitteilen dass ein Spieler nun im Vanish ist oder
	 * anzeigen als ob er geleavet wäre.
	 * 
	 * @param p
	 *            Der Spieler über den die Nachricht geschickt werden soll.
	 */
	private void sendVanishPlayerMessage(Player p) {
		String quit = "§7* &d§7 hat das Spiel verlassen.";
		String doVanish = BlocksVanish.pc + BlocksVanish.getPexPrefix(p) + BlocksVanish.mc
				+ " ist jetzt im Vanish!";

		for (Player players : Bukkit.getServer().getOnlinePlayers()) {
			if (players == p)
				continue;
			if (players.hasPermission("blocks.vanish"))
				players.sendMessage(doVanish);
			else
				players.sendMessage(quit);
		}
	}

	/**
	 * überprüfen ob ein Spieler im Vanish ist.
	 * 
	 * @param p
	 *            Der Spieler der überprüft wird
	 * @return ein Boolean der mitteilt ob der Spieler im Vanish ist.
	 */
	public boolean isVanish(Player p) {
		if (p == null)
			return false;
		return Vanish.vanishedPlayer.contains(p.getUniqueId() + "");
	}

	/**
	 * überprüfen ob ein OfflinePlayer im Vanish ist.
	 * 
	 * @param op
	 *            Der OfflineSpieler..
	 * @return gibt nen Boolean zurück mit dem Vanishwert.
	 */
	public boolean isVanish(OfflinePlayer op) {
		if (op == null)
			return false;
		return Vanish.vanishedPlayer.contains(op.getUniqueId() + "");
	}

	/**
	 * Alle Spieler wieder sichtbar machen.
	 */
	public void unVanishAll() {
		/*
		 * for(String s : Vanish.vanishedPlayer){ Player p =
		 * Bukkit.getPlayer(s); if(s == null){ Vanish.vanishedPlayer.remove(s);
		 * continue; } Vanish.deVanish(p); }
		 */
		Iterator<String> interator = Vanish.vanishedPlayer.iterator();
		while (interator.hasNext()) {
			Player p = Bukkit.getPlayer(UUID.fromString(interator.next()));
			if (p != null)
				unVanish(p);
		}
	}

	/**
	 * abfragen wie viele Spieler im Vanish sind.
	 * 
	 * @return Anzahl an Vanish Spieler
	 */
	public List<Player> getOnlineVanish() {
		List<Player> playerlist = new ArrayList<Player>();
		for (String vp : Vanish.vanishedPlayer) {
			Player p = Bukkit.getServer().getPlayer(UUID.fromString(vp));
			if (p != null)
				playerlist.add(p);
		}
		return playerlist;
	}

	/**
	 * Die Vanish Spieler aktualisieren / Fehler beheben...
	 */
	public void refresh() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!canVanish(p) && isVanish(p))
				unVanish(p);
			setVanishScoreboard(p);
			continue;
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (p == players)
					continue;
				if (isVanish(players)) {
					if (!(canVanish(p))) {
						p.hidePlayer(players);
						continue;
					}
				}
				p.showPlayer(players);
				continue;
			}
		}
	}

	/**
	 * überprüfen ob ein Spieler Vanish kann
	 * 
	 * @param p
	 *            Der Spieler der überprüft wird
	 * @return Der Wert ob ein Spieler Vanish ist.
	 */
	public boolean canVanish(Player p) {
		return p.hasPermission("blocks.vanish");
	}

	/**
	 * Checken ob ein OfflinePlayer Vanish kann...
	 * 
	 * @param op
	 *            Der OfflinePlayer...
	 * @return Den Vanishwert...
	 */
	public boolean canVanish(OfflinePlayer op) {
		/*
		 * if (Blocks.pex) { PermissionUser pexp =
		 * PermissionsEx.getUser((Player) op); return pexp.has("blocks.vanish");
		 * } else { return true; }
		 */
		return true;
	}

	/**
	 * Die YAML der Vanishspieler laden und zur Liste hinzufügen und wenn noch
	 * nicht erstellt erstellen.
	 */
	public void loadVanishPlayers() {
		File vanishfile = BlocksVanish.vanishfile;
		FileConfiguration vanishconfig = BlocksVanish.vanishconfig;
		try{
			if (!vanishfile.getParentFile().exists()) vanishfile.getParentFile().mkdirs();
			if(!BlocksVanish.vanishfile.exists()) BlocksVanish.vanishfile.createNewFile();
			vanishconfig.load(BlocksVanish.vanishfile);
			Vanish.vanishedPlayer.addAll(vanishconfig.getStringList("players"));
			vanishconfig.set("players", null);
			vanishconfig.save(BlocksVanish.vanishfile);
		} catch(IOException | InvalidConfigurationException e){
			System.out.println("Fehler beim laden der Vanishconfig!");
			e.printStackTrace();
		}
		return;
	}

	/**
	 * Die YAML der Vanishspieler mit allen Spielern füttern die noch Vanish
	 * sind (vanishedPlayer)
	 */
	public void saveVanishPlayers() {
		FileConfiguration vanishfile = BlocksVanish.vanishconfig;
		ArrayList<String> players = new ArrayList<String>(Vanish.vanishedPlayer);
		vanishfile.set("players", players);
		try {
			vanishfile.save(BlocksVanish.vanishfile);
		} catch (IOException e) {
			System.out.println("Die Vanishspieler konnten nicht gespeichert werden!");
			e.printStackTrace();
		}
	}

	/**
	 * Scoreboard bei Vanishspielern anzeigen lassen!
	 * 
	 * @param p
	 *            Spieler bei dem ein Scoreboard angezeigt wird
	 */
	public void setVanishScoreboard(Player p) {
		if (isVanish(p)) {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();
			Objective objective = board.registerNewObjective(p.getName(), "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName("§f§lVanish:");
			Score score = objective.getScore(Bukkit
					.getOfflinePlayer("    §2§l\u2714"));
			score.setScore(1);
			p.setScoreboard(board);
		} else {
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}

	}

}
