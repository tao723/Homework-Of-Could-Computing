//读取mongodb上某个数据库里集合的所有数据
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Main {

    public static void main(String[] args) {

        try {

            Mongo mongo = new Mongo("47.100.88.174", 27017);

            DB db = mongo.getDB("zhilian");

            DBCollection collection = db.getCollection("base");

            BasicDBObject employee = new BasicDBObject();
            //employee.put("name", "Hannah");
            //employee.put("no", 2);

            //collection.insert(employee);

            BasicDBObject searchEmployee = new BasicDBObject();
            //searchEmployee.put("no", 2);

            DBCursor cursor = collection.find(searchEmployee);

            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }

            System.out.println("The Search Query has Executed!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
             */
/** MongoClient               连接服务器
             * MongoDatabase             连接数据库
             * MongoCollection           连接表
             * FindIterable<Document>    记录型迭代器
             * MongoCursor               记录游标
             * 应用顺序： 服务器-->数据库-->表-->记录迭代器-->记录游标*//*



            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("company");
            MongoCollection<Document> collection = mongoDatabase.getCollection("employees");
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();

             // 游标滚动-->获取记录-->读取字段值


            while(mongoCursor.hasNext()){
                Document studentDocument = mongoCursor.next();
                System.out.println(studentDocument.getString("name") +", " );
                //System.out.println(mongoCursor.next());
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

    }
}
*/
