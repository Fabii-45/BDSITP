package application;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static java.util.Arrays.asList;

public class ConnexionMongo {

    public static void main(String[] args) {
        try {
            //Changer l'adresse en fonction du docker inspect ipv4
            MongoClient mongoClient = new MongoClient( "172.18.0.2", 27017 );
            MongoDatabase database = mongoClient.getDatabase("cine");

            MongoIterable<String> list = database.listCollectionNames();
            /*
            for (String name : list) {
                System.out.println(name);
            }
            */

            MongoCollection<Document> collectionFilms = database.getCollection("movies");
            MongoCollection<Document> collectionArtists = database.getCollection("artists");
            /** Question1 **/
            //System.out.println(collectionFilms.countDocuments());
            //System.out.println(collectionArtists.countDocuments());

            /** Question 2 **/
            //BasicDBObject fields = new BasicDBObject().append("film", 1); // SELECT name
            //BasicDBObject query = new BasicDBObject().append("film", "Jon"); // WHERE name = "Jon"
            FindIterable<Document> results = collectionFilms.find(); // FROM yourCollection
            for (Document resu2 : results) {
                //System.out.println(resu2);
            }
            FindIterable<Document> results2artists = collectionArtists.find(); // FROM yourCollection
            for (Document resu2artists : results2artists) {
                //System.out.println(resu2artists);
            }

            /** Question 3 **/
            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put("genre", "Science-fiction");
            FindIterable<Document> results3 = collectionFilms.find(whereQuery);
            for (Document res3 : results3) {
                //System.out.println(resu3.getString("title"));
            }

            /** Question 4 **/
            DistinctIterable<String> results4 = collectionFilms.distinct("genre", String.class);
            for (String resu4 : results4){
                //System.out.println(resu4);
            }

            /** Question 5 **/
            BasicDBObject whereQuery5 = new BasicDBObject();
            String idDirector=null;
            whereQuery5.put("last_name", "Coppola");
            whereQuery5.put("first_name", "Francis Ford");
            FindIterable<Document> results5 = collectionArtists.find(whereQuery5);
            for (Document resu5 : results5) {
                //System.out.println(resu5.getString("_id"));
                idDirector=resu5.getString("_id");
            }
            BasicDBObject whereQuery52 = new BasicDBObject();
            Document doc52 = new Document("_id",idDirector);
            whereQuery52.put("director", doc52);
            FindIterable<Document> results52 = collectionFilms.find(whereQuery52);
            for (Document res52 : results52) {
                //System.out.println(res52);
            }

            /** Question 6 **/

            Document role = collectionFilms.aggregate(asList(
                    match(eq("title", "Matrix")),
                    unwind("$actors"),
                    match(eq("actors.role", "Neo")),
                    project(fields(include("actors._id")))
            )).first();
            System.out.println(collectionFilms.aggregate(asList(
                    match(eq("title", "Matrix")),
                    unwind("$actors"),
                    match(eq("actors.role", "Neo")))
            ));
            System.out.println(
                    collectionArtists.find(
                            new BasicDBObject("_id", ((Document) role.get("actors")).get("_id"))).first());

            /** Question 7 **/
            // ID DU BOUG
            Document alPacino = collectionArtists.aggregate(asList(
                    match(and(
                            eq("last_name", "Pacino"),
                            eq("first_name", "Al")
                        )
                    ),
                    project(fields(include("_id")))
            )).first();
            // SES RÔLES GRRR
            System.out.println("Al Pacino ID : "+alPacino.get("_id"));
            collectionFilms.aggregate(asList(
                match(eq("actors._id", alPacino.get("_id"))),
                unwind("$actors"),
                match(eq("actors._id", alPacino.get("_id"))),
                group("$actors.role")
            )
            ).forEach((Block<? super Document>) m-> System.out.println(m.get("_id")));

            /** Question 8 **/
            Document toyStory = new Document();
            //toyStory.put("title", "Toy Story");
            //toyStory.put("year", "1879");
            //collectionFilms.insertOne(toyStory);

           collectionFilms.find().forEach((Block<? super Document>) f-> System.out.println(f));
           collectionArtists.find().forEach((Block<? super Document>) f-> System.out.println(f));


           /**Question 9**/
/*
            Document shackNicholson = new Document();
            shackNicholson.put("_id", "artist:190");
            shackNicholson.put("last_name", "Nicholson");
            shackNicholson.put("first_name", "Shack");

            collectionArtists.insertOne(shackNicholson);


            Document volAuDessus = new Document();
            volAuDessus.put("title", "Vol au dessus d'un nid de coucou");


            List<Document> actors = new ArrayList<>();
            Document actorJack = new Document();
            actorJack.put("_id", "artist:190");
            actorJack.put("role", "Voleur");
            actors.add(actorJack);
            volAuDessus.put("actors", actors);

            collectionFilms.insertOne(volAuDessus);
*/


            mongoClient.close();
        } catch (Exception e){
            System.err.println(e);
        }
    }
}
