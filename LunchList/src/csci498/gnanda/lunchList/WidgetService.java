package csci498.gnanda.lunchList;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

public class WidgetService extends IntentService {

	private static final String SELECT_ID_NAME_FROM_RESTAURANTS = "SELECT _ID, name FROM restaurants LIMIT 1 OFFSET ?";
	private static final String SELECT_COUNT_FROM_RESTAURANTS = "SELECT COUNT(*) FROM restaurants";

	public WidgetService() {
		super("WidgetService");
	}

	// TODO: Change raw query to use database helper
	@Override
	protected void onHandleIntent(Intent intent) {
		ComponentName me = new ComponentName(this, AppWidget.class);
		RemoteViews updateViews = new RemoteViews("csci498.gnanda.lunchList", R.layout.widget);
		RestaurantHelper helper = new RestaurantHelper(this);
		AppWidgetManager mgr = AppWidgetManager.getInstance(this);
		
		try {
			Cursor c = helper.getReadableDatabase().rawQuery(SELECT_COUNT_FROM_RESTAURANTS, null);
			c.moveToFirst();
			
			int count = c.getInt(0);
			
			if (count > 0) {
				int offset = (int) (count*Math.random());
				String args[] = {String.valueOf(offset)};
				c = helper.getReadableDatabase().rawQuery(SELECT_ID_NAME_FROM_RESTAURANTS, args);
				c.moveToFirst();
				updateViews.setTextViewText(R.id.name, c.getString(1));
				
				Intent i = new Intent(this, DetailForm.class);
				
				i.putExtra(LunchList.ID_EXTRA, c.getString(0));
				
				PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
				updateViews.setOnClickPendingIntent(R.id.name, pi);
				c.close();
			}
			else {
				updateViews.setTextViewText(R.id.title, this.getString(R.string.empty));
			}
		}
		finally {
			helper.close();
		}
		
		Intent i = new Intent(this, WidgetService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		
		updateViews.setOnClickPendingIntent(R.id.next, pi);
		mgr.updateAppWidget(me, updateViews);
	}

}
