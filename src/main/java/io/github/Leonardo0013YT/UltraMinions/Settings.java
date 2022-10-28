package io.github.Leonardo0013YT.UltraMinions;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    private YamlConfiguration config;
    private File file;
    private Main u;
    private boolean hexColor;

    public Settings(Main u, String s, boolean defaults, boolean hexColor) {
        this.u = u;
        this.hexColor = hexColor;
        this.file = new File(u.getDataFolder(), s + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
        Reader reader = new InputStreamReader(u.getResource(s + ".yml"), StandardCharsets.UTF_8);
        YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(reader);
        try {
            if (!this.file.exists()) {
                this.config.addDefaults(loadConfiguration);
                this.config.options().copyDefaults(true);
                save();
            } else {
                if (defaults) {
                    this.config.addDefaults(loadConfiguration);
                    this.config.options().copyDefaults(true);
                    save();
                }
                this.config.load(this.file);
            }
        } catch (IOException | InvalidConfigurationException ignored) {
        }
    }

    public File getFile() {
        return file;
    }

    public void reload() {
        try {
            this.config.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException ignored) {
        }
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public String get(String s) {
        if (config.getString(s) == null) {
            return "";
        }
        String text = config.getString(s).replaceAll("<arrow>", "➤");
        if (hexColor) {
            return u.getHm().replaceColors(text);
        }
        return text;
    }

    public String getOrDefault(String s, String def) {
        if (config.isSet(s)) {
            return get(s);
        }
        set(s, def);
        save();
        return def;
    }

    public int getInt(String s) {
        return this.config.getInt(s);
    }

    public int getIntOrDefault(String s, int def) {
        if (config.isSet(s)) {
            return getInt(s);
        }
        set(s, def);
        save();
        return def;
    }

    public List<String> getList(String s) {
        return this.config.getStringList(s);
    }

    public List<String> getListOrDefault(String s, ArrayList<String> def) {
        if (config.isSet(s)) {
            return getList(s);
        }
        return def;
    }

    public boolean isSet(String s) {
        return this.config.isSet(s);
    }

    public void set(String s, Object o) {
        this.config.set(s, o);
    }

    public boolean getBoolean(String s) {
        return this.config.getBoolean(s);
    }

    public boolean getBooleanOrDefault(String s, boolean def) {
        if (config.isSet(s)) {
            return getBoolean(s);
        }
        set(s, def);
        save();
        return def;
    }

}
