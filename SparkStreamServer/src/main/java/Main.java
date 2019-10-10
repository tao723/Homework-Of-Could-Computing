
//读取mongodb上某个数据库里集合的所有数据
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Main {

    public static void main(String[] args) {
        System.out.println("Start Main!");
        try
        {
            //读取某字段的所有值
            /*
             * MongoClient               连接服务器
             * MongoDatabase             连接数据库
             * MongoCollection           连接表
             * FindIterable<Document>    记录型迭代器
             * MongoCursor               记录游标
             * 应用顺序： 服务器-->数据库-->表-->记录迭代器-->记录游标
             */
            MongoClient mongoClient = new MongoClient("47.100.88.174", 27017);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("zhilian2");
            MongoCollection<Document> collection = mongoDatabase.getCollection("base");
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();

            String str="";
            String FilePath="C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\MongodbData";
            int i=0;
            //File f=new File("C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\MongodbData3.txt");
            //FileWriter fw=new FileWriter(f);
            while(mongoCursor.hasNext()){
                Document studentDocument = mongoCursor.next();
                String s=studentDocument.getString("keyWord");
                str=str+s+ " ";
                //System.out.println(studentDocument.getString("keyWord") +" " );
                //System.out.println(mongoCursor.next());

                if(str.length()>=1000){
                    //str=str.substring(0,str.length()-1);
                    FilePath=FilePath+i+".txt";
                    File f=new File(FilePath);
                    FileWriter fw=new FileWriter(f);
                    fw.write(str);
                    fw.flush();
                    System.out.println(str);
                    //System.out.println("-----------------------------------------------------------------------");
                    str="";
                    FilePath="C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\MongodbData";
                    i++;
                }
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}







