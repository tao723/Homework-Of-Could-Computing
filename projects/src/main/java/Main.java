//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.Optional;
////import org.apache.spark.api.java.function.*;
//import org.apache.spark.streaming.Durations;
//import org.apache.spark.streaming.api.java.JavaDStream;
//import org.apache.spark.streaming.api.java.JavaPairDStream;
//import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
//import org.apache.spark.streaming.api.java.JavaStreamingContext;
//import scala.Tuple2;
//
//import java.sql.Connection;
//import java.sql.Statement;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.spark.api.java.function.FlatMapFunction;
//import org.apache.spark.api.java.function.Function;
//import org.apache.spark.api.java.function.Function2;
//import org.apache.spark.api.java.function.PairFunction;
//import org.apache.spark.api.java.function.VoidFunction;
//
//
//// streaming从socket获取数据处理
//
//
//public class Main {
//    public static void main(String[] args) {
//        //设置运行模式local 设置appname
//        SparkConf conf=new SparkConf().setMaster("local[2]").setAppName("StreamingFromSocketTest");
//        //初始化，设置窗口大小为2s
//        JavaStreamingContext jssc=new JavaStreamingContext(conf, Durations.seconds(5L));
//        //从本地Socket的9999端口读取数据
//        //JavaReceiverInputDStream<String> lines= jssc.socketTextStream("localhost", 9999);
//        jssc.checkpoint("C:\\Users\\20271\\Desktop\\SparkHomework\\CheckpointLogs");
//        JavaDStream<String> lines = jssc.textFileStream("C:\\Users\\20271\\Desktop\\SparkHomework\\TestFile");
//
//        //lines.print();
//        //System.out.println("----------------------------------------------------");
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
//        //累加计数
//        JavaPairDStream<String, Integer> resultDstream=pairs.updateStateByKey(new Function2<List<Integer>, Optional<Integer>, Optional<Integer>>() {
//
//            private static final long serialVersionUID=1L;
//            public Optional<Integer> call(List<Integer> values, Optional<Integer> state) throws Exception {
//                Integer oldValue=0;   //默认旧value是0
//                if (state.isPresent()) {
//                    oldValue=state.get();
//                }
//                for (Integer value:values) {
//                    oldValue+=value;
//                }
//                return Optional.of(oldValue);
//            }
//        });
//
////        JavaPairDStream<String,Integer> wordCounts=pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
////            @Override
////            public Integer call(Integer integer, Integer integer2) throws Exception {
////                return integer+integer2;
////            }
////        });
//        //输出统计结果
//        //resultDstream.print(800);
//
//
//        // 每次得到当前所有单词的统计次数之后，将其写入mysql存储，进行持久化，以便于后续的J2EE应用程序
//        // 进行显示
//        resultDstream.foreachRDD(new Function <JavaPairRDD<String,Integer>,Void>() {
//
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public Void call(JavaPairRDD<String, Integer> wordCountsRDD) throws Exception {
//                // 调用RDD的foreachPartition()方法
//                wordCountsRDD.foreachPartition(new VoidFunction<Iterator<Tuple2<String,Integer>>>() {
//
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public void call(Iterator<Tuple2<String, Integer>> wordCounts) throws Exception {
//                        // 给每个partition，获取一个连接
//                        Connection conn = ConnectionPool.getConnection();
//
//                        // 遍历partition中的数据，使用一个连接，插入数据库
//                        Tuple2<String, Integer> wordCount = null;
//                        while(wordCounts.hasNext()) {
//                            wordCount = wordCounts.next();
//
//                            String sql = "insert into wordcount(word,count) "
//                                    + "values('" + wordCount._1 + "'," + wordCount._2 + ")";
//
//                            Statement stmt = conn.createStatement();
//                            stmt.executeUpdate(sql);
//                        }
//
//                        // 用完以后，将连接还回去
//                        ConnectionPool.returnConnection(conn);
//                    }
//                });
//
//                return null;
//            }
//
//        });
//
//
//
//
//
//
//        jssc.start();
//        //600s后结束
//        try {
//            jssc.awaitTerminationOrTimeout(600*1000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
//
