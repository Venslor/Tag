package com.gihub.venslore.tag.database;

import com.gihub.venslore.tag.TagPlugin;
import com.gihub.venslore.tag.utilities.chat.Color;
import com.gihub.venslore.tag.utilities.file.ConfigFile;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gihub.venslore.tag.utilities.Manager;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
@Getter
public class MongoManager
extends Manager {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> documentation;
    private MongoCollection<Document> tags;
    private ConfigFile configFile;

    public MongoManager(TagPlugin plugin) {
        super(plugin);
        this.configFile = this.plugin.getDataBase();
    }

    public boolean connect() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        try {
            if (this.configFile.getBoolean("MONGODB.AUTHENTICATION.ENABLED")) {
                MongoCredential credential = MongoCredential.createCredential(this.configFile.getString("MONGODB.AUTHENTICATION.USERNAME"), this.configFile.getString("MONGODB.AUTHENTICATION.DATABASE"), this.configFile.getString("MONGODB.AUTHENTICATION.PASSWORD").toCharArray());
                this.mongoClient = new MongoClient(new ServerAddress(this.configFile.getString("MONGODB.ADDRESS"), this.configFile.getInt("MONGODB.PORT")), Collections.singletonList(credential));
            } else {
                this.mongoClient = new MongoClient(this.configFile.getString("MONGODB.ADDRESS"), this.configFile.getInt("MONGODB.PORT"));
            }
            this.mongoDatabase = this.mongoClient.getDatabase(this.configFile.getString("MONGODB.DATABASE"));
            this.documentation = this.mongoDatabase.getCollection("playerdata");
            this.tags = this.mongoDatabase.getCollection("tags");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Color.translate("&cDisabling Tags due to issues with mongo database."));
            Bukkit.getServer().getPluginManager().disablePlugin(this.plugin);
            return false;
        }
    }
}

