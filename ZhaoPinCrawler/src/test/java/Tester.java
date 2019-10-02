import org.junit.Test;
import request.ListRequest;
import request.SouRequest;

import static junit.framework.TestCase.assertEquals;

public class Tester {

    @Test
    public void SouRequestTest1(){
        SouRequest request=new SouRequest().parse("https://xiaoyuan.zhaopin.com/api/sou?sourceClient=sou&keyWord=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&jobSource=-1&pageNumber=1&jobNatures=2&jobTypeId=&cityId=&industryId=&companyTypeId=&orderBy=1&ss=0");
        assertEquals("%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86",request.getKw());
    }

    @Test
    public void test(){
        ListRequest listRequest = new ListRequest().parse("https://xiaoyuan.zhaopin.com/search/jn=2&kw=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&pg=1&order=1&city=530&jt=0&ind=&cor=&js=");
        assertEquals(0,listRequest.getJt());
    }

    @Test
    public void test1(){
        ListRequest listRequest = new ListRequest().parse("https://xiaoyuan.zhaopin.com/search/jn=2&kw=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&pg=1&order=1&city=530&jt=0&ind=&cor=&js=");
        SouRequest souRequest = new SouRequest();
        souRequest.setJt(listRequest.getJt());
        souRequest.setCity(listRequest.getCity());
        souRequest.setKw(listRequest.getKw());
        souRequest.setSt(SouRequest.JT);
        assertEquals("https://xiaoyuan.zhaopin.com/api/sou?sourceClient=sou&keyWord=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&jobSource=&pageNumber=1&jobNatures=2&jobTypeId=0&cityId=530&industryId=&companyTypeId=&orderBy=1&ss=1",souRequest.get());
    }

    @Test
    public void test2(){
        SouRequest souRequest=new SouRequest().parse("https://xiaoyuan.zhaopin.com/api/sou?sourceClient=sou&keyWord=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&jobSource=0&pageNumber=1&jobNatures=2&jobTypeId=0&cityId=530&industryId=&companyTypeId=&orderBy=1&ss=1");
        ListRequest listRequest=new ListRequest();
        listRequest.setKw(souRequest.getKw());
        listRequest.setCity(souRequest.getCity());
        listRequest.setJt(souRequest.getJt());
        assertEquals("https://xiaoyuan.zhaopin.com/search/jn=2&kw=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&pg=1&order=1&city=530&jt=0&ind=&cor=&js=",listRequest.get());
    }

    @Test
    public void test3(){
        SouRequest souRequest=new SouRequest().parse("https://xiaoyuan.zhaopin.com/api/sou?sourceClient=sou&keyWord=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&jobSource=&pageNumber=0&jobNatures=2&jobTypeId=846&cityId=530&industryId=0&companyTypeId=0&orderBy=1&ss=-1");
        assertEquals(SouRequest.NORMAL,souRequest.getSt());
    }
}
