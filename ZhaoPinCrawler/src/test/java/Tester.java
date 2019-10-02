import org.junit.Test;
import request.SouRequest;

import static junit.framework.TestCase.assertEquals;

public class Tester {

    @Test
    public void SouRequestTest1(){
        SouRequest request=new SouRequest().parse("https://xiaoyuan.zhaopin.com/api/sou?sourceClient=sou&keyWord=%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86&jobSource=-1&pageNumber=1&jobNatures=2&jobTypeId=&cityId=&industryId=&companyTypeId=&orderBy=1&ss=0");
        assertEquals("%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86",request.getKw());
    }
}
