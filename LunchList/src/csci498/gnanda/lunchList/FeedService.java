package csci498.gnanda.lunchList;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class FeedService extends IntentService {
	
	private static final String EXTRA_URL = "csci498.gnanda.lunchlist.EXTRA_URL";

	public FeedService() {
		super("FeedService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		RSSReader reader = new RSSReader();
		
		try {
			RSSFeed result = reader.load(intent.getStringExtra(EXTRA_URL));
		} catch (Exception e) {
			Log.e("LunchList", "Exception parsing feed", e);
		}
	}
	
}
