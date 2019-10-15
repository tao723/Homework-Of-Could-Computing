

import java.io.{DataInputStream, DataOutputStream}
import java.net.{ServerSocket, Socket}

import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}



object Test {
  def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Simple GraphX").setMaster("local")
        val sc = new SparkContext(conf)

        // 顶点
        val vertexArray = Array(
          (1L,("Alice", 38)),
          (2L,("Henry", 27)),
          (3L,("Charlie", 55)),
          (4L,("Peter", 32)),
          (5L,("Mike", 35)),
          (6L,("Kate", 23))
        )

        // 边
        val edgeArray = Array(
          Edge(2L, 1L, 5),
          Edge(2L, 4L, 2),
          Edge(3L, 2L, 7),
          Edge(3L, 6L, 3),
          Edge(4L, 1L, 1),
          Edge(5L, 2L, 3),
          Edge(5L, 3L, 8),
          Edge(5L, 6L, 8),
          Edge(5L, 6L, 7)
        )

        //构造vertexRDD和edgeRDD
        val vertexRDD:RDD[(Long,(String,Int))] = sc.parallelize(vertexArray)
        val edgeRDD:RDD[Edge[Int]] = sc.parallelize(edgeArray)

        // 构造图
        val graph:Graph[(String,Int),Int] = Graph(vertexRDD, edgeRDD)

        val mergedGraph=graph.groupEdges((a,b)=>{
          a+b
        }).edges.foreach(a=>println(a.srcId+" "+a.dstId+" "+a.attr))




  }
}


