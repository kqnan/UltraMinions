package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.Main;
import io.github.Leonardo0013YT.UltraMinions.enums.MinionType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HologramManager {

    private final HashMap<MinionType, ArrayList<String>> noCorrect = new HashMap<>();
    private final HashMap<MinionType, ArrayList<String>> fully = new HashMap<>();
    private final HashMap<MinionType, ArrayList<String>> lowFood = new HashMap<>();
    private final HashMap<MinionType, ArrayList<String>> lowHealth = new HashMap<>();
    private final HashMap<MinionType, ArrayList<String>> sleeping = new HashMap<>();
    private final HashMap<MinionType, ArrayList<String>> noHealthNoFood = new HashMap<>();
    private final HashMap<MinionType, ArrayList<String>> social = new HashMap<>();
    private final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");
    private final Main plugin;

    public HologramManager(Main plugin) {
        this.plugin = plugin;
    }

    public String replaceColors(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        if (Bukkit.getVersion().contains("1.16")) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String color = text.substring(matcher.start(), matcher.end());
                text = text.replace(color, "" + ChatColor.of(color.toUpperCase()));
            }
        }
        return text;
    }

    public void reload() {
        noCorrect.clear();
        fully.clear();
        noHealthNoFood.clear();
        lowFood.clear();
        lowHealth.clear();
        sleeping.clear();
        social.clear();
        for (MinionType t : MinionType.values()) {
            if (!noCorrect.containsKey(t)) {
                noCorrect.put(t, new ArrayList<>());
            }
            if (!fully.containsKey(t)) {
                fully.put(t, new ArrayList<>());
            }
            if (!social.containsKey(t)) {
                social.put(t, new ArrayList<>());
            }
            if (!lowHealth.containsKey(t)) {
                lowHealth.put(t, new ArrayList<>());
            }
            if (!lowFood.containsKey(t)) {
                lowFood.put(t, new ArrayList<>());
            }
            if (!noHealthNoFood.containsKey(t)) {
                noHealthNoFood.put(t, new ArrayList<>());
            }
            if (!sleeping.containsKey(t)) {
                sleeping.put(t, new ArrayList<>());
            }
            for (String msg : plugin.getLang().get("holograms.noCorrect." + t.name().toLowerCase()).split("\\n")) {
                noCorrect.get(t).add(msg);
            }
            for (String msg : plugin.getLang().get("holograms.fully." + t.name().toLowerCase()).split("\\n")) {
                fully.get(t).add(msg);
            }
            for (String msg : plugin.getLang().get("holograms.lowHealth." + t.name().toLowerCase()).split("\\n")) {
                lowHealth.get(t).add(msg);
            }
            for (String msg : plugin.getLang().get("holograms.lowFood." + t.name().toLowerCase()).split("\\n")) {
                lowFood.get(t).add(msg);
            }
            for (String msg : plugin.getLang().get("holograms.noHealthNoFood." + t.name().toLowerCase()).split("\\n")) {
                noHealthNoFood.get(t).add(msg);
            }
            for (String msg : plugin.getLang().get("holograms.sleeping." + t.name().toLowerCase()).split("\\n")) {
                sleeping.get(t).add(msg);
            }
            for (String msg : plugin.getLang().get("holograms.social." + t.name().toLowerCase()).split("\\n")) {
                social.get(t).add(msg);
            }
        }
    }

    public ArrayList<String> getSleepingMessage(MinionType type) {
        return sleeping.get(type);
    }

    public ArrayList<String> getNoCorrectMessage(MinionType type) {
        return noCorrect.get(type);
    }

    public ArrayList<String> getFullyMessage(MinionType type) {
        return fully.get(type);
    }

    public ArrayList<String> getLowFoodMessage(MinionType type) {
        return lowFood.get(type);
    }

    public ArrayList<String> getLowHealthMessage(MinionType type) {
        return lowHealth.get(type);
    }

    public ArrayList<String> getNoHealthNoFood(MinionType type) {
        return noHealthNoFood.get(type);
    }

    public ArrayList<String> getSocialMessage(MinionType type) {
        int i = ThreadLocalRandom.current().nextInt(0, social.get(type).size());
        if (i == social.get(type).size()) {
            i--;
        }
        return new ArrayList<>(Collections.singletonList(social.get(type).get(i)));
    }

}