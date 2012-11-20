package csci498.gnanda.lunchList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class LunchFragment extends ListFragment {

	private static final String SORT_ORDER = "sort_order";
	public static final String ID_EXTRA = "apt.tutorial._ID";
	private Cursor model = null;
	private RestaurantAdapter adapter = null;
	private RestaurantHelper helper = null;
	private SharedPreferences prefs = null;
	private OnRestaurantListener listener;

	private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
				
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals(SORT_ORDER)) {
				initList();
			}
		}
		
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		helper = new RestaurantHelper(getActivity());
		initList();
		prefs.registerOnSharedPreferenceChangeListener(prefListener);
	}
	
	private void initList() {
		if (model != null) {
			model.close();
		}
		
		model = helper.getAll(prefs.getString(SORT_ORDER, "name"));
		adapter = new RestaurantAdapter(model);
		setListAdapter(adapter);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.option, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			startActivity(new Intent(getActivity(), DetailForm.class));
			return true;
		}
		else if (item.getItemId() == R.id.prefs) {
			startActivity(new Intent(getActivity(), EditPreferences.class));
			return true;
		}
		else if (item.getItemId() == R.id.help) {
			startActivity(new Intent(getActivity(), HelpPage.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		helper.close();
		super.onPause();		
	}
	
	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		if (listener != null) {
			listener.onRestaurantSelected(id);
		}
	}

	class RestaurantAdapter extends CursorAdapter {

		RestaurantAdapter(Cursor c) {
			super(getActivity(), c);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder) row.getTag();
			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			
			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);
			
			return row;
		}

	}
	
	public void setOnRestaurantListener(OnRestaurantListener listener) {
		this.listener = listener;
	}
	
	public interface OnRestaurantListener {
		void onRestaurantSelected(long id);
	}

	static class RestaurantHolder {

		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;

		RestaurantHolder(View row) {
			name = (TextView) row.findViewById(R.id.title);
			address = (TextView) row.findViewById(R.id.address);
			icon = (ImageView) row.findViewById(R.id.icon);
		}

		void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));

			if (helper.getType(c).equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			}
			else if (helper.getType(c).equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			}
			else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}

	}

}
