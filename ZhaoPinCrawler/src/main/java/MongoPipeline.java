import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Iterator;
import java.util.Map;

public class MongoPipeline implements Pipeline {

    private final String ALIYUN = "47.100.88.174";
    private final String LOCAL = "localhost";

    MongoClient mongoClient = new MongoClient(ALIYUN,27017);
    MongoDatabase mongoDatabase = mongoClient.getDatabase("zhilian");
    MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("base");

    public void process(ResultItems resultItems, Task task) {
        Iterator it = resultItems.getAll().entrySet().iterator();
        Document document = new Document();
        while(it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)it.next();
            document.append(entry.getKey(),entry.getValue());
        }
        if(!document.isEmpty()) mongoCollection.insertOne(document);
    }
}
