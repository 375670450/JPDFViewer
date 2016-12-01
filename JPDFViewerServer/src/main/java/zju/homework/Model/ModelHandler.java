package zju.homework.Model;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

/**
 * Created by stardust on 2016/11/29.
 */
public class ModelHandler {

    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> accountCol;
    MongoCollection<Document> groupCol;
    MongoCollection<Document> annotCol;

    public ModelHandler(){
        mongoClient = new MongoClient("localhost");
        database = mongoClient.getDatabase("jpdfserver");
        accountCol = database.getCollection("account");
        groupCol = database.getCollection("groups");
        annotCol = database.getCollection("annotation");

//        if( accountCol == null )
//            database.createCollection("account");
    }

    public void insert(Account account){
        Document document = new Document("email", account.getEmail())
                .append("passwd", account.getPasswd())
                .append("groupId", account.getGroupId());

        accountCol.insertOne(document);
    }

    public void insert(Group group){
        Document document = new Document("groupId", group.getGroupId())
                .append("creator", group.getCreator())
                .append("pdfData", group.getPdfData());
        groupCol.insertOne(document);
    }

    public void insert(Annotation annot){
        Document document = new Document("author", annot.getAuthor())
                .append("data", annot.getData())
                .append("groupId", annot.getGroupId());

        annotCol.insertOne(document);

    }

    public Document findAccount(String email){
        return accountCol.find(Filters.eq("email", email)).first();
    }

    public Document findGroup(String id){
        return groupCol.find(Filters.eq("groupId", id)).first();
    }

    public FindIterable<Document> findAnnotations(String groupId) {
        return annotCol.find(Filters.eq("groupId", groupId));
    }

}
