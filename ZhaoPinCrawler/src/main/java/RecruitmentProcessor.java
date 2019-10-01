import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;


public class RecruitmentProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(30);
    private static final String DETAIL="https://xiaoyuan.zhaopin.com/job/[A-Z]*[0-9]*";
    private static final String LIST="https://xiaoyuan.zhaopin.com/search/jn=2&js=-1&kw=\\S+&pg=[0-9]*&order=1";
    private static final String SOU="https://xiaoyuan.zhaopin.com/api/sou?\\S+";

    public void process(Page page) {
        //招聘信息详细页面
        if(page.getUrl().regex(DETAIL).match()){
            String time = page.getHtml().xpath("//span[@class='time']/text()").get();
            String jobName = page.getHtml().xpath("//span[@class='name']/span//text()").get();
            String city = page.getHtml().xpath("//p[@class='muilt-infos']/span[1]/span/text()").get();
            String jobType = page.getHtml().xpath("//p[@class='muilt-infos']/span[2]/text()").get();
            String eduLevel = page.getHtml().xpath("//p[@class='muilt-infos']/span[3]/text()").get();
            String inviteCount = page.getHtml().xpath("//p[@class='muilt-infos']/span[4]/span/text()").get();
            String major = page.getHtml().xpath("//p[@class='muilt-infos']/span[5]/text()").get();
            String companyName = page.getHtml().xpath("//div[@class='intro']/p/text()").get();
            String companyScale = page.getHtml().xpath("//div[@class='intro']/p[3]/span[2]/text()").get();
            String companyType = page.getHtml().xpath("//div[@class='intro']/p[4]/span[2]/text()").get();
            String description = page.getHtml().xpath("//div[@class='describe']//text()").get();
            page.putField("time",time);
            page.putField("jobName",jobName);
            page.putField("jobType",jobType);
            page.putField("city",city);
            page.putField("companyName",companyName);
            page.putField("companyScale",companyScale);
            page.putField("companyType",companyType);
            page.putField("eduLevel",eduLevel);
            page.putField("inviteCount",inviteCount);
            page.putField("major",major);
            page.putField("description",description);
        }
        //招聘信息列表页面
        else if(page.getUrl().regex(LIST).match()){
            page.addTargetRequest("https://xiaoyuan.zhaopin.com/api/sou?sourceClient=sou&keyWord=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&jobSource=&pageNumber=1&jobNatures=2&jobTypeId=&cityIdList=&cityId=&industryId=&companyTypeId=&dateSearchTypeId=&orderBy=&clientIp=10.172.27.221&_v=0.26932904&x-zp-page-request-id=713f04ef3484427fa820b7ddeb5bab0f-1569682437617-181697&x-zp-client-id=9166d50b-75bc-4618-8c76-2e74bd39b5a1");
        }
        //搜索请求获得的json
        else if(page.getUrl().regex(SOU).match()){
            for(String item:page.getJson().jsonPath("$.data.Items").all()){
                JSONObject jsonObject = JSONObject.parseObject(item);
                page.addTargetRequest("https://xiaoyuan.zhaopin.com/job/"+(String) jsonObject.get("JobPositionNumber"));
            }
        }
    }

    public Site getSite() {
        return site;
    }
}
