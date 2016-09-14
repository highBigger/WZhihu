package will.wzhihu.detail;

/**
 * Created by taoming on 2016/9/13.
 */

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import will.wzhihu.common.cupboard.WColumn;
import will.wzhihu.common.cupboard.WJSONFieldConverter;
import will.wzhihu.main.model.Story;

public class StoryDetail extends Story {

    public String body;

    @SerializedName("image_source")
    public String imageSource;

    @SerializedName("share_url")
    public String shareUrl;

    @WColumn(fieldConverter = WJSONFieldConverter.class)
    public List<String> js;

    @WColumn(fieldConverter = WJSONFieldConverter.class)
    public List<String> css;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.body);
        dest.writeString(this.imageSource);
        dest.writeString(this.shareUrl);
        dest.writeStringList(this.js);
        dest.writeStringList(this.css);
    }

    public StoryDetail() {
    }

    protected StoryDetail(Parcel in) {
        super(in);
        this.body = in.readString();
        this.imageSource = in.readString();
        this.shareUrl = in.readString();
        this.js = in.createStringArrayList();
        this.css = in.createStringArrayList();
    }

    public static final Creator<StoryDetail> CREATOR = new Creator<StoryDetail>() {
        @Override
        public StoryDetail createFromParcel(Parcel source) {
            return new StoryDetail(source);
        }

        @Override
        public StoryDetail[] newArray(int size) {
            return new StoryDetail[size];
        }
    };

    @Override
    public String toString() {
        return "StoryDetail{" +
            "body='" + body + '\'' +
            ", imageSource='" + imageSource + '\'' +
            ", shareUrl='" + shareUrl + '\'' +
            ", js=" + js +
            ", css=" + css +
            "} " + super.toString();
    }
}
