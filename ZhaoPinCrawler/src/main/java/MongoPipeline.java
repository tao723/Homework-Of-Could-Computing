import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.net.URLDecoder;

public class MongoPipeline implements Pipeline {

    private static final String ALIYUN = "47.100.88.174";
    private static final String LOCAL = "localhost";

    private MongoClient mongoClient = new MongoClient(ALIYUN,27017);
    private MongoDatabase mongoDatabase = mongoClient.getDatabase("zhilian2");
    private MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("base");

    public void process(ResultItems resultItems, Task task) {
        Iterator it = resultItems.getAll().entrySet().iterator();
        Document document = new Document();
        while(it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)it.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.compareTo("keyWord")==0){
                try {
                    value = URLDecoder.decode((String)value,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            document.append(key,value);
        }
        if(!document.isEmpty()) mongoCollection.insertOne(document);
    }
}
