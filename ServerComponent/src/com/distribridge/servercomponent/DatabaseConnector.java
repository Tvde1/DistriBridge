package com.distribridge.servercomponent;

import com.distribridge.shared.Constants;
import com.distribridge.shared.models.Stats;
import com.distribridge.shared.models.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

class DatabaseConnector {
    MongoClient _client;
    MongoDatabase _database;
    MongoCollection<Document> _users;

    DatabaseConnector() {
        this(false);
    }

    DatabaseConnector(Boolean init) {

        MongoClientURI uri  = new MongoClientURI(Constants.DATABASE_URL);
        _client = new MongoClient(uri);
        _database = _client.getDatabase(uri.getDatabase());

        if (init) {
            _database.drop();
            _database.createCollection("users");
        }

        _users = _database.getCollection("users");
    }

    User createUser(String username, String password) {
        Document found = _users.find(eq("username", username)).first();

        if (found != null) {
            return null;
        }

        Document doc = new Document()
                .append("username", username)
                .append("password", password)
                .append("stats", new Document()
                        .append("wins", 0)
                        .append("losses", 0)
                        .append("logins", 0));

        _users.insertOne(doc);
        return new User(username, Stats.Empty());
    }

    User getUser(String username) {
        Document found = _users.find(eq("username", username)).first();
        return DocumentToUser(found);
    }

    void editUser(User user) {
        Bson query = eq("username", user.getUsername());
        Document found = _users.find(query).first();
        if (found == null) {
            return;
        }

        Bson newStats = new Document("stats", new Document()
            .append("wins", user.getStats().getWins())
            .append("losses", user.getStats().getLosses())
            .append("logins", user.getStats().getLogins()));

        _users.updateOne(query, new Document("$set", newStats));
    }

    User getUserWithCredentials(String username, String password) {
        Document found = _users.find(and(eq("username", username), eq("password", password))).first();
        return DocumentToUser(found);
    }

    private static User DocumentToUser(Document d) {
        if (d == null) {
            return null;
        }
        Document dStats = d.get("stats", Document.class);
        Stats stats = new Stats(dStats.getInteger("wins"), dStats.getInteger("losses"), dStats.getInteger("logins"));
        return new User(d.getString("username"), stats);
    }
}
