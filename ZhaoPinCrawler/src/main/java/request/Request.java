package request;

/**
 * 访问请求接口
 * @author debonet
 */
public interface Request {
    String get();
    Request parse(String str);
}
