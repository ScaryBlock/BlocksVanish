package lu.blocks.scary_block.blocksvanish;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListener implements Listener {
	
	@EventHandler(ignoreCancelled=true)
	public void onEntityTarget(EntityTargetEvent event) {
		if ((event.getTarget() instanceof Player) && (Vanish.vanishedPlayer.contains(((Player) event.getTarget()).getUniqueId() + "")))
				event.setCancelled(true);
	}
	
	@EventHandler
	public void onJump(PlayerInteractEvent event){
		if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL){
			event.setCancelled(true); 
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String prefix = BlocksVanish.pc;

		if (Vanish.Instance().canVanish(p)) {
			if (!(Vanish.Instance().isVanish(p))) {
				EssentialsUtils.Instance().setVanish(p, false);
				for (Player players : Bukkit.getOnlinePlayers()) {
					players.showPlayer(p);
					continue;
				}
			} else {
				EssentialsUtils.Instance().setVanish(p, true);
				Vanish.Instance().setVanishScoreboard(p);
				p.sendMessage(prefix
						+ "Du bist im Vanish gejoint!");
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (Vanish.Instance().canVanish(players)) {
						players.showPlayer(p);
						if(players == p) continue;
						players.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc + BlocksVanish.getPexPrefix(p) + BlocksVanish.mc + " ist im Vanish gejoint!");
						continue;
					}
					players.hidePlayer(p);
					continue;
				}
				e.setJoinMessage(null);
			}
		} else {
			Vanish.Instance().unVanish(p);
			Vanish.Instance().refresh();
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		String prefix = BlocksVanish.pc;
		Player p = e.getPlayer();
		
		if (Vanish.Instance().isVanish(p)){
			for(Player players : Bukkit.getOnlinePlayers()){
				if(Vanish.Instance().canVanish(players))
					players.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc + BlocksVanish.getPexPrefix(p) + BlocksVanish.mc + " hat das Spiel im Vanish verlassen!");
				continue;
			}
			e.setQuitMessage(null);
		}
	}

	@EventHandler
	public void PlayerKickEvent(PlayerKickEvent e) {
		String prefix = BlocksVanish.pc;
		Player p = e.getPlayer();
		
		if (Vanish.Instance().isVanish(p)){
			for(Player players : Bukkit.getOnlinePlayers()){
				if(Vanish.Instance().canVanish(players))
					players.sendMessage(prefix + "Der Spieler " + BlocksVanish.sc + BlocksVanish.getPexPrefix(p) + BlocksVanish.mc + " hat das Spiel im Vanish verlassen!");
				continue;
			}
			e.setLeaveMessage(null);
		}
	}
}
