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

import java.rmi.RemoteException;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

class DatabaseConnector implements IDatabaseConnector {
    private MongoCollection<Document> users;

    DatabaseConnector() {
        this(false);
    }

    DatabaseConnector(Boolean init) {

        MongoClientURI uri = new MongoClientURI(Constants.DATABASE_URL);
        MongoClient client = new MongoClient(uri);
        MongoDatabase database = client.getDatabase(uri.getDatabase());

        if (init) {
            database.drop();
            database.createCollection("users");
        }

        users = database.getCollection("users");
    }

    private static User DocumentToUser(Document d) {
        if (d == null) {
            return null;
        }
        Document dStats = d.get("stats", Document.class);
        Stats stats = new Stats(dStats.getInteger("wins"), dStats.getInteger("losses"), dStats.getInteger("logins"));
        return new User(d.getString("username"), stats);
    }

    public User createUser(String username, String password) {
        Document found = users.find(eq("username", username)).first();

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

        users.insertOne(doc);
        return new User(username, Stats.Empty());
    }

    public User getUser(String username) {
        Document found = users.find(eq("username", username)).first();
        return DocumentToUser(found);
    }

    public void editUser(User user) {
        try {
            Bson query = eq("username", user.getUsername());
            Document found = users.find(query).first();
            if (found == null) {
                return;
            }

            Bson newStats = new Document("stats", new Document()
                    .append("wins", user.getStats().getWins())
                    .append("losses", user.getStats().getLosses())
                    .append("logins", user.getStats().getLogins()));

            users.updateOne(query, new Document("$set", newStats));
        } catch (RemoteException e) {
            Constants.logException(e);
        }
    }

    public User getUserWithCredentials(String username, String password) {
        Document found = users.find(and(eq("username", username), eq("password", password))).first();
        return DocumentToUser(found);
    }
}
