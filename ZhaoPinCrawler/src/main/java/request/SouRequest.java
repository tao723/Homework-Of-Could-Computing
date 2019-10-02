package request;

public class SouRequest implements Request {

    public static final String FORMAT="https://xiaoyuan.zhaopin.com/api/sou?\\S+";

    //查询类型
    //普通查询
    public static final int NORMAL=-1;
    //获取城市id列表
    public static final int CITY=0;
    //获取职业类型列表
    public static final int JT=1;
    //获取产业类型列表
    public static final int IND=2;
    //获取公司性质列表
    public static final int COR=3;
    //获取招聘来源列表
    public static final int JS=4;

    private String kw;
    private int pg;
    private int jn;
    private int jt;
    private int city;
    private int ind;
    private int js;
    private int cor;
    private int order;
    private int st;

    public SouRequest() {
        this.st=0;
        this.jn=-1;
        this.kw="";
        this.pg=-1;
        this.order=-1;
        this.city=-1;
        this.jt=-1;
        this.ind=-1;
        this.cor=-1;
        this.js=-1;
    }

    public SouRequest(String kw, int pg, int jn, int jt, int city, int ind, int js, int cor, int order,int st) {
        this.kw = kw;
        this.pg = pg;
        this.jn = jn;
        this.jt = jt;
        this.city = city;
        this.ind = ind;
        this.js = js;
        this.cor = cor;
        this.order = order;
        this.st = st;
    }


    public SouRequest(String kw) {
        this.jn=-1;
        this.kw=kw;
        this.pg=1;
        this.order=-1;
        this.city=-1;
        this.jt=-1;
        this.ind=-1;
        this.cor=-1;
        this.js=-1;
        this.st=-1;
    }

    public String get() {
        return "https://xiaoyuan.zhaopin.com/api/sou?sourceClient=sou" +
                "&keyWord=" + kw+
                "&jobSource=" + (js==-1?"-1":js) +
                "&pageNumber=" + (pg==-1?"1":pg)+
                "&jobNatures=" + (jn==-1?"2":jn)+
                "&jobTypeId=" + (jt==-1?"":jt)+
                "&cityId=" + (city==-1?"":city)+
                "&industryId=" + (ind==-1?"":ind)+
                "&companyTypeId=" + (cor==-1?"":cor)+
                "&orderBy=" + (order==-1?"1":order)+
                "&ss=" + st;
    }

    public SouRequest parse(String str) {
        if(!str.matches(FORMAT))return new SouRequest();
        String[] list = str.split("&");
        this.kw=list[1].substring(8);
        this.js=parseItem(list[2]);
        this.pg=parseItem(list[3]);
        this.jn=parseItem(list[4]);
        this.jt=parseItem(list[5]);
        this.city=parseItem(list[6]);
        this.ind=parseItem(list[7]);
        this.cor=parseItem(list[8]);
        this.order=parseItem(list[9]);
        this.st=parseItem(list[10]);
        return this;
    }

    private int parseItem(String item){
        String[] ss=item.split("=");
        if(ss.length==1)return 0;
        else return Integer.parseInt(ss[1]);
    }

    public String getKw() {
        return kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }

    public int getPg() {
        return pg;
    }

    public void setPg(int pg) {
        this.pg = pg;
    }

    public int getJn() {
        return jn;
    }

    public void setJn(int jn) {
        this.jn = jn;
    }

    public int getJt() {
        return jt;
    }

    public void setJt(int jt) {
        this.jt = jt;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getInd() {
        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public int getJs() {
        return js;
    }

    public void setJs(int js) {
        this.js = js;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }
}
