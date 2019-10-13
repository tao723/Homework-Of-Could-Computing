import com.mongodb.MongoClient
import com.mongodb.casbah.Imports._
import com.mongodb.client.{FindIterable, MongoCollection, MongoCursor, MongoDatabase}
import org.apache.hadoop.hive.ql.exec.spark.session.SparkSession
import org.apache.spark.sql.{DataFrame, Row}
import com.mongodb.spark.MongoSpark
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.bson.Document
//object ConnectMongodb{
//  def main(args:Array[String]): Unit = {
     //链接到默认主机（localhost）和默认端口号（27017）
    //val mongoClient =  MongoClient()

//    val db = mongoClient("zhilian2")\
//    val col=db("base")
//
//    val allDocs=col.find()
//    allDocs.foreach(println)
//
//
//    val mongoClient = new MongoClient("47.100.88.174", 27017)
//    val mongoDatabase = mongoClient.getDatabase("zhilian2")
//    val collection = mongoDatabase.getCollection("base")
//    val findIterable = collection.find
//    val mongoCursor = findIterable.iterator
//    while (mongoCursor.hasNext) {
//      Document studentDocument = mongoCursor.next()
//      String s = studentDocument.getString("keyWord")
//      String c = studentDocument.getString("city")
//    }



      //  val spark = SparkSession.builder()
//    .master("local")
//    .appName("MongoSparkConnectorIntro")
//    .config("spark.mongodb.input.uri", "mongodb://192.168.177.13/novels.novel")
//    .getOrCreate()
//
//
//
//    val spark = SparkSession.builder()
//      .appName(this.getClass.getName().stripSuffix("$"))
//      .getOrCreate()
//    val inputUri="mongodb://test:pwd123456@192.168.0.1:27017/test.articles"
//    val df = spark.read.format("com.mongodb.spark.sql").options(
//      Map("spark.mongodb.input.uri" -> inputUri,
//        "spark.mongodb.input.partitioner" -> "MongoPaginateBySizePartitioner",
//        "spark.mongodb.input.partitionerOptions.partitionKey"  -> "_id",
//        "spark.mongodb.input.partitionerOptions.partitionSizeMB"-> "32")
//    ).load()
//


//    val spark = SparkSession.builder()
//      .master("local")
//      .appName("MyApp")
//      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/test.user")
//      .getOrCreate()
//
//    // 设置log级别
//    spark.sparkContext.setLogLevel("WARN")
//
//    val df = MongoSpark.load(spark)
//    df.show()
//
//    df.createOrReplaceTempView("user")
//
//    val resDf = spark.sql("select name,age,sex from user")
//    resDf.show()
//
//    spark.stop()
//    System.exit(0)


//  }
//}

