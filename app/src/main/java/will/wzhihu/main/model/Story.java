package will.wzhihu.main.model;

import android.os.Parcel;
import android.os.Parcelable;
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
public class Story implements FeedItem , Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.images);
        dest.writeInt(this.type);
        dest.writeLong(this.id);
        dest.writeString(this.gaPrefix);
        dest.writeString(this.title);
        dest.writeByte(this.multiPic ? (byte) 1 : (byte) 0);
        dest.writeString(this.itemType);
        dest.writeString(this.date);
        dest.writeString(this.image);
    }

    public Story() {
    }

    protected Story(Parcel in) {
        this.images = in.createStringArrayList();
        this.type = in.readInt();
        this.id = in.readLong();
        this.gaPrefix = in.readString();
        this.title = in.readString();
        this.multiPic = in.readByte() != 0;
        this.itemType = in.readString();
        this.date = in.readString();
        this.image = in.readString();
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}
