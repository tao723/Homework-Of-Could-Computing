
import com.mongodb.{BasicDBObject, DBCursor, DBObject}
import com.mongodb.casbah._
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext, rdd}
import java.util.List

import scala.collection.mutable.ArrayBuffer


object Main {
  val MONGO_HOST:String = "47.100.88.174"
  val MONGO_PORT:Int = 27017
  val MONGO_DB_NAME:String = "zhilian3"
  val MONGO_COLLECTION_NAME:String = "base"
  val mongoClient:MongoClient =MongoClient(MONGO_HOST,MONGO_PORT)
  val mongoDatabase:MongoDB=mongoClient.getDB(MONGO_DB_NAME);
  val mongoCollection=mongoDatabase.getCollection(MONGO_COLLECTION_NAME)
  //设置spark环境
  val sparkConf = new SparkConf().setAppName("spark graphX").setMaster("local[2]")
  val sc = new SparkContext(sparkConf)
  var counter=0L
  def main(args:Array[String]): Unit ={
    var dbObjectList:ArrayBuffer[DBObject]=ArrayBuffer()
    mongoCollection.find().limit(10).forEach(x=>dbObjectList.append(x))

    val cities:ArrayBuffer[(String,Int)]=dbObjectList.map(dbobject=>new Tuple2[String,Int](dbobject.get("city").toString,0))
    val eduLevels:ArrayBuffer[(String,Int)]=dbObjectList.map(dbobject=>new Tuple2[String,Int](dbobject.get("eduLevel").toString,1))
    val jobs:ArrayBuffer[(String,Int)]=dbObjectList.map(dbobject=>new Tuple2[String,Int](dbobject.get("keyWord").toString,2))
    val numOfPerson:ArrayBuffer[Int]=dbObjectList.map(dbobject=>dbobject.get("inviteCount").toString.toInt)
    println("-----------------------------")
    println("开始打印1")
    for(i<-cities){
      println(i)
    }
    for(i<-eduLevels){
      println(i)
    }
    for(i<-jobs){
      println(i)
    }



    val searchList:ArrayBuffer[(String,Int)]=ArrayBuffer.concat(cities.distinct,eduLevels.distinct,jobs.distinct)
    val listRDD:RDD[(String,Int)]=sc.parallelize(searchList)
    println("------------------------------------------")
    println("开始打印2：")
    listRDD.foreach(a=>println(a))
    val getLong=()=>{
      counter+=1L
      counter
    }
    val vertexRDD:RDD[(VertexId,(String,Int))]=listRDD.map(new Tuple2[VertexId,(String,Int)](getLong(),_))
    println("-----------------------------------------------")
    println("开始打印3：")
    vertexRDD.foreach(a=>println(a))

    val edgeList:ArrayBuffer[Edge[Int]]=ArrayBuffer[Edge[Int]]()


    for(i<-0 to (cities.size-1)){
      println("开始建立第 "+i+" 条边")
      var cityNum:Long= -1L
      var eduNum:Long= -1L
      var jobNum:Long= -1L

      vertexRDD.foreach(a=>{
        println("要找的是："+cities(i)._1+" "+"实际找到的是:"+a._2._1)
        if(a._2._2==0) {
          println("找到属性为城市的顶点了!!!")
          if (a._2._1.equals(cities(i)._1)) {
            println("找到城市了，顶点ID：" + a._1)
            cityNum = a._1
          }
        }
        println("cityNum:"+cityNum)

        println("要找的是："+jobs(i)._1+" "+"实际找到的是："+a._2._1)
        if(a._2._2==2){
          println("找到属性为职业的顶点了!!!")
          if(a._2._1.equals(jobs(i)._1)) {
            println("找到职业了，顶点ID：" + a._1)
            jobNum = a._1
          }
        }
        println("jobNum:"+jobNum)

        println("要找的是："+eduLevels(i)._1+" "+"实际找到的是："+a._2._1)
        if(a._2._2==1){
          println("找到属性为学历的顶点了!!!")
          if(a._2._1.equals(eduLevels(i)._1)){
            println("找到学历了，顶点ID："+a._1)
            eduNum=a._1
          }
        }
        println("eduNum:"+eduNum)
      })









      println(cityNum+" "+eduNum+" "+jobNum)
      edgeList.append(new Edge[Int](cityNum,eduNum,numOfPerson(i)))
      edgeList.append(new Edge[Int](eduNum,jobNum,numOfPerson(i)))
    }

    val edgeRDD:RDD[Edge[Int]]=sc.parallelize(edgeList)

    val graph:Graph[(String,Int),Int]=Graph(vertexRDD,edgeRDD)
    println("------------------------------------------")
    println("开始打印4：")

    graph.vertices.foreach(a=>println(a))
    graph.edges.foreach(a=>println(a.srcId+" "+a.dstId+" "+a.attr))



  }
}


