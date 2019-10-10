import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
//import com.mongodb.casbah.{MongoClient, MongoDB}

object SparkStream{
  def main(args:Array[String]): Unit ={
    val conf=new SparkConf().setAppName("SparkStreamingSocket").setMaster("local[2]")  //两个线程，一个监听新文件并输出，另一个参与计算reduce并输出
    val sc=new SparkContext(conf)
    val ssc=new StreamingContext(sc,Seconds(10))

    ssc.checkpoint("C:\\Users\\20271\\Desktop\\SparkHomework\\CheckpointLogs")

    //val ds=ssc.socketTextStream("localhost",9999)
    //val res=ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    val ds=ssc.textFileStream("C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile")

    //ds.print(80);
    //val res=ds.flatMap(_.split(" ")).map(word => (word,1)).reduceByKey((a,b) => (a+b))
    val res=ds.flatMap(_.split(" ")).map(word => (word,1)).updateStateByKey[Int](updateFunction _)
    //val res=ds.flatMap(_.split(" ")).map(word => (word,1)).updateStateByKey(updateFunc)
//    val res0=ds.flatMap(_.split(" "))
//    val res1=res0.map(word => (word,1))
//    val res=res1.updateStateByKey[Int](addFunc)
    res.print(800)
    //res.saveAsTextFiles("C:\\Users\\20271\\Desktop\\SparkHomework\\ResultFile")
    ssc.start()
    ssc.awaitTermination()
  }
  val addFunc = (currValues: Seq[Int], prevValueState: Option[Int]) => {
    //通过Spark内部的reduceByKey按key规约。然后这里传入某key当前批次的Seq/List,再计算当前批次的总和
    val currentCount = currValues.sum
    // 已累加的值
    val previousCount = prevValueState.getOrElse(0)
    // 返回累加后的结果。是一个Option[Int]类型
    Some(currentCount + previousCount)
  }

  def updateFunc(currentValues:Seq[Int], historyValue:Option[Int]):Option[Int] ={
    val newValues: Int = currentValues.sum + historyValue.getOrElse(0)
    Some(newValues)
  }


  def updateFunction(newValues:Seq[Int], runningCount:Option[Int]):Option[Int]={
    val preCount=runningCount.getOrElse(0)
    val newCount=newValues.sum
    Some(newCount+preCount)
  }

}









//val sparkConf:SparkConf=new SparkConf().setAppName("SparkStreamingSocket").setMaster("local[2]")
//
//val sc=new SparkContext(sparkConf)
//sc.setLogLevel("WARN")
//
//val ssc=new StreamingContext(sc,Seconds(5))
//
//ssc.checkpoint("C:\\Users\\20271\\Desktop\\SparkHomework\\checkpoint.txt")
//
//val stream: