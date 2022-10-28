package io.github.Leonardo0013YT.UltraMinions.addons;

import io.github.Leonardo0013YT.UltraMinions.interfaces.PlaceholderAddon;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;

public class PlaceholderAPIAddon implements PlaceholderAddon {

    @Override
    public String parsePlaceholders(Player p, String value) {
        return PlaceholderAPI.setPlaceholders(p, value);
    }

    @Override
    public List<String> parsePlaceholders(Player p, List<String> value) {
        return PlaceholderAPI.setPlaceholders(p, value);
    }

}