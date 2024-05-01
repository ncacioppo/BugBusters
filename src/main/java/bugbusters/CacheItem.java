package bugbusters;

public class CacheItem {
    private Object element;
    private int size;
    private long timeAdded;

    public CacheItem(Object element, int size) {
        timeAdded = System.currentTimeMillis();
        this.element = element;
    }

    public Object getElement() {
        return element;
    }

    public int getSize() {
        return size;
    }

    public long getTimeAdded() {
        return timeAdded;
    }
}
