package request;

/**
 * 获取列表的请求封装
 */
public class ListRequest implements Request{

    public static String FORMAT ="https://xiaoyuan.zhaopin.com/search/jn=[0-9]*&kw=\\S*&pg=[0-9]*&order=[0-9]*&city=[0-9]*&jt=[0-9]*&ind=[0-9]*&cor=[0-9]*&js=[0-9]*";
    private String kw;
    private int jn;
    private int pg;
    private int order;
    private int city;
    private int jt;
    private int ind;
    private int cor;
    private int js;

    public ListRequest(int jn,String kw,int pg,int order,int city,int jt,int ind,int cor,int js) {
        this.jn=jn;
        this.kw=kw;
        this.pg=pg;
        this.order=order;
        this.city=city;
        this.jt=jt;
        this.ind=ind;
        this.cor=cor;
        this.js=js;
    }
    public ListRequest(String kw){
        this.jn=-1;
        this.kw=kw;
        this.pg=1;
        this.order=1;
        this.city=-1;
        this.jt=-1;
        this.ind=-1;
        this.cor=-1;
        this.js=-1;
    }
    public ListRequest(){
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

    public ListRequest parse(String request){
        if(request.matches(FORMAT)){
            String[] list=request.split("&");
            this.kw=list[1].split("=")[1];
            this.pg=parseItem(list[2]);
            this.order=parseItem(list[3]);
            this.city=parseItem(list[4]);
            this.jt=parseItem(list[5]);
            this.ind=parseItem(list[6]);
            this.cor=parseItem(list[7]);
            this.js=parseItem(list[8]);
            return this;
        }
        else return new ListRequest();
    }

    public String get(){
        return "https://xiaoyuan.zhaopin.com/search/"+
                "jn="+(jn==-1?2:jn)+
                "&kw="+kw+
                "&pg="+(pg==-1?1:pg)+
                "&order="+(order==-1?1:order)+
                "&city="+(city==-1?"":city)+
                "&jt="+(jt==-1?"":jt)+
                "&ind="+(ind==-1?"":ind)+
                "&cor="+(cor==-1?"":cor)+
                "&js="+(js==-1?"":js);
    }

    private int parseItem(String item){
        String[] ss=item.split("=");
        if(ss.length==1)return 0;
        else return Integer.parseInt(ss[1]);
    }




    public int getJn() {
        return jn;
    }

    public void setJn(int jn) {
        this.jn = jn;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getJt() {
        return jt;
    }

    public void setJt(int jt) {
        this.jt = jt;
    }

    public int getInd() {
        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    public int getJs() {
        return js;
    }

    public void setJs(int js) {
        this.js = js;
    }
}
