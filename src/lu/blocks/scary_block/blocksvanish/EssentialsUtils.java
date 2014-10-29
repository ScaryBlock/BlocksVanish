package lu.blocks.scary_block.blocksvanish;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.earth2me.essentials.User;

public class EssentialsUtils {

	private final static EssentialsUtils instance = new EssentialsUtils();

	/**
	 * Jemanden Vanish setzen...
	 * 
	 * @param player
	 *            Spieler der Vanish gesetzt werden soll
	 * @param vanish
	 *            Boolean mit dem Vanishwert..
	 */
	public void setVanish(Player player, boolean vanish) {
		if (BlocksVanish.iess == null)
			return;
		User essuser = BlocksVanish.iess.getUser(player);
		if (essuser == null)
			return;
		essuser.setHidden(vanish);
	}

	/**
	 * Offlinespieler Vanish setzen...
	 * 
	 * @param op
	 *            Der Offlinespieler
	 * @param vanish
	 *            Boolean über Vanishstatus
	 */
	public void setVanish(OfflinePlayer op, boolean vanish) {
		if (BlocksVanish.iess == null)
			return;
		User essuser = BlocksVanish.iess.getUser(op);
		if (essuser == null)
			return;
		essuser.setHidden(vanish);
	}

	/**
	 * Den Vanishstatus von einem Spieler überprüfen
	 * 
	 * @param player
	 *            Spieler dessen Vanish Status überprüft werden soll
	 * @return VanishStatus von Spieler
	 */
	public boolean isVanish(Player player) {
		if (BlocksVanish.iess == null)
			return false;
		User essuser = BlocksVanish.iess.getUser(player);
		if (essuser == null)
			return false;
		return essuser.isHidden();
	}

	public static EssentialsUtils Instance() {
		return EssentialsUtils.instance;
	}

}
