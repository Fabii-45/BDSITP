package application;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

import static com.mongodb.MongoClient.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ConnexionMongoPojo {
    /*
    public record Movie(
            String title,
            Integer year,
            String genre,
            String summary,
            String country,
            Artist director,
            List<Artist> actors) {
    }
    public record Artist(
            String title,
            String genre,
            String summary,
            String country {
    }*/

    public static void main(String[] args) {
        String bdd = "mongodb://172.18.0.2:27017";
        MongoClient mongoClient = MongoClients.create(bdd);
        //Connexion to cine
        MongoDatabase database = mongoClient.getDatabase("cine");
        //Recup collection movies et artists
        MongoCollection<Document> movies = database.getCollection("movies");
        MongoCollection<Document> artists = database.getCollection("artists");


        System.out.println(movies.find().first());
        //MongoCollection<Movie> movies =
          //      database.getCollection("movies", Movie.class);


        //System.out.println(movies.countDocuments());

    }
}
