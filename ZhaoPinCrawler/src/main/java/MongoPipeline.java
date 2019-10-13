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

/**
 * 数据过滤以及插入数据库
 * @author debonet
 */
public class MongoPipeline implements Pipeline {

    private static final String ALIYUN = "47.100.88.174";
    private static final String LOCAL = "localhost";

    private MongoClient mongoClient = new MongoClient(ALIYUN,27017);
    private MongoDatabase mongoDatabase = mongoClient.getDatabase("zhilian3");
    //不过滤链家公司的数据集合
    private MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("base");
    //过滤链家公司的数据集合
    private MongoCollection<Document> mongoCollection2 = mongoDatabase.getCollection("basel");

    public void process(ResultItems resultItems, Task task) {
        Iterator it = resultItems.getAll().entrySet().iterator();
        Document document = new Document();
        boolean baselInsert = true;
        while(it.hasNext()) {
            //是否插入mongoCollection2
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
            else if(key.compareTo("companyName")==0){
                if(value.toString().contains("链家"))baselInsert = false;
            }
            else if(key.compareTo("inviteCount")==0){
                int renIdx = value.toString().indexOf("人");
                if(value.toString().contains("若干"))value=3;
                else if(renIdx!=-1){
                    value = value.toString().substring(0,renIdx);
                    value = Integer.parseInt((String)value);
                }
            }
            document.append(key,value);
        }
        if(!document.isEmpty()) mongoCollection.insertOne(document);
        if(!document.isEmpty() && baselInsert) mongoCollection2.insertOne(document);
    }
}
