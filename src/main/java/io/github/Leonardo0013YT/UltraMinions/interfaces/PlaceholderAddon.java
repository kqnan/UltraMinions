package io.github.Leonardo0013YT.UltraMinions.interfaces;

import org.bukkit.entity.Player;

import java.util.List;

public interface PlaceholderAddon {

    String parsePlaceholders(Player p, String value);

    List<String> parsePlaceholders(Player p, List<String> value);

}