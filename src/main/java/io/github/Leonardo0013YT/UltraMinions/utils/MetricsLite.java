package io.github.Leonardo0013YT.UltraMinions.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

/**
 * bStats collects some data for plugin authors.
 * <p>
 * Check out https://bStats.org/ to learn more about bStats!
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MetricsLite {

    public static final int B_STATS_VERSION = 1;
    private static final String URL = "https://bStats.org/submitData/bukkit";
    private static boolean logFailedRequests;
    private static boolean logSentData;
    private static boolean logResponseStatusText;
    private static String serverUUID;

    static {
        if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
            final String defaultPackage = new String(
                    new byte[]{'o', 'r', 'g', '.', 'b', 's', 't', 'a', 't', 's', '.', 'b', 'u', 'k', 'k', 'i', 't'});
            final String examplePackage = new String(new byte[]{'y', 'o', 'u', 'r', '.', 'p', 'a', 'c', 'k', 'a', 'g', 'e'});
            if (MetricsLite.class.getPackage().getName().equals(defaultPackage) || MetricsLite.class.getPackage().getName().equals(examplePackage)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }
    }

    private final Plugin plugin;
    private final int pluginId;
    private boolean enabled;

    public MetricsLite(Plugin plugin, int pluginId) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        this.plugin = plugin;
        this.pluginId = pluginId;

        // Get the config file
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Check if the config file exists
        if (!config.isSet("serverUuid")) {

            // Add default values
            config.addDefault("enabled", true);
            // Every server gets it's unique random id.
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            // Should failed request be logged?
            config.addDefault("logFailedRequests", false);
            // Should the sent data be logged?
            config.addDefault("logSentData", false);
            // Should the response text be logged?
            config.addDefault("logResponseStatusText", false);

            // Inform the server owners about bStats
            config.options().header(
                    "bStats collects some data for plugin authors like how many servers are using their plugins.\n" +
                            "To honor their work, you should not disable it.\n" +
                            "This has nearly no effect on the server performance!\n" +
                            "Check out https://bStats.org/ to learn more :)"
            ).copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException ignored) {
            }
        }

        serverUUID = config.getString("serverUuid");
        logFailedRequests = config.getBoolean("logFailedRequests", false);
        enabled = config.getBoolean("enabled", true);
        logSentData = config.getBoolean("logSentData", false);
        logResponseStatusText = config.getBoolean("logResponseStatusText", false);
        if (enabled) {
            boolean found = false;
            for (Class<?> service : Bukkit.getServicesManager().getKnownServices()) {
                try {
                    service.getField("B_STATS_VERSION"); // Our identifier :)
                    found = true; // We aren't the first
                    break;
                } catch (NoSuchFieldException ignored) {
                }
            }
            Bukkit.getServicesManager().register(MetricsLite.class, this, plugin, ServicePriority.Normal);
            if (!found) {
                startSubmitting();
            }
        }
    }

    private static void sendData(Plugin plugin, JsonObject data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalAccessException("This method must not be called from the main thread!");
        }
        if (logSentData) {
            plugin.getLogger().info("Sending data to bStats: " + data);
        }
        HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();

        // Compress the data to save bandwidth
        byte[] compressedData = compress(data.toString());

        // Add headers
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip"); // We gzip our request
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
        connection.setRequestProperty("User-Agent", "MC-Server/" + B_STATS_VERSION);

        // Send data
        connection.setDoOutput(true);
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(compressedData);
        }

        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        }

        if (logResponseStatusText) {
            plugin.getLogger().info("Sent data to bStats and received response: " + builder);
        }
    }

    private static byte[] compress(final String str) throws IOException {
        if (str == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
        }
        return outputStream.toByteArray();
    }

    public boolean isEnabled() {
        return enabled;
    }

    private void startSubmitting() {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!plugin.isEnabled()) {
                    timer.cancel();
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> submitData());
            }
        }, 1000 * 60 * 5, 1000 * 60 * 30);
    }

    public JsonObject getPluginData() {
        JsonObject data = new JsonObject();

        String pluginName = plugin.getDescription().getName();
        String pluginVersion = plugin.getDescription().getVersion();

        data.addProperty("pluginName", pluginName); // Append the name of the plugin
        data.addProperty("id", pluginId); // Append the id of the plugin
        data.addProperty("pluginVersion", pluginVersion); // Append the version of the plugin
        data.add("customCharts", new JsonArray());

        return data;
    }

    private JsonObject getServerData() {
        // Minecraft specific data
        int playerAmount;
        try {
            // Around MC 1.8 the return type was changed to a collection from an array,
            // This fixes java.lang.NoSuchMethodError: org.bukkit.Bukkit.getOnlinePlayers()Ljava/util/Collection;
            Method onlinePlayersMethod = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers");
            playerAmount = onlinePlayersMethod.getReturnType().equals(Collection.class)
                    ? ((Collection<?>) onlinePlayersMethod.invoke(Bukkit.getServer())).size()
                    : ((Player[]) onlinePlayersMethod.invoke(Bukkit.getServer())).length;
        } catch (Exception e) {
            playerAmount = Bukkit.getOnlinePlayers().size(); // Just use the new method if the Reflection failed
        }
        int onlineMode = Bukkit.getOnlineMode() ? 1 : 0;
        String bukkitVersion = Bukkit.getVersion();
        String bukkitName = Bukkit.getName();

        // OS/Java specific data
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        int coreCount = Runtime.getRuntime().availableProcessors();

        JsonObject data = new JsonObject();

        data.addProperty("serverUUID", serverUUID);

        data.addProperty("playerAmount", playerAmount);
        data.addProperty("onlineMode", onlineMode);
        data.addProperty("bukkitVersion", bukkitVersion);
        data.addProperty("bukkitName", bukkitName);

        data.addProperty("javaVersion", javaVersion);
        data.addProperty("osName", osName);
        data.addProperty("osArch", osArch);
        data.addProperty("osVersion", osVersion);
        data.addProperty("coreCount", coreCount);

        return data;
    }

    private void submitData() {
        final JsonObject data = getServerData();

        JsonArray pluginData = new JsonArray();
        // Search for all other bStats Metrics classes to get their plugin data
        for (Class<?> service : Bukkit.getServicesManager().getKnownServices()) {
            try {
                service.getField("B_STATS_VERSION"); // Our identifier :)

                for (RegisteredServiceProvider<?> provider : Bukkit.getServicesManager().getRegistrations(service)) {
                    try {
                        Object plugin = provider.getService().getMethod("getPluginData").invoke(provider.getProvider());
                        if (plugin instanceof JsonObject) {
                            pluginData.add((JsonObject) plugin);
                        } else { // old bstats version compatibility
                            try {
                                Class<?> jsonObjectJsonSimple = Class.forName("org.json.simple.JSONObject");
                                if (plugin.getClass().isAssignableFrom(jsonObjectJsonSimple)) {
                                    Method jsonStringGetter = jsonObjectJsonSimple.getDeclaredMethod("toJSONString");
                                    jsonStringGetter.setAccessible(true);
                                    String jsonString = (String) jsonStringGetter.invoke(plugin);
                                    JsonObject object = new JsonParser().parse(jsonString).getAsJsonObject();
                                    pluginData.add(object);
                                }
                            } catch (ClassNotFoundException e) {
                                // minecraft version 1.14+
                                if (logFailedRequests) {
                                    this.plugin.getLogger().log(Level.SEVERE, "Encountered unexpected exception ", e);
                                }
                            }
                        }
                    } catch (NullPointerException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                    }
                }
            } catch (NoSuchFieldException ignored) {
            }
        }

        data.add("plugins", pluginData);

        // Create a new thread for the connection to the bStats server
        new Thread(() -> {
            try {
                // Send the data
                sendData(plugin, data);
            } catch (Exception e) {
                // Something went wrong! :(
                if (logFailedRequests) {
                    plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + plugin.getName(), e);
                }
            }
        }).start();
    }

}