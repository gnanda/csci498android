package csci498.gnanda.lunchList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class ListViewsFactory implements RemoteViewsFactory {
	
	private static final String SELECT_ID_NAME_FROM_RESTAURANTS = "SELECT _ID, name FROM restaurants";
	private Context ctx;
	private RestaurantHelper helper;
	private Cursor restuarants;

	public ListViewsFactory(Context applicationContext, Intent intent) {
		this.ctx = applicationContext;
	}

	@Override
	public int getCount() {
		return restuarants.getCount();
	}

	@Override
	public long getItemId(int position) {
		restuarants.moveToPosition(position);
		return restuarants.getInt(0);
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews row = new RemoteViews(ctx.getPackageName(), R.layout.widget_row);
		restuarants.moveToPosition(position);
		row.setTextViewText(android.R.id.text1, restuarants.getString(1));
		
		Intent i = new Intent();
		Bundle extras = new Bundle();
		
		extras.putString(LunchList.ID_EXTRA, String.valueOf(restuarants.getInt(0)));
		i.putExtras(extras);
		row.setOnClickFillInIntent(android.R.id.text1, i);
		return row;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	// TODO: Change raw query to use database helper
	@Override
	public void onCreate() {
		helper = new RestaurantHelper(ctx);
		restuarants = helper.getReadableDatabase().rawQuery(SELECT_ID_NAME_FROM_RESTAURANTS, null);
	}

	@Override
	public void onDataSetChanged() {
		// NO-OP
	}

	@Override
	public void onDestroy() {
		restuarants.close();
		helper.close();
	}

}
