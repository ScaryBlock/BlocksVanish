package lu.blocks.scary_block.blocksvanish;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		String prefix = BlocksVanish.pc;
		Player p = null;

		/*
		 * ->/unvanish Spieler unvanishen
		 */
		if (label.equalsIgnoreCase("unvanish")) {
			/*
			 * ->/unvanish * * If ARGS länger als 1 ->USAGE
			 */
			if (args.length > 1) {
				cs.sendMessage(prefix + "/unvanish [Player]");
				return true;
			}

			/*
			 * Checken wer Vanish sein soll ->CommandSender oder eingegebener
			 * Player
			 */
			if (args.length == 1) {
				/*
				 * ->/unvanish <Player>
				 */
				p = Bukkit.getServer().getPlayer(args[0]);
				if (p == null) {
					OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(args[0]);
					if (op == null) {
						cs.sendMessage(prefix + "Der Spieler "
								+ BlocksVanish.sc + args[0] + BlocksVanish.mc
								+ " wurde nicht gefunden!");
						return true;
					}
					if (Vanish.Instance().isVanish(op)) {
						Vanish.Instance().unVanish(op);
						cs.sendMessage(prefix + "Der Offlinespieler "
								+ BlocksVanish.sc + op.getName()
								+ BlocksVanish.mc + " wurde unvanished!");
					} else {
						cs.sendMessage(prefix + "Der Offlinespieler "
								+ BlocksVanish.sc + op.getName()
								+ BlocksVanish.mc + " war nicht im Vanish!");
					}
					return true;
				}
				if (!Vanish.Instance().canVanish(p)) {
					cs.sendMessage("Dieser Spieler kann kein Vanish!");
					return true;
				}
			} else {
				/*
				 * ->/unvanish
				 */
				if (!(cs instanceof Player)) {
					cs.sendMessage(prefix + "Nein Console!");
					return true;
				}
				p = (Player) cs;
			}

			/*
			 * überprüfen ob der Spieler Vanish ist... wenn nicht -> Nachricht
			 * ausgeben dass er es schon war
			 */
			if (Vanish.Instance().isVanish(p)) {
				Vanish.Instance().unVanish(p);
			} else {
				if (p != cs)
					cs.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc
							+ p.getName() + BlocksVanish.mc
							+ " war nicht Vanish!");
				else
					cs.sendMessage(prefix + "Du warst nicht Vanish!");
			}
			return true;
		}

		/*
		 * -> /vanish ?.. überprüfen wie viele ARGS es gibt, 0 = Normaler Vanish
		 * Command (Toggle) Mehr = Argumente überprüfen
		 */
		if (args.length == 0) {
			/*
			 * ->/vanish Toggle Vanish
			 */
			if (!(cs instanceof Player)) {
				cs.sendMessage(prefix + "Nein Console!");
				return true;
			}
			Vanish.Instance().toggleVanish((Player) cs);
			return true;

		} else {
			/*
			 * ->/vanish list Liste ausgeben wer alles Vanish ist!
			 */
			if (args[0].equalsIgnoreCase("list")) {
				if (Vanish.vanishedPlayer.size() == 0) {
					cs.sendMessage(prefix + "Zur Zeit sind " + BlocksVanish.sc
							+ "keine" + BlocksVanish.mc + " Spieler vanish!");
					return true;
				}
				if (Bukkit.getServer().getOnlinePlayers().length == 0) {
					cs.sendMessage(prefix + "Zur Zeit ist" + BlocksVanish.sc
							+ " niemand " + BlocksVanish.mc + "Online!");
					return true;
				}

				String mehrVanish = Vanish.vanishedPlayer.size() > 1 ? BlocksVanish.mc
						+ "sind "
						+ BlocksVanish.sc
						+ Vanish.vanishedPlayer.size()
						: "ist " + BlocksVanish.sc + "einer";
				String mehrOnline = Bukkit.getServer().getOnlinePlayers().length > 1 ? BlocksVanish.sc
						+ Bukkit.getServer().getOnlinePlayers().length
						+ BlocksVanish.mc + " Spielern"
						: BlocksVanish.sc + "einem " + BlocksVanish.mc
								+ "Spieler";

				cs.sendMessage(prefix + "Zur Zeit " + mehrVanish
						+ BlocksVanish.mc + " von " + mehrOnline
						+ BlocksVanish.mc + " im Vanish:");
				String vanishList = null;
				for (String s : Vanish.vanishedPlayer) {
					Player p1 = Bukkit.getServer()
							.getPlayer(UUID.fromString(s));
					if (p1 != null) {
						s = BlocksVanish.getPexPrefix(p1);
					} else {
						OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(
								UUID.fromString(s));
						if (op == null)
							continue;
						s = "§8" + op.getName();
					}
					if (vanishList == null) {
						vanishList = s;
					} else {
						vanishList = vanishList + BlocksVanish.mc + ", " + s;
					}
				}
				cs.sendMessage(vanishList + BlocksVanish.mc + ".");
				return true;
			}

			/*
			 * ->/vanish all Vanish von allen Spielern entfernen
			 */
			if (args[0].equalsIgnoreCase("all")) {
				Vanish.Instance().unVanishAll();
				cs.sendMessage(prefix + "Es wurden alle sichtbar gemacht!");
				return true;
			}
			/*
			 * ->/vanish on Vanish einschalten
			 */
			if (args[0].equalsIgnoreCase("on")) {
				if (args.length == 2) {
					/*
					 * ->/vanish on <Player> Einem Spieler Vanish einschalten
					 */
					p = Bukkit.getServer().getPlayer(args[1]);
					if (p == null) {
						cs.sendMessage(prefix + "Der Spieler "
								+ BlocksVanish.sc + args[1] + BlocksVanish.mc
								+ " wurde nicht gefunden!");
						return true;
					}
					if (!Vanish.Instance().canVanish(p)) {
						cs.sendMessage("Dieser Spieler kann kein Vanish!");
						return true;
					}
				} else if (args.length == 1) {
					/*
					 * ->/vanish on Sich selber Vanishen
					 */
					if (!(cs instanceof Player)) {
						cs.sendMessage(prefix + "Nein Console!");
						return true;
					}
					p = (Player) cs;
				} else {
					/*
					 * ->/vanish on * *... Usage ausgeben wenn zuviele Argumente
					 */
					cs.sendMessage(prefix + "/vanish on <Player>");
					return true;
				}
				if (Vanish.Instance().isVanish(p)) {
					cs.sendMessage(prefix + "Du bist schon Vanish!");
				} else {
					Vanish.Instance().doVanish(p);
				}
				return true;
			}
			/*
			 * ->/vanish off ?
			 */
			if (args[0].equalsIgnoreCase("off")) {
				if (args.length == 2) {
					/*
					 * ->/vanish off <Player> Einem Spieler Vanish ausschalten
					 */
					p = Bukkit.getServer().getPlayer(args[1]);
					if (p == null) {
						cs.sendMessage(prefix + "Der Spieler "
								+ BlocksVanish.sc + args[1] + BlocksVanish.mc
								+ " wurde nicht gefunden!");
						return true;
					}
					if (!Vanish.Instance().canVanish(p)) {
						cs.sendMessage("Dieser Spieler kann kein Vanish!");
						return true;
					}
				} else if (args.length == 1) {
					/*
					 * ->/vanish off Sich selber UnVanishen
					 */
					if (!(cs instanceof Player)) {
						cs.sendMessage(prefix + "Nein Console!");
						return true;
					}
					p = (Player) cs;
				} else {
					/*
					 * ->/vanish off * *... Usage ausgeben wenn zuviele
					 * Argumente
					 */
					cs.sendMessage(prefix + "/vanish off <Player>");
					return true;
				}
				if (!(cs instanceof Player)) {
					cs.sendMessage(prefix + "Nein Console!");
					return true;
				}
				if (!(Vanish.Instance().isVanish(p))) {
					cs.sendMessage(prefix + "Du bist schon Vanish!");
				} else {
					Vanish.Instance().unVanish(p);
				}
				return true;
			}
			/*
			 * Wenn das zweite Argument nicht "list", "on", "off" oder "all" ist
			 * ->/vanish <Player> ? überprüfen ob Spieler null ist überprüfen ob
			 * der Spieler Vanish kann überprüfe weitere Argumente (off/on)
			 */
			Player playa = Bukkit.getPlayer(args[0]);
			if (playa == null) {
				cs.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc
						+ args[0] + BlocksVanish.mc + " wurde nicht gefunden!");
				return true;
			}
			if (!Vanish.Instance().canVanish(playa)) {
				cs.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc
						+ BlocksVanish.getPexPrefix(playa) + BlocksVanish.mc
						+ " kann kein Vanish!");
				return true;
			}
			if (args.length == 1) {
				Vanish.Instance().toggleVanish(playa);
				if (Vanish.vanishedPlayer.contains(playa.getUniqueId() + "")) {
					cs.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc
							+ BlocksVanish.getPexPrefix(playa)
							+ BlocksVanish.mc + " ist nun Vanish!");
				} else {
					cs.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc
							+ BlocksVanish.getPexPrefix(playa)
							+ BlocksVanish.mc + " ist nun nicht mehr Vanish!");
				}
				return true;
			} else if (args.length == 2) {
				if (args[1].equalsIgnoreCase("on")) {
					Vanish.Instance().doVanish(playa);
					cs.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc
							+ BlocksVanish.getPexPrefix(playa)
							+ BlocksVanish.mc + " ist jetzt Vanish!");
					return true;
				}
				if (args[1].equalsIgnoreCase("off")) {
					Vanish.Instance().unVanish(playa);
					cs.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc
							+ BlocksVanish.getPexPrefix(playa)
							+ BlocksVanish.mc + " ist nicht mehr Vanish!");
					return true;
				}

				cs.sendMessage(prefix + "/vanish [Player] [on/off]");
				return true;
			} else {
				cs.sendMessage(prefix + "/vanish [Player] [on/off]");
				return true;
			}
		}
	}
}
