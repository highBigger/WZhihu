package will.wzhihu.detail.binder;

import android.util.Log;
import android.webkit.WebChromeClient;
import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.common.widget.WrapWebView;
import will.wzhihu.detail.presenter.DetailPresenter;

/**
 * @author wendeping
 */
public class WebViewBinder extends CompositeBinder {
    private static final String TAG = "WebViewBinder";

    public WebViewBinder(final WrapWebView webView, final DetailPresenter presenter) {
        webView.getSettings().setDefaultTextEncodingName("UTF-8");

        add(new Binder() {
            @Override
            public void bind() {
                webView.setWebChromeClient(new WebChromeClient());
            }

            @Override
            public void unbind() {
                webView.setWebChromeClient(null);
            }
        });

        presenter.addPropertyChangeListener("body", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                //TODO if just use load data then chinese show error
                //use local css later
                webView.loadDataWithBaseURL("", getHTMLData(presenter.getCss(), presenter.getBody()), "text/html", "utf-8", null);
            }
        });
    }

    private String getHTMLData(String css, String body) {
        //TODO fix it later
        String startString = "content-wrap\">";
        int i = body.indexOf(startString);
        int startIndex = 0;
        if (i != -1) {
            startIndex = i + startString.length();
        }
        String header = body.substring(0, startIndex);
        Log.d(TAG, header);

        String endString = "<div class=\"content-inner\">";
        int endIndex = body.indexOf(endString);
        String bodyString = body.substring(endIndex);
        Log.d(TAG, bodyString);

        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<body>");
        html.append("<link rel=stylesheet href='"+ css + "'>");
        html.append(header);
        html.append(bodyString);
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
