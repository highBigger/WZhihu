package will.wzhihu.main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author wendeping
 */
public class Latest {
    public String date;
    public List<Story> stories;

    @SerializedName("top_stories")
    public List<Story> topStories;

    public Latest(String date, List<Story> stories, List<Story> topStories) {
        this.date = date;
        this.stories = stories;
        this.topStories = topStories;
    }

    @Override
    public String toString() {
        return "Latest{" +
            "date='" + date + '\'' +
            ", stories=" + stories +
            ", topStories=" + topStories +
            '}';
    }
}
