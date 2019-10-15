
import java.io.{DataInputStream, DataOutputStream, File, FileWriter, Writer}
import java.net.ServerSocket

import com.mongodb.{BasicDBObject, DBCursor, DBObject}
import com.mongodb.casbah._
import org.apache.spark.graphx.{Edge, EdgeTriplet, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext, rdd}
import java.util.{Calendar, List}

import it.uniroma1.dis.wsngroup.gexf4j.core
import it.uniroma1.dis.wsngroup.gexf4j.core.data.{Attribute, AttributeClass, AttributeList, AttributeType}
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.{GexfImpl, StaxGraphWriter}
import it.uniroma1.dis.wsngroup.gexf4j.core.{EdgeType, Gexf, Metadata, Mode, Node}

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
  sc.setLogLevel("ERROR")
  def main(args:Array[String]): Unit ={
    var dbObjectList:ArrayBuffer[DBObject]=ArrayBuffer()
    mongoCollection.find().limit(300).forEach(x=>dbObjectList.append(x))

    for(i<-dbObjectList){
      println(i)
    }
    val cities:ArrayBuffer[(String,Int)]=dbObjectList.map(dbobject=>new Tuple2[String,Int](dbobject.get("city").toString,0))
    val eduLevels:ArrayBuffer[(String,Int)]=dbObjectList.map(dbobject=>new Tuple2[String,Int](dbobject.get("eduLevel").toString,1))
    val jobs:ArrayBuffer[(String,Int)]=dbObjectList.map(dbobject=>new Tuple2[String,Int](dbobject.get("keyWord").toString,2))
    val numOfPerson:ArrayBuffer[Int]=dbObjectList.map(dbobject=>dbobject.get("inviteCount").toString.toInt)





    var arr:ArrayBuffer[Long]=ArrayBuffer()
    for(i<-1 to 1500){
      arr.append(i)
    }
    val iterator=arr.iterator


    val searchList:ArrayBuffer[(String,Int)]=ArrayBuffer.concat(cities.distinct,eduLevels.distinct,jobs.distinct)
    val vertexList:ArrayBuffer[(Long,(String,Int))]=searchList.map(a=>new Tuple2[Long,(String,Int)](iterator.next(),a))


    //设置gexf环境
    val date:Calendar=Calendar.getInstance()
    val gexf:Gexf=new GexfImpl()
    gexf.getMetadata()
      .setLastModified(date.getTime)
      .setCreator("Gephi.org")
      .setDescription("sparkgraph")
    gexf.setVisualization(true)
    val gexfGraph:core.Graph=gexf.getGraph
    gexfGraph.setDefaultEdgeType(EdgeType.UNDIRECTED).setMode(Mode.STATIC)

    //设置属性列表
    val attributeList:AttributeList=new AttributeListImpl(AttributeClass.NODE)
    gexfGraph.getAttributeLists().add(attributeList)

    //设置属性
    val nodeType:Attribute=attributeList.createAttribute("NodeType",AttributeType.INTEGER,"NodeType")

    //创建节点
    for(i<-1 to searchList.size){
      gexfGraph.createNode(i.toString).setLabel(searchList(i-1)._1).getAttributeValues.addValue(nodeType,searchList(i-1)._2.toString)
    }


    val vertexRDD:RDD[(Long,(String,Int))]=sc.parallelize(vertexList)
    println("-----------------------------------------------")
    println("开始打印vertexRDD：")
    vertexRDD.foreach(a=>println(a))

    val edgeList:ArrayBuffer[Edge[Int]]=ArrayBuffer[Edge[Int]]()


    for(i<-0 to (cities.size-1)) {
      println("开始建立第 " + i + " 条边")


      var cityNum = searchList.indexOf(cities(i)) + 1
      var eduNum = searchList.indexOf(eduLevels(i)) + 1
      var jobNum = searchList.indexOf(jobs(i)) + 1


      println(cityNum + " " + eduNum + " " + jobNum)
      edgeList.append(new Edge[Int](cityNum, eduNum, numOfPerson(i)))
      edgeList.append(new Edge[Int](eduNum, jobNum, numOfPerson(i)))
      edgeList.append(new Edge[Int](cityNum,jobNum,numOfPerson(i)))
      //创建边

      try{
        gexfGraph.getNode(cityNum.toString).connectTo((i + 1).toString, gexfGraph.getNode(eduNum.toString)).setWeight(numOfPerson(i))
      }catch {
        case ex:IllegalArgumentException=>{
          edgeList.map(a=>{
            if((a.srcId==cityNum)&&(a.dstId==eduNum)){
              a.attr+=numOfPerson(i)
            }
          })
        }
      }

      try {

        gexfGraph.getNode(eduNum.toString).connectTo((cities.size+i+1).toString, gexfGraph.getNode(jobNum.toString)).setWeight(numOfPerson(i))
      }catch{
        case ex:IllegalArgumentException=>{
          //如果边已经存在，找出该边，加上人数
          edgeList.map(a=>{
            if((a.srcId==eduNum)&&(a.dstId==jobNum)){
              a.attr+=numOfPerson(i)
            }
          })
        }
      }

      try{
        gexfGraph.getNode(cityNum.toString).connectTo((cities.size*2+i+1).toString,gexfGraph.getNode(jobNum.toString)).setWeight(numOfPerson(i))
      }catch{
        case ex:IllegalArgumentException=>{
          edgeList.map(a=>{
            if((a.srcId==cityNum)&&(a.dstId==jobNum)){
              a.attr+=numOfPerson(i)
            }
          })
        }
      }
    }


    val edgeRDD:RDD[Edge[Int]]=sc.parallelize(edgeList)


    val graph:Graph[(String,Int),Int]=Graph(vertexRDD,edgeRDD)

    println("------------------------------------------")
    println("开始打印4：")

    graph.vertices.foreach(a=>println(a._1+" "+a._2))
    graph.edges.foreach(a=>println(a.srcId+" "+a.dstId+" "+a.attr))








    val f:File=new File("/Users/kevinchiang/Desktop/graph.gexf")

    val out:Writer=new FileWriter(f,false)
    val graphWriter:StaxGraphWriter=new StaxGraphWriter()
    graphWriter.writeToStream(gexf,out,"UTF-8")
    System.out.println(f.getAbsolutePath)


    val serverSocket:ServerSocket=new ServerSocket(9999)

    //持续监听客户端请求
    while(true) {
      val server = serverSocket.accept()
      println("远程主机地址是：" + server.getRemoteSocketAddress)
      val in: DataInputStream = new DataInputStream(server.getInputStream)
      val response = in.readUTF()
      val writer: DataOutputStream = new DataOutputStream(server.getOutputStream)



      //开始计算对应顶点的两个最相关的顶点
      //找出顶点类型
      val vertexId:Long=response.toLong
      var vertexType:Int= -1

      val tmpArr=graph.vertices.collect()
      for(i<-tmpArr){
        if(i._1==vertexId){
          vertexType=i._2._2
        }
      }

      if(vertexType== -1){
        throw new IllegalArgumentException("未找到顶点类型，vertexType = "+vertexType)
      }

      //把跟所选中顶点同类型的其他顶点去除
      val sub=graph.subgraph(edgeTriplet=>{
        true
      },(verId,VD)=>{
        if(VD._2==vertexType){ //所有职业顶点
          if(verId==vertexId){//所选定的职业顶点
            true
          }else{
            false
          }
        }else{
          true
        }
      })


      var vertexType1= -1
      var vertexType2= -1
      if(vertexType==0){
        vertexType1=1
        vertexType2=2
      }else if(vertexType==1){
        vertexType1=0
        vertexType2=2
      }else if(vertexType==2){
        vertexType1=0
        vertexType2=1
      }
      //提取另外两个类型顶点中的第一个
      //去除第二个类型顶点
      val subsub1=sub.subgraph(edgeTriplet=>{
        true
      },(verId,VD)=>{
        if(VD._2==vertexType2){
          false
        }else{
          true
        }
      })
      val tripletOfVertex1:EdgeTriplet[(String,Int),Int]=subsub1.triplets.reduce((triplet1, triplet2)=>{
        if(triplet1.attr>=triplet2.attr){
          triplet1
        }else{
          triplet2
        }
      })


      //提取另外两个类型顶点中的第二个
      //去除第一个类型顶点

      val subsub2=sub.subgraph(edgeTriplet=>{
        true
      },(verId,VD)=>{
        if(VD._2==vertexType1){
          false
        }else{
          true
        }
      })
      val tripletOfVertex2:EdgeTriplet[(String,Int),Int]=subsub2.triplets.reduce((triplet1,triplet2)=>{
        if(triplet1.attr>=triplet2.attr){
          triplet1
        }else{
          triplet2
        }
      })

      var vertexId1= -1L
      var vertexId2= -1L
      var vertexString1= ""
      var vertexString2= ""
      if(tripletOfVertex1.srcAttr._2==vertexType1){
        vertexString1=tripletOfVertex1.srcAttr._1
        vertexId1=tripletOfVertex1.srcId
      }else if(tripletOfVertex1.dstAttr._2==vertexType1){
        vertexString1=tripletOfVertex1.dstAttr._1
        vertexId1=tripletOfVertex1.dstId
      }else{
        throw new Exception("顶点类型错误")
      }

      if(tripletOfVertex2.srcAttr._2==vertexType2){
        vertexString2=tripletOfVertex2.srcAttr._1
        vertexId2=tripletOfVertex2.srcId
      }else if(tripletOfVertex2.dstAttr._2==vertexType2){
        vertexString2=tripletOfVertex2.dstAttr._1
        vertexId2=tripletOfVertex2.dstId
      }else{
        throw new Exception("顶点类型错误")
      }

      writer.writeUTF(vertexId1+" "+vertexString1+" "+vertexId2+" "+vertexString2)

      server.close()
    }

  }
}



