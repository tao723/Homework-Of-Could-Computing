package request;

/**
 * 详细页面请求
 * @author debonet
 */
public class DetailRequest implements Request{

    public static final String FORMAT="https://xiaoyuan.zhaopin.com/job/[A-Z0-9]*\\?\\S+";
    private String jobPositionNumber;
    private String traceUrl;
    private String kw;
    public DetailRequest(String jobPositionNumber,String traceUrl,String kw) {
        this.jobPositionNumber = jobPositionNumber;
        this.traceUrl=traceUrl;
        this.kw=kw;
    }

    public DetailRequest() {
        this.jobPositionNumber = "";
    }

    public String get() {
        return "https://xiaoyuan.zhaopin.com/job/"+jobPositionNumber+"?"+traceUrl+"?"+kw;
    }

    public DetailRequest parse(String str) {
        if(!str.matches(FORMAT))return new DetailRequest();
        this.jobPositionNumber=str.substring(str.lastIndexOf("/")+1,str.indexOf("?"));
        String[] list = str.split("\\?");
        this.traceUrl=list[1];
        this.kw=list[2];
        return this;
    }


    public String getJobPositionNumber() {
        return jobPositionNumber;
    }

    public void setJobPositionNumber(String jobPositionNumber) {
        this.jobPositionNumber = jobPositionNumber;
    }

    public String getTraceUrl() {
        return traceUrl;
    }

    public void setTraceUrl(String traceUrl) {
        this.traceUrl = traceUrl;
    }

    public String getKw() {
        return kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }
}
