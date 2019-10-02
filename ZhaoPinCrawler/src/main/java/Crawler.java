import request.ListRequest;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

public class Crawler {
    private static final String[] jobs={"%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86"};
    public static void main(String[] args){
        System.out.println("开始爬取：");
        Spider spider=Spider.create(new RecruitmentProcessor());
        for(String job:jobs){
            spider.addUrl(new ListRequest(job).get());
        }
        spider.addPipeline(new ConsolePipeline());
        spider.addPipeline(new MongoPipeline());
        spider.thread(5).run();
    }
}
