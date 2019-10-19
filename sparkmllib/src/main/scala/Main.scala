import com.mongodb.DBObject
import com.mongodb.casbah.{MongoClient, MongoDB}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object Main {
  val MONGO_HOST:String = "localhost"
  val MONGO_PORT:Int = 27017
  val MONGO_DB_NAME:String = "zhilian3"
  val MONGO_COLLECTION_NAME:String = "basel"
  val mongoClient:MongoClient =MongoClient(MONGO_HOST,MONGO_PORT)
  val mongoDatabase:MongoDB=mongoClient.getDB(MONGO_DB_NAME);
  val mongoCollection=mongoDatabase.getCollection(MONGO_COLLECTION_NAME)
  //设置spark环境
  val sparkConf = new SparkConf().setAppName("spark graphX").setMaster("local[2]")
  val sc = new SparkContext(sparkConf)
  sc.setLogLevel("ERROR")
  def main(args:Array[String]): Unit ={

    ar dbObjectList:ArrayBuffer[DBObject]=ArrayBuffer()
    mongoCollection.find().forEach(x=>dbObjectList.append(x))


  }
}
