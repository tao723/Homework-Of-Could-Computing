package request;

public class DetailRequest implements Request{

    public static final String FORMAT="https://xiaoyuan.zhaopin.com/job/[A-Z0-9]*\\?\\S+";
    private String jobPositionNumber;
    private String traceUrl;
    public DetailRequest(String jobPositionNumber,String traceUrl) {
        this.jobPositionNumber = jobPositionNumber;
        this.traceUrl=traceUrl;
    }

    public DetailRequest() {
        this.jobPositionNumber = "";
    }

    public String get() {
        return "https://xiaoyuan.zhaopin.com/job/"+jobPositionNumber+"?"+traceUrl;
    }

    public DetailRequest parse(String str) {
        if(!str.matches(FORMAT))return new DetailRequest();
        this.jobPositionNumber=str.substring(str.lastIndexOf("/")+1,str.lastIndexOf("?"));
        String[] list = str.split("\\?");
        this.traceUrl=list[1];
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
}
