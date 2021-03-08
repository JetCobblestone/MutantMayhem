package net.jetcobblestone.minigameplugin.games.mutantmayhem;

import lombok.Getter;
import net.jetcobblestone.minigameplugin.assets.customdamage.DamageablePlayer;
import net.jetcobblestone.minigameplugin.assets.game.player.GamePlayer;
import net.jetcobblestone.minigameplugin.assets.game.player.PlayerManager;
import net.jetcobblestone.minigameplugin.assets.util.Pair;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.AbstractKit;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.ClassItem;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.itemability.Ability;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class MutantMayhemPlayer extends GamePlayer implements Listener {

    @Getter private AbstractKit kit;
    @Getter private final Map<ClassItem, Pair<Ability, Boolean>> abilityCooldownCache = new HashMap<>();
    @Getter private int kills = 0;
    @Getter private final DamageablePlayer damageablePlayer;

    public MutantMayhemPlayer(DamageablePlayer damageablePlayer, Game game, PlayerManager playerManager) {
        super(damageablePlayer.getLivingEntity(), game, playerManager);
        this.damageablePlayer = damageablePlayer;
    }

    public void makeKill() {
        kills++;
    }

    public void setKit(AbstractKit kit) {
        this.kit = kit;
    }


}
