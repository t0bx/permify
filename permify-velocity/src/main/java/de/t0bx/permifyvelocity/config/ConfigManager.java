package de.t0bx.permifyvelocity.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private final Gson gson;
    private final File directory;
    private final File file;
    private JsonObject config;

    public ConfigManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.directory = new File("plugins/Permify/");
        this.file = new File(directory, "config.json");
        loadConfig();
    }

    private void loadConfig() {
        try {
            if (!this.file.exists()) {
                createDefaultConfig();
            } else {
                try (FileReader reader = new FileReader(this.file)) {
                    this.config = this.gson.fromJson(reader, JsonObject.class);
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Fehler beim Laden der Konfiguration", exception);
        }
    }

    public void createDefaultConfig() {
        try {
            if (!this.directory.exists()) {
                this.directory.mkdir();
            }

            if (!this.file.exists()) {
                file.createNewFile();
            }

            JsonObject jsonObject = new JsonObject();

            JsonObject redisObject = new JsonObject();
            redisObject.addProperty("host", "localhost");
            redisObject.addProperty("port", 6379);
            jsonObject.add("redis", redisObject);

            JsonObject databaseObject = new JsonObject();
            databaseObject.addProperty("host", "localhost");
            databaseObject.addProperty("port", 3306);
            databaseObject.addProperty("username", "root");
            databaseObject.addProperty("database", "test");
            databaseObject.addProperty("password", "test");
            jsonObject.add("database", databaseObject);

            this.config = jsonObject;

            try (FileWriter writer = new FileWriter(this.file)) {
                this.gson.toJson(jsonObject, writer);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Fehler beim Erstellen der Standardkonfiguration", exception);
        }
    }

    public String getRedisHost() {
        return this.config.getAsJsonObject("redis").get("host").getAsString();
    }

    public int getRedisPort() {
        return this.config.getAsJsonObject("redis").get("port").getAsInt();
    }

    public String getDatabaseHost() {
        return this.config.getAsJsonObject("database").get("host").getAsString();
    }

    public int getDatabasePort() {
        return this.config.getAsJsonObject("database").get("port").getAsInt();
    }

    public String getDatabaseUsername() {
        return this.config.getAsJsonObject("database").get("username").getAsString();
    }

    public String getDatabaseName() {
        return this.config.getAsJsonObject("database").get("database").getAsString();
    }

    public String getDatabasePassword() {
        return this.config.getAsJsonObject("database").get("password").getAsString();
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(this.file)) {
            this.gson.toJson(this.config, writer);
        } catch (IOException exception) {
            throw new RuntimeException("Fehler beim Speichern der Konfiguration", exception);
        }
    }
}
