package will.wzhihu.main.model;

import java.util.List;

/**
 * @author wendeping
 */
public class Stories {
    public String date;
    public List<Story> stories;

    public Stories(String date, List<Story> stories) {
        this.date = date;
        this.stories = stories;
    }
}
