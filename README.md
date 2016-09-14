
仿知乎日报客户端（如侵权，删）
=======
用知乎的API仿照了知乎日报几个页面

![](/screenshot/main.jpg)

![](/screenshot/detail.jpg)

用了四个接口：


host = news-at.zhihu.com


Splash 页面:{host}/api/4/start-image/{width*height}


最新：{host}/api/4/news/latest


以前的日报：{host}/api/4/news/before/{date}  //date 格式20160914


详情页：{host}/api/4/news/{storyId}

License
--------

Copyright 2016 will
