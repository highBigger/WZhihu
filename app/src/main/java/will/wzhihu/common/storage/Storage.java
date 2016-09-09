package will.wzhihu.common.storage;


public interface Storage {

    int TTL_SESSION = -2;

    <T> T get(String key, Class<T> type);

    void put(String key, Object value);

    void put(String key, Object value, long ttl);

    void remove(String key);

    void clearSessionKeys();
}
