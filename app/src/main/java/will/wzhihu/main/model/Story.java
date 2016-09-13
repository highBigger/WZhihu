package will.wzhihu.main.model;

import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import will.wzhihu.common.model.FeedItem;
import will.wzhihu.common.utils.CollectionUtils;
import will.wzhihu.common.utils.StringUtils;

/**
 * @author wendeping
 */

/**
 *
 images: [
 "http://pic3.zhimg.com/12730f2ed82b5b899ca2594113a9309e.jpg"
 ],
 type: 0,
 id: 8779765,
 ga_prefix: "090919",
 title: "多一根「拐杖」，登山就会轻松很多
 */
public class Story implements FeedItem{
    public static final String ITEM_TYPE_STORY = "story";
    public static final String ITEM_TYPE_TOP_STORY = "top_story";
    public static final String ITEM_TYPE_DATE = "date";

    @StringDef({ITEM_TYPE_TOP_STORY, ITEM_TYPE_DATE, ITEM_TYPE_STORY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StoryItemType {}

    public List<String> images;

    public int type;

    public long id;

    @SerializedName("ga_prefix")
    public String gaPrefix;

    public String title;

    @SerializedName("multipic")
    public boolean multiPic;

    private String itemType = ITEM_TYPE_STORY;

    public String date;

    public String image;

    public void setItemType(@StoryItemType String itemType) {
       this.itemType = itemType;
    }

    @Override
    public Comparable getSort() {
        return null;
    }

    @Override
    public String getItemId() {
        return String.valueOf(id);
    }

    @Override
    public String getItemType() {
        return String.valueOf(itemType);
    }

    public String getImage() {
        if (StringUtils.isEqual(itemType, ITEM_TYPE_TOP_STORY)) {
            return image;
        }
        return CollectionUtils.isEmpty(images) ? null : images.get(0);
    }

    @Override
    public String toString() {
        return "Story{" +
            "images=" + images +
            ", type=" + type +
            ", id=" + id +
            ", gaPrefix='" + gaPrefix + '\'' +
            ", title='" + title + '\'' +
            ", multiPic=" + multiPic +
            ", itemType='" + itemType + '\'' +
            ", date='" + date + '\'' +
            '}';
    }
}