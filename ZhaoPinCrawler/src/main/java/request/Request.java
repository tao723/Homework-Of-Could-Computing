package request;

public interface Request {
    String get();
    Request parse(String str);
}
