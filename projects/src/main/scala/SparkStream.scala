import org.apache.avro.util.Utf8
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.datanucleus.store.rdbms.connectionpool.ConnectionPool
//import com.mongodb.casbah.{MongoClient, MongoDB}

object SparkStream{
//  def functionToCreateContext():StreamingContext={
//    val conf=new SparkConf().setAppName("SparkStreamingSocket").setMaster("local[2]")  //两个线程，一个监听新文件并输出，另一个参与计算reduce并输出
//    val sc=new SparkContext(conf)
//    val ssc=new StreamingContext(sc,Seconds(4))  //若无法dstream没有间断性运行，又可能是此处时间间隔的设置问题
//    var index= -3;
//    ssc.checkpoint("C:\\Users\\20271\\Desktop\\SparkHomework\\CheckpointLogs")
//
//
//  }


  def main(args:Array[String]): Unit ={
    val conf=new SparkConf().setAppName("SparkStreamingSocket").setMaster("local[*]")  //两个线程，一个监听新文件并输出，另一个参与计算reduce并输出
    val sc=new SparkContext(conf)
    sc.setLogLevel("ERROR")
    val ssc=new StreamingContext(sc,Seconds(10))  //若无法dstream没有间断性运行，又可能是此处时间间隔的设置问题
    var index1= 0;
    var index2= 0;
    ssc.checkpoint("C:\\Users\\20271\\Desktop\\SparkHomework\\CheckpointLogs")
    //ssc.setCheckpointDir("C:\\Users\\20271\\Desktop\\SparkHomework\\CheckpointLogs")

    //val ds=ssc.socketTextStream("localhost",9999)
    //val res=ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    val ds1=ssc.textFileStream("C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\searchkeyword")  //用来存取岗位的dstream
    val ds2=ssc.textFileStream("C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile\\city")  //用来存取城市的dstream
    ds1.checkpoint(Seconds(20))
    ds2.checkpoint(Seconds(20))

    val res1=ds1.flatMap(_.split(" ")).map(word => (word,1)).updateStateByKey[Int](updateFunc2)
    val res2=ds2.flatMap(_.split(" ")).map(word => (word,1)).updateStateByKey[Int](updateFunc2)
    res1.print(800)
    res2.print(800)
    res1.foreachRDD(rdd1 => {
      index1=index1+1
      rdd1.checkpoint()
      rdd1.foreachPartition(partitionOfRecords =>{
        val conn1 = ConnectionPool.getConnection
        //conn1.setAutoCommit(false);  //设为手动提交
        val  stmt = conn1.createStatement();
        //index=index+1
        partitionOfRecords.foreach( record => {

          stmt.addBatch("insert into searchkeyword (keyword,search_count,insertIndex) values ('"+record._1+"','"+record._2+"',"+index1+")");
        })
        stmt.executeBatch();
        //conn1.commit();  //提交事务

      })
    })

    res2.foreachRDD(rdd => {
      index2=index2+1
      rdd.checkpoint()
      rdd.foreachPartition(partitionOfRecords =>{
        val conn2 = ConnectionPool.getConnection
        //conn2.setAutoCommit(false);  //设为手动提交
        val  stmt = conn2.createStatement();
        partitionOfRecords.foreach( record => {

          stmt.addBatch("insert into City (city,search_count,insertIndex) values ('"+record._1+"','"+record._2+"',"+index2+")");
        })
        stmt.executeBatch();
        //conn2.commit();  //提交事务

      })
    })
    //res.saveAsTextFiles("C:\\Users\\20271\\Desktop\\SparkHomework\\ResultFile")
    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
  }

  val updateFunc2 = (values: Seq[Int], state: Option[Int]) => {
    val currentCount = values.foldLeft(0)(_ + _)
    val previousCount = state.getOrElse(0)
    Some(currentCount + previousCount)
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








//
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