import java.io.{DataInputStream, DataOutputStream, File, FileWriter, Writer}
import java.net.ServerSocket
import java.util.Calendar

import com.mongodb.DBObject
import com.mongodb.casbah.{MongoClient, MongoDB}
import org.apache.spark.graphx.{Edge, EdgeTriplet, Graph, PartitionStrategy}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable.ArrayBuffer._

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
  val sparkConf = new SparkConf().setAppName("spark mllib").setMaster("local[2]")
  val sc = new SparkContext(sparkConf)
  sc.setLogLevel("ERROR")
  val jobClass=Array("产品经理","产品专员","产品设计","新媒体运营","文案编辑","内容运营","交互设计","UI设计师","网页设计","大数据工程师","系统设计","数据分析师","研发工程师","软件工程师","测试工程师","前端工程师","硬件工程师","移动开发","人工智能","数据库工程师","运维工程师","游戏开发","通信工程师","项目管理","售前工程师","售后工程师","技术支持","银行柜员","通用岗位","投资经理","理财顾问","银行客户经理","精算师","信托业务","证券经纪人","保险顾问","理赔专员","个人业务","公司业务","信用卡业务","工业工程师","制造工程师","生产经理","电子工程师","电气工程师","自动化工程师","半导体技术","机械工程师","机电工程师","维修工程师","汽车设计","汽车制造","汽车维修、保养","模具工程师","化工工程师","实验室技术员","生物制药","医药技术研发","服装设计","纺织工程师","房地产销售","房地产项目管理","合同管理","建筑工程师","土建工程师","结构工程师","造价工程师","市政工程师","给排水工程师","制冷工程师","暖通工程师","建筑设计师","项目工程师","景观设计","室内设计","装潢设计师","环保工程师","地质工程师","销售代表","电话销售","网络销售","大客户销售","销售业务跟单","招商专员","销售管理","销售支持","售前支持","售后支持","客服专员","网络客服","市场专员","活动策划","活动执行","市场推广","品牌专员","品牌公关","行政专员","前台","文秘","人力资源专员","人事助理","招聘专员","绩效考核专员","薪酬福利专员","出纳","会计师","财务专员","审计专员","税务","法务专员","合规专员","实习生","管培生","储备干部","公务员","咨询顾问","教师","翻译","兼职","其他")
  def main(args:Array[String]): Unit ={

    var dbObjectList:ArrayBuffer[DBObject]=ArrayBuffer()
    mongoCollection.find().forEach(x=>dbObjectList.append(x))

    val cities:ArrayBuffer[(String,Int)]=dbObjectList.map(dbobject=>new Tuple2[String,Int](dbobject.get("city").toString,0))
    val eduLevels:ArrayBuffer[(String,Int)]=dbObjectList.map(dbobject=>new Tuple2[String,Int](dbobject.get("eduLevel").toString,1))
    val jobs:ArrayBuffer[(String,Int)]=dbObjectList.map(dbobject=>new Tuple2[String,Int](getNumOfJobs(dbobject.get("keyWord").toString),2))
    val numOfPerson:ArrayBuffer[Int]=dbObjectList.map(dbobject=>dbobject.get("inviteCount").toString.toInt)



    var arr:ArrayBuffer[Long]=ArrayBuffer()
    for(i<-1 to 1500){
      arr.append(i)
    }
    val iterator=arr.iterator

    val searchList:ArrayBuffer[(String,Int)]=ArrayBuffer.concat(cities.distinct,eduLevels.distinct,jobs.distinct)
    val vertexList:ArrayBuffer[(Long,(String,Int))]=searchList.map(a=>new Tuple2[Long,(String,Int)](iterator.next(),a))

    val vertexRDD:RDD[(Long,(String,Int))]=sc.parallelize(vertexList)

    val edgeList:ArrayBuffer[Edge[Int]]=ArrayBuffer[Edge[Int]]()

    for(i<-0 to (cities.size-1)) {
      var cityNum = searchList.indexOf(cities(i)) + 1
      var eduNum = searchList.indexOf(eduLevels(i)) + 1
      var jobNum = searchList.indexOf(jobs(i)) + 1

      edgeList.append(new Edge[Int](cityNum, eduNum, numOfPerson(i)))
      edgeList.append(new Edge[Int](eduNum, jobNum, numOfPerson(i)))
      edgeList.append(new Edge[Int](cityNum,jobNum,numOfPerson(i)))
    }
    val edgeRDD:RDD[Edge[Int]]=sc.parallelize(edgeList)

    val graph:Graph[(String,Int),Int]=Graph(vertexRDD,edgeRDD)

    //将平行边合并
    val mergedGraph=graph.partitionBy(PartitionStrategy.CanonicalRandomVertexCut).groupEdges((ed1,ed2)=>{
      ed1+ed2
    })

    val noEduGraph=mergedGraph.subgraph(edgeTriplet=>{
      true
    },(verId,VD)=>{
      if(VD._2==1){
        false
      }else{
        true
      }
    })
    val numOfCity:Int=cities.distinct.count(x=>{
      true
    })



    for(i<-cities.distinct){
      val jobClassArr=ArrayBuffer(0,0,0,0,0,0,0)
      val subsubGraph=noEduGraph.subgraph(edgeTriplet=>{
        true
      },(verId,VD)=>{
        if(VD._2==0){
          if(VD._1.equals(i._1)){
            true
          }
          else{
            false
          }
        }else{
          true
        }
      })

      val a1="产品/技术"
      val a2="金融"
      val a3="生产/制造"
      val a4="地产/建筑"
      val a5="销售/市场"
      val a6="职能类"
      val a7="其他"

      val tripletArr=subsubGraph.triplets.collect()
      for (elem <- tripletArr) {
        if(elem.dstAttr._1.equals(a1)){
          jobClassArr(0)=elem.attr
        }
        if(elem.dstAttr._1.equals(a2)){
          jobClassArr(1)=elem.attr
        }
        if(elem.dstAttr._1.equals(a3)){
          jobClassArr(2)=elem.attr
        }
        if(elem.dstAttr._1.equals(a4)){
          jobClassArr(3)=elem.attr
        }
        if(elem.dstAttr._1.equals(a5)){
          jobClassArr(4)=elem.attr
        }
        if(elem.dstAttr._1.equals(a6)){
          jobClassArr(5)=elem.attr
        }
        if(elem.dstAttr._1.equals(a7)){
          jobClassArr(6)=elem.attr
        }
      }
      print(i._1+" ")
      for(j<-0 to 6){
        print(jobClassArr(j)+" ")
      }
      println()
    }
















  }
  def getNumOfJobs(job:String):String={
    var index=jobClass.indexOf(job)

    if(index<=26){
      "产品/技术"
    }else if(index<=39){
      "金融"
    }else if(index<=59){
      "生产/制造"
    }else if(index<=77){
      "地产/建筑"
    }else if(index<=95){
      "销售/市场"
    }else if(index<=110){
      "职能类"
    }else{
      "其他"
    }
  }

}
