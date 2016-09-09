package will.wzhihu.common.model;

import java.util.ArrayList;
import java.util.List;

public class Container<T> {
    private List<T> items = new ArrayList<>();
    private Object afterScrollId;
    private Object beforeScrollId;

    public Container() {
    }

    public Container(List<T> items, Object scrollId) {
        this(items, scrollId, null);
    }

    public Container(List<T> items, Object afterScrollId, Object beforeScrollId) {
        this.items = items;
        this.afterScrollId = afterScrollId;
        this.beforeScrollId = beforeScrollId;
    }


    public Object getAfterScrollId() {
        return afterScrollId;
    }

    public void setAfterScrollId(Object scrollId) {
        this.afterScrollId = scrollId;
    }

    public Object getBeforeScrollId() {
        return beforeScrollId;
    }

    public void setBeforeScrollId(Object beforeScrollId) {
        this.beforeScrollId = beforeScrollId;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
