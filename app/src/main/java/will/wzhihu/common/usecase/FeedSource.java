package will.wzhihu.common.usecase;

import android.support.v4.util.Pair;

import java.util.Comparator;
import will.wzhihu.common.model.FeedItem;

public abstract class FeedSource<T extends FeedItem> extends ListSource<T> {

    public static final boolean BEFORE = false;

    public final static boolean AFTER = true;

    public String mFeedId;

    protected static Comparator<Comparable> ASC = new Comparator<Comparable>() {
        @Override
        public int compare(Comparable lhs, Comparable rhs) {
            return lhs.compareTo(rhs);
        }
    };

    protected static Comparator<Comparable> DESC =
        new Comparator<Comparable>() {
            @Override
            public int compare(Comparable lhs, Comparable rhs) {
                return rhs.compareTo(lhs);
            }
        };


    protected static String generateScrollIdKey(String feedId, boolean after) {
        return feedId + ":scrollId:" + (after ? "after" : "before");
    }

    public FeedSource(String feedId) {
        this.mFeedId = feedId;
    }

    public abstract String[] getFeedItemTypes();

    public Comparator<Comparable> comparator() {
        return DESC;
    }

    public Pair<String, String> getFeedIdentifier() {
        return new Pair<>(mFeedId, null);
    }

}
