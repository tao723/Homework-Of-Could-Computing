
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

import static com.mongodb.client.model.Filters.ne;

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
            MongoDatabase mongoDatabase = mongoClient.getDatabase("zhilian3");
            MongoCollection<Document> collection = mongoDatabase.getCollection("basel");
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();

            String str1="";
            String str2="";
            String FilePath1="C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\searchkeyword\\MongodbData";
            String FilePath2="C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\city\\MongodbData";
            int i1=0;
            int i2=0;
            //File f=new File("C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\MongodbData3.txt");
            //FileWriter fw=new FileWriter(f);
            while(mongoCursor.hasNext()){
                Document studentDocument = mongoCursor.next();
                //System.out.println(studentDocument);
                String s=studentDocument.getString("keyWord");
                String c=studentDocument.getString("city");
                str1=str1+s+ " ";
                str2=str2+c+ " ";
                //System.out.println(studentDocument.getString("keyWord") +" " );
                //System.out.println(mongoCursor.next());

                if(str1.length()>=500){
                    //str=str.substring(0,str.length()-1);
                    FilePath1=FilePath1+i1+".txt";
                    File f=new File(FilePath1);
                    FileWriter fw=new FileWriter(f);
                    fw.write(str1);
                    fw.flush();
                    System.out.println(str1);
                    System.out.println("-----------------------------------------------------------------------");
                    str1="";
                    FilePath1="C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\searchkeyword\\MongodbData";
                    i1++;
                }
                if(str2.length()>=500){
                    //str=str.substring(0,str.length()-1);
                    FilePath2=FilePath2+i2+".txt";
                    File f=new File(FilePath2);
                    FileWriter fw=new FileWriter(f);
                    fw.write(str2);
                    fw.flush();
                    System.out.println(str2);
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    str2="";
                    FilePath2="C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\city\\MongodbData";
                    i2++;
                }
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}







