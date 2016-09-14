package will.wzhihu.main;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;
import will.wzhihu.WApplication;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.store.StoryStore;
import will.wzhihu.common.utils.CollectionUtils;
import will.wzhihu.common.utils.DateUtils;
import will.wzhihu.common.utils.StringUtils;
import will.wzhihu.main.client.StoryClient;
import will.wzhihu.main.model.Latest;
import will.wzhihu.main.model.Stories;
import will.wzhihu.main.model.Story;

/**
 * @author wendeping
 */
public class StoryFinder {
    private static final String TAG = "StoryFinder";

    @Inject
    StoryClient storyClient;

    @Inject
    StoryStore storyStore;

    public StoryFinder() {
        WApplication.getInjector().inject(this);
    }

    public Observable<Latest> getLatest() {
        final List<Story> storiesByDate = storyStore.getStoriesByDate(DateUtils.getTodayDateString());
        if (!CollectionUtils.isEmpty(storiesByDate)) {
            Log.d(TAG, "get latest from db");
            return Observable.just(migrateLatest(storiesByDate));
        }
        return storyClient.getLatest().map(new Func1<Latest, Latest>() {
            @Override
            public Latest call(Latest latest) {
                Log.d(TAG, "get latest from client success");
                List<Story> storyList = convertLatest(latest);
                List<Story> topStories = latest.topStories;

                addDate(latest.date, topStories);

                ArrayList<Story> stories = new ArrayList<Story>();
                stories.addAll(topStories);
                stories.addAll(storyList);

                storyStore.putAll(stories);
                Log.d(TAG, "put latest to db");
                return new Latest(latest.date, storyList, latest.topStories);
            }
        });
    }

    private List<Story> addDate(String date, List<Story> stories) {
        for (Story story : stories) {
            story.date = date;
        }

        return stories;
    }


    private Latest migrateLatest(List<Story> stories) {
        List<Story> topStories = new ArrayList<>();
        List<Story> listStories = new ArrayList<>();

        for (Story story : stories) {
            if (StringUtils.isEqual(story.getItemType(), Story.ITEM_TYPE_TOP_STORY)) {
                if (story.id == Story.TOP_STORY_ID) {
                    listStories.add(0, story);
                } else {
                    topStories.add(story);
                }
            } else {
                listStories.add(story);
            }
        }
        return new Latest(stories.get(0).date, listStories, topStories);
    }

    private List<Story> convertLatest(Latest latest) {
        if (null == latest) {
            return null;
        }
        List<Story> resultStories = new ArrayList<>();

        if (!CollectionUtils.isEmpty(latest.topStories)) {
            for (Story story : latest.topStories) {
                story.setItemType(Story.ITEM_TYPE_TOP_STORY);
            }

            Story topStory = new Story();
            topStory.setItemType(Story.ITEM_TYPE_TOP_STORY);
            topStory.id = Story.TOP_STORY_ID;
            topStory.date = latest.date;
            resultStories.add(0, topStory);
        }

        resultStories.addAll(convertStories(latest.date, latest.stories));
        return resultStories;
    }

    public Observable<Stories> getStories(final String currentDate) {
        String beforeDay = DateUtils.subtractDay(currentDate);
        final List<Story> storiesByDate = storyStore.getStoriesByDate(beforeDay);
        if (CollectionUtils.isEmpty(storiesByDate)) {
            Log.d(TAG, "load date %s story from network", currentDate);
            return storyClient.getBefore(currentDate).map(new Func1<Stories, Stories>() {
                @Override
                public Stories call(Stories stories) {
                    List<Story> storyList = convertStories(stories.date, stories.stories);
                    storyStore.putAll(storyList);
                    Log.d(TAG, "put date %s stories to store", currentDate);
                    return new Stories(stories.date, storyList);
                }
            });
        } else {
            Log.d(TAG, "load date %s story from db", currentDate);
            return Observable.just(new Stories(beforeDay, filterTopStories(storiesByDate)));
        }
    }

    private List<Story> filterTopStories(List<Story> stories) {
        List<Story> list = new ArrayList<>();
        for (Story story: stories) {
            if(StringUtils.isEqual(story.getItemType(), Story.ITEM_TYPE_TOP_STORY)) {
                continue;
            }
            list.add(story);
        }

        return list;
    }

    private List<Story> convertStories(String date, List<Story> stories) {
        List<Story> resultStories = new ArrayList<>();
        Story dateStory = new Story();
        dateStory.date = date;
        dateStory.setItemType(Story.ITEM_TYPE_DATE);
        resultStories.add(dateStory);

        if (!CollectionUtils.isEmpty(stories)) {
            addDate(date, stories);
            resultStories.addAll(stories);
        }

        return resultStories;
    }
}
