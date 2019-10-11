//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.function.FlatMapFunction;
//import org.apache.spark.api.java.function.Function2;
//import org.apache.spark.api.java.function.PairFunction;
//import org.apache.spark.streaming.Durations;
//import org.apache.spark.streaming.api.java.JavaDStream;
//import org.apache.spark.streaming.api.java.JavaPairDStream;
//import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
//import org.apache.spark.streaming.api.java.JavaStreamingContext;
//import scala.Tuple2;
//
//import java.util.Arrays;
//import java.util.Iterator;
//
//// streaming从socket获取数据处理
//
//
//public class Main {
//    public static void main(String[] args) {
//        //设置运行模式local 设置appname
//        SparkConf conf=new SparkConf().setMaster("local[2]").setAppName("StreamingFromSocketTest");
//        //初始化，设置窗口大小为2s
//        JavaStreamingContext jssc=new JavaStreamingContext(conf, Durations.seconds(2L));
//        //从本地Socket的9999端口读取数据
//        JavaReceiverInputDStream<String> lines= jssc.socketTextStream("localhost", 9999);
//        lines.print();
//        //把一行数据转化成单个单次  以空格分隔
//        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String,String>(){
//            @Override
//            public Iterator<String> call(String line) throws Exception {
//
//                return Arrays.asList(line.split(" ")).iterator();
//
//            }
//        });
//        //计算每一个单次在一个batch里出现的个数
//        JavaPairDStream<String, Integer> pairs= words.mapToPair(new PairFunction<String, String, Integer>() {
//            @Override
//            public Tuple2<String, Integer> call(String s) throws Exception {
//                return new Tuple2<String, Integer>(s,1);
//            }
//        });
//        JavaPairDStream<String,Integer> wordCounts=pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
//            @Override
//            public Integer call(Integer integer, Integer integer2) throws Exception {
//                return integer+integer2;
//            }
//        });
//        //输出统计结果
//        wordCounts.print();
//        jssc.start();
//        //20s后结束
//        try {
//            jssc.awaitTerminationOrTimeout(200*1000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//}

