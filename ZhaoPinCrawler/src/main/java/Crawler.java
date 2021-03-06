import request.ListRequest;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

/**
 * 爬虫入口
 * @author debonet
 */
public class Crawler {

    private static final String[] jobs={"%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86","%E4%BA%A7%E5%93%81%E4%B8%93%E5%91%98",
            "%E4%BA%A7%E5%93%81%E8%AE%BE%E8%AE%A1","%E6%96%B0%E5%AA%92%E4%BD%93%E8%BF%90%E8%90%A5","%E6%96%87%E6%A1%88%E7%BC%96%E8%BE%91"
            ,"%E5%86%85%E5%AE%B9%E8%BF%90%E8%90%A5","%E4%BA%A4%E4%BA%92%E8%AE%BE%E8%AE%A1","UI%E8%AE%BE%E8%AE%A1%E5%B8%88"
            ,"%E7%BD%91%E9%A1%B5%E8%AE%BE%E8%AE%A1","%E5%A4%A7%E6%95%B0%E6%8D%AE%E5%B7%A5%E7%A8%8B%E5%B8%88","%E7%B3%BB%E7%BB%9F%E8%AE%BE%E8%AE%A1"
            ,"%E6%95%B0%E6%8D%AE%E5%88%86%E6%9E%90%E5%B8%88","%E7%A0%94%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%88","%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E6%B5%8B%E8%AF%95%E5%B7%A5%E7%A8%8B%E5%B8%88","%E5%89%8D%E7%AB%AF%E5%B7%A5%E7%A8%8B%E5%B8%88","%E7%A1%AC%E4%BB%B6%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E7%A7%BB%E5%8A%A8%E5%BC%80%E5%8F%91","%E4%BA%BA%E5%B7%A5%E6%99%BA%E8%83%BD","%E6%95%B0%E6%8D%AE%E5%BA%93%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E8%BF%90%E7%BB%B4%E5%B7%A5%E7%A8%8B%E5%B8%88","%E6%B8%B8%E6%88%8F%E5%BC%80%E5%8F%91","%E9%80%9A%E4%BF%A1%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E9%A1%B9%E7%9B%AE%E7%AE%A1%E7%90%86","%E5%94%AE%E5%89%8D%E5%B7%A5%E7%A8%8B%E5%B8%88","%E5%94%AE%E5%90%8E%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E6%8A%80%E6%9C%AF%E6%94%AF%E6%8C%81","%E9%93%B6%E8%A1%8C%E6%9F%9C%E5%91%98","%E9%80%9A%E7%94%A8%E5%B2%97%E4%BD%8D"
            ,"%E6%8A%95%E8%B5%84%E7%BB%8F%E7%90%86","%E7%90%86%E8%B4%A2%E9%A1%BE%E9%97%AE","%E9%93%B6%E8%A1%8C%E5%AE%A2%E6%88%B7%E7%BB%8F%E7%90%86"
            ,"%E7%B2%BE%E7%AE%97%E5%B8%88","%E4%BF%A1%E6%89%98%E4%B8%9A%E5%8A%A1","%E8%AF%81%E5%88%B8%E7%BB%8F%E7%BA%AA%E4%BA%BA"
            ,"%E4%BF%9D%E9%99%A9%E9%A1%BE%E9%97%AE","%E7%90%86%E8%B5%94%E4%B8%93%E5%91%98","%E4%B8%AA%E4%BA%BA%E4%B8%9A%E5%8A%A1"
            ,"%E5%85%AC%E5%8F%B8%E4%B8%9A%E5%8A%A1","%E4%BF%A1%E7%94%A8%E5%8D%A1%E4%B8%9A%E5%8A%A1","%E5%B7%A5%E4%B8%9A%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E5%88%B6%E9%80%A0%E5%B7%A5%E7%A8%8B%E5%B8%88","%E7%94%9F%E4%BA%A7%E7%BB%8F%E7%90%86","%E7%94%B5%E5%AD%90%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E7%94%B5%E6%B0%94%E5%B7%A5%E7%A8%8B%E5%B8%88","%E8%87%AA%E5%8A%A8%E5%8C%96%E5%B7%A5%E7%A8%8B%E5%B8%88","kw=%E5%8D%8A%E5%AF%BC%E4%BD%93%E6%8A%80%E6%9C%AF"
            ,"%E6%9C%BA%E6%A2%B0%E5%B7%A5%E7%A8%8B%E5%B8%88","%E6%9C%BA%E7%94%B5%E5%B7%A5%E7%A8%8B%E5%B8%88","%E7%BB%B4%E4%BF%AE%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E6%B1%BD%E8%BD%A6%E8%AE%BE%E8%AE%A1","%E6%B1%BD%E8%BD%A6%E5%88%B6%E9%80%A0","%E6%B1%BD%E8%BD%A6%E7%BB%B4%E4%BF%AE%E3%80%81%E4%BF%9D%E5%85%BB"
            ,"%E6%A8%A1%E5%85%B7%E5%B7%A5%E7%A8%8B%E5%B8%88","%E5%8C%96%E5%B7%A5%E5%B7%A5%E7%A8%8B%E5%B8%88","%E5%AE%9E%E9%AA%8C%E5%AE%A4%E6%8A%80%E6%9C%AF%E5%91%98"
            ,"%E7%94%9F%E7%89%A9%E5%88%B6%E8%8D%AF","%E5%8C%BB%E8%8D%AF%E6%8A%80%E6%9C%AF%E7%A0%94%E5%8F%91","%E6%9C%8D%E8%A3%85%E8%AE%BE%E8%AE%A1"
            ,"%E7%BA%BA%E7%BB%87%E5%B7%A5%E7%A8%8B%E5%B8%88","%E6%88%BF%E5%9C%B0%E4%BA%A7%E9%94%80%E5%94%AE","%E6%88%BF%E5%9C%B0%E4%BA%A7%E9%A1%B9%E7%9B%AE%E7%AE%A1%E7%90%86"
            ,"%E5%90%88%E5%90%8C%E7%AE%A1%E7%90%86","%E5%BB%BA%E7%AD%91%E5%B7%A5%E7%A8%8B%E5%B8%88","%E5%9C%9F%E5%BB%BA%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E7%BB%93%E6%9E%84%E5%B7%A5%E7%A8%8B%E5%B8%88","%E9%80%A0%E4%BB%B7%E5%B7%A5%E7%A8%8B%E5%B8%88","%E5%B8%82%E6%94%BF%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E7%BB%99%E6%8E%92%E6%B0%B4%E5%B7%A5%E7%A8%8B%E5%B8%88","%E5%88%B6%E5%86%B7%E5%B7%A5%E7%A8%8B%E5%B8%88","%E6%9A%96%E9%80%9A%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E5%BB%BA%E7%AD%91%E8%AE%BE%E8%AE%A1%E5%B8%88","%E9%A1%B9%E7%9B%AE%E5%B7%A5%E7%A8%8B%E5%B8%88","%E6%99%AF%E8%A7%82%E8%AE%BE%E8%AE%A1"
            ,"%E5%AE%A4%E5%86%85%E8%AE%BE%E8%AE%A1","%E8%A3%85%E6%BD%A2%E8%AE%BE%E8%AE%A1%E5%B8%88","%E7%8E%AF%E4%BF%9D%E5%B7%A5%E7%A8%8B%E5%B8%88"
            ,"%E5%9C%B0%E8%B4%A8%E5%B7%A5%E7%A8%8B%E5%B8%88","%E9%94%80%E5%94%AE%E4%BB%A3%E8%A1%A8","%E7%94%B5%E8%AF%9D%E9%94%80%E5%94%AE"
            ,"%E7%BD%91%E7%BB%9C%E9%94%80%E5%94%AE","%E5%A4%A7%E5%AE%A2%E6%88%B7%E9%94%80%E5%94%AE","%E9%94%80%E5%94%AE%E4%B8%9A%E5%8A%A1%E8%B7%9F%E5%8D%95"
            ,"%E6%8B%9B%E5%95%86%E4%B8%93%E5%91%98","%E9%94%80%E5%94%AE%E7%AE%A1%E7%90%86","%E9%94%80%E5%94%AE%E6%94%AF%E6%8C%81"
            ,"%E5%94%AE%E5%89%8D%E6%94%AF%E6%8C%81","%E5%94%AE%E5%90%8E%E6%94%AF%E6%8C%81","%E5%AE%A2%E6%9C%8D%E4%B8%93%E5%91%98"
            ,"%E7%BD%91%E7%BB%9C%E5%AE%A2%E6%9C%8D","%E5%B8%82%E5%9C%BA%E4%B8%93%E5%91%98","%E6%B4%BB%E5%8A%A8%E7%AD%96%E5%88%92"
            ,"%E6%B4%BB%E5%8A%A8%E6%89%A7%E8%A1%8C","%E5%B8%82%E5%9C%BA%E6%8E%A8%E5%B9%BF","%E5%93%81%E7%89%8C%E4%B8%93%E5%91%98"
            ,"%E5%93%81%E7%89%8C%E5%85%AC%E5%85%B3","%E8%A1%8C%E6%94%BF%E4%B8%93%E5%91%98","%E5%89%8D%E5%8F%B0"
            ,"%E6%96%87%E7%A7%98","%E4%BA%BA%E5%8A%9B%E8%B5%84%E6%BA%90%E4%B8%93%E5%91%98","%E4%BA%BA%E4%BA%8B%E5%8A%A9%E7%90%86"
            ,"%E6%8B%9B%E8%81%98%E4%B8%93%E5%91%98","%E7%BB%A9%E6%95%88%E8%80%83%E6%A0%B8%E4%B8%93%E5%91%98","%E8%96%AA%E9%85%AC%E7%A6%8F%E5%88%A9%E4%B8%93%E5%91%98"
            ,"%E5%87%BA%E7%BA%B3","%E4%BC%9A%E8%AE%A1%E5%B8%88","%E8%B4%A2%E5%8A%A1%E4%B8%93%E5%91%98"
            ,"%E5%AE%A1%E8%AE%A1%E4%B8%93%E5%91%98","%E7%A8%8E%E5%8A%A1","%E6%B3%95%E5%8A%A1%E4%B8%93%E5%91%98"
            ,"%E5%90%88%E8%A7%84%E4%B8%93%E5%91%98","%E5%AE%9E%E4%B9%A0%E7%94%9F","%E7%AE%A1%E5%9F%B9%E7%94%9F"
            ,"%E5%82%A8%E5%A4%87%E5%B9%B2%E9%83%A8","%E5%85%AC%E5%8A%A1%E5%91%98","%E5%92%A8%E8%AF%A2%E9%A1%BE%E9%97%AE"
            ,"%E6%95%99%E5%B8%88","%E7%BF%BB%E8%AF%91","%E5%85%BC%E8%81%8C"
            ,"%E5%85%B6%E4%BB%96"};


    public static void main(String[] args){
        System.out.println("开始爬取：");
        Spider spider=Spider.create(new RecruitmentProcessor());
        for(String job:jobs){
            spider.addUrl(new ListRequest(job).get());
        }
        spider.addPipeline(new ConsolePipeline());
        spider.addPipeline(new MongoPipeline());
        spider.setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover()));
        spider.thread(50).run();
    }
}
