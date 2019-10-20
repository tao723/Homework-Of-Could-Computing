import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.{DistanceMeasure, KMeans}
import org.apache.spark.mllib.linalg.Vectors



object Demo {
  def main(args: Array[String]): Unit = {
    val cityName = "南京"
    val cityData = "22991 12889 1146 458 6524 1632 1299"

    val conf = new SparkConf().setMaster("local[3]").setAppName("Kmeans")
    val context = new SparkContext(conf)
    context.setLogLevel("ERROR")
    val dataSourceRDD = context.textFile("E:\\study\\cities-jobs.txt")
    val trainRDD = dataSourceRDD.map(lines => Vectors.dense(lines.substring(lines.indexOf(' ')+1).split(" ").map(_.toDouble)))

    val kMeans = new KMeans()
    kMeans.setDistanceMeasure(DistanceMeasure.COSINE)
    kMeans.setK(8)
    kMeans.setMaxIterations(60)
    val model = kMeans.run(trainRDD)



    val cross = model.computeCost(trainRDD)
    println("Cost：" + cross)


    //处理传输过来的城市数据
    val res = model.predict(Vectors.dense(cityData.split(' ').map(_.toDouble)))
    println("城市 "+cityName+" 的预测类别为:类别" + res + "\r\n")

    println("与城市 "+cityName+" 类型相关的城市还有：")

    val crossPredictRes = dataSourceRDD.map{
      lines =>
        val lineVectors = Vectors.dense(lines.substring(lines.indexOf(' ')+1).split(" ").map(_.toDouble))
        val city = lines.substring(0,lines.indexOf(' '))
        val predictRes = model.predict(lineVectors)
        city + ":" + predictRes
    }
    crossPredictRes.foreach(x=>{
      if(res.toString.equals(x.substring(x.indexOf(':')+1))){
        println(x)
      }
    })





  }
}
