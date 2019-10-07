import com.alibaba.fastjson.JSONObject;
import request.DetailRequest;
import request.ListRequest;
import request.SouRequest;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;


public class RecruitmentProcessor implements PageProcessor {

    private String souBackup = "https://xiaoyuan.zhaopin.com/api/sou?sourceClient=sou&keyWord=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&jobSource=&pageNumber=1&jobNatures=2&jobTypeId=&cityIdList=&cityId=&industryId=&companyTypeId=&dateSearchTypeId=&orderBy=&clientIp=10.172.27.221&_v=0.26932904&x-zp-page-request-id=713f04ef3484427fa820b7ddeb5bab0f-1569682437617-181697&x-zp-client-id=9166d50b-75bc-4618-8c76-2e74bd39b5a1";
    private Site site = Site.me().setRetryTimes(3).setSleepTime(30);
    //单次请求获取的最大的招聘数量
    private static final int TOTAL_MAX=1020;
    private enum QUERY{CITY,JT,OTHER,IND,COR,JS};

    public void process(Page page) {
        //招聘信息详细页面
        if(page.getUrl().regex(DetailRequest.FORMAT).match()){
            DetailRequest currentRequest = new DetailRequest().parse(page.getUrl().get());
            String jobPositionNumber = currentRequest.getJobPositionNumber();
            String keyWord = currentRequest.getKw();
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
            page.putField("_id",jobPositionNumber);
            page.putField("keyWord",keyWord);
            page.putField("time",time);
            page.putField("jobName",jobName);
            page.putField("jobType",jobType.trim());
            page.putField("city",city);
            page.putField("companyName",companyName);
            page.putField("companyScale",companyScale);
            page.putField("companyType",companyType);
            page.putField("eduLevel",eduLevel.trim());
            page.putField("inviteCount",inviteCount);
            page.putField("major",major);
            page.putField("description",description);
        }
        //招聘信息列表页面
        else if(page.getUrl().regex(ListRequest.FORMAT).match()){
            int total = Integer.parseInt(page.getHtml().xpath("//span[@class='total']/text()").get().trim());
            ListRequest currentRequest = new ListRequest().parse(page.getUrl().get());
            //单次请求获取的数量过多无法爬取
            if(total>TOTAL_MAX){
                String firstTitle = page.getHtml().xpath("//span[@class='query-title fn-left'][1]/text()").get();
                SouRequest request=new SouRequest();
                boolean isPartition = true;
                switch (getQuery(firstTitle)){
                    case CITY:
                        request.setKw(currentRequest.getKw());
                        request.setSt(SouRequest.CITY);
                        break;
                    case JT:
                        request.setKw(currentRequest.getKw());
                        request.setCity(currentRequest.getCity());
                        request.setSt(SouRequest.JT);
                        break;
                    case IND:
                        request.setKw(currentRequest.getKw());
                        request.setCity(currentRequest.getCity());
                        request.setJt(currentRequest.getJt());
                        request.setSt(SouRequest.IND);
                        break;
                    case COR:
                        request.setKw(currentRequest.getKw());
                        request.setCity(currentRequest.getCity());
                        request.setJt(currentRequest.getJt());
                        request.setInd(currentRequest.getInd());
                        request.setSt(SouRequest.COR);
                        break;
                    case JS:
                        request.setKw(currentRequest.getKw());
                        request.setCity(currentRequest.getCity());
                        request.setJt(currentRequest.getJt());
                        request.setInd(currentRequest.getInd());
                        request.setCor(currentRequest.getCor());
                        request.setSt(SouRequest.JS);
                        break;
                    case OTHER:
                        crawList(page);
                        isPartition=false;
                        break;
                    default:
                        isPartition=false;
                        break;
                }
                if (isPartition) page.addTargetRequest(request.get());
            }
            //单次请求的数量可以爬取
            else {
                crawList(page);
            }
        }
        //搜索请求获得的json
        else if(page.getUrl().regex(SouRequest.FORMAT).match()){
            SouRequest currentRequest = new SouRequest().parse(page.getUrl().get());
            int st = currentRequest.getSt();
            if(st==SouRequest.NORMAL){
                for(String item:page.getJson().jsonPath("$.data.Items").all()){
                    JSONObject jsonObject = JSONObject.parseObject(item);
                    String jobPositionNumber = (String) jsonObject.get("JobPositionNumber");
                    String traceUrl = (String) jsonObject.get("Traceurl");
                    page.addTargetRequest(new DetailRequest(jobPositionNumber,traceUrl,currentRequest.getKw()).get());
                }
            }
            ListRequest request=new ListRequest();
            if(st==SouRequest.CITY){
                for(String item:page.getJson().jsonPath("$.data.FacetsItems.SOU_WORK_CITY").all()){
                    JSONObject jsonObject = JSONObject.parseObject(item);
                    String cityCode = (String) jsonObject.get("code");
                    int city = Integer.parseInt(cityCode);
                    //if(city!=489)
                    request.setKw(currentRequest.getKw());
                    request.setCity(city);
                    page.addTargetRequest(request.get());
                }
            }
            else if(st==SouRequest.JT){
                for(String item:page.getJson().jsonPath("$.data.FacetsItems.SOU_POSITION_SMALLTYPE").all()){
                    JSONObject jsonObject = JSONObject.parseObject(item);
                    String jtCode = (String) jsonObject.get("code");
                    int jt = Integer.parseInt(jtCode);
                    request.setKw(currentRequest.getKw());
                    request.setCity(currentRequest.getCity());
                    request.setJt(jt);
                    page.addTargetRequest(request.get());
                }
            }
            else if(st==SouRequest.IND){
                for(String item:page.getJson().jsonPath("$.data.FacetsItems.SOU_INDUSTRY").all()){
                    JSONObject jsonObject = JSONObject.parseObject(item);
                    String indCode = (String) jsonObject.get("code");
                    int ind = Integer.parseInt(indCode);
                    request.setKw(currentRequest.getKw());
                    request.setCity(currentRequest.getCity());
                    request.setJt(currentRequest.getJt());
                    request.setInd(ind);
                    page.addTargetRequest(request.get());
                }
            }
            else if(st==SouRequest.COR){
                for(String item:page.getJson().jsonPath("$.data.FacetsItems.SOU_COMPANY_TYPE").all()){
                    JSONObject jsonObject = JSONObject.parseObject(item);
                    String corCode = (String) jsonObject.get("code");
                    int cor = Integer.parseInt(corCode);
                    request.setKw(currentRequest.getKw());
                    request.setCity(currentRequest.getCity());
                    request.setJt(currentRequest.getJt());
                    request.setInd(currentRequest.getInd());
                    request.setCor(cor);
                    page.addTargetRequest(request.get());
                }
            }
            else if (st==SouRequest.JS){
                for(String item:page.getJson().jsonPath("$.data.FacetsItems.SOU_POSITION_SOURCE_TYPE").all()){
                    JSONObject jsonObject = JSONObject.parseObject(item);
                    String jsCode = (String) jsonObject.get("code");
                    int js = Integer.parseInt(jsCode);
                    request.setKw(currentRequest.getKw());
                    request.setCity(currentRequest.getCity());
                    request.setJt(currentRequest.getJt());
                    request.setInd(currentRequest.getInd());
                    request.setCor(currentRequest.getCor());
                    request.setJs(js);
                    page.addTargetRequest(request.get());
                }
            }
        }
    }


    public Site getSite() {
        return site;
    }

    //爬取列表页面和所有分页
    private void crawList(Page page){
        int currentPage = Integer.parseInt(page.getHtml().xpath("//li[@class='page-item page-btn page-btn-current']/text()").get());
        if(currentPage==1){
            List<String> pages = page.getHtml().xpath("//ul[@class='pages-list']/li/text()").all();
            int lastPage = Integer.parseInt(pages.get(pages.size()-1));
            for(int i=currentPage;i<=lastPage;i++){
                SouRequest souRequest=SouRequest.getFromListRequest(new ListRequest().parse(page.getUrl().get()));
                souRequest.setPg(i);
                souRequest.setSt(SouRequest.NORMAL);
                page.addTargetRequest(souRequest.get());
            }
        }
    }

    private QUERY getQuery(String title){
        if(title.compareTo("工作地点:")==0){
            return QUERY.CITY;
        }
        if(title.compareTo("职位类型:")==0){
            return QUERY.JT;
        }
        if(title.compareTo("行业类型:")==0){
            return QUERY.IND;
        }
        if (title.compareTo("公司性质:")==0){
            return QUERY.COR;
        }
        if(title.compareTo("职位来源:")==0){
            return QUERY.JS;
        }
        return QUERY.OTHER;
    }
}
