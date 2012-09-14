package csci498.gnanda.lunchList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class LunchList extends TabActivity {

	private List<Restaurant> model = new ArrayList<Restaurant>();
	private RestaurantAdapter adapter = null;
	private RadioGroup types = null;
	private EditText name = null;
	private AutoCompleteTextView address = null;
	private EditText notes = null;
	private Restaurant current = null;
	private int progress;
	private AtomicBoolean isActive = new AtomicBoolean(true);

	private List<String> addresses = new ArrayList<String>();
	private ArrayAdapter<String> addressesAdapter = null;

	private Runnable longTask = new Runnable() {
		public void run() {
			for (int i = progress; i < 10000 && isActive.get(); i += 200) {
				doSomeLongWork(200);
			}
			
			if (isActive.get()) {
				runOnUiThread(new Runnable() {
					public void run() {
						setProgressBarVisibility(false);
						progress = 0;
					}
				});
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.main);

		name = (EditText) findViewById(R.id.name);
		address = (AutoCompleteTextView) findViewById(R.id.addr);
		types = (RadioGroup) findViewById(R.id.types);
		notes = (EditText) findViewById(R.id.notes);
		addressesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, addresses);
		address.setAdapter(addressesAdapter);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(onSave);    

		setUpListAdapter();
		setUpTabs();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		isActive.set(false);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		isActive.set(true);
		if (progress > 0) {
			startWork();
		}
	}
	
	private void startWork() {
		setProgressBarVisibility(true);
		new Thread(longTask).start();
	}
	
	private void setUpTabs() {
		TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
		spec.setContent(R.id.restaurants);
		spec.setIndicator("List", getResources().getDrawable(R.drawable.list));

		getTabHost().addTab(spec);
		spec = getTabHost().newTabSpec("tag2");
		spec.setContent(R.id.details);
		spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));

		getTabHost().addTab(spec);
		getTabHost().setCurrentTab(0);
	}

	private void setUpListAdapter() {
		ListView list = (ListView) findViewById(R.id.restaurants);
		adapter = new RestaurantAdapter();
		list.setAdapter(adapter);
		list.setOnItemClickListener(onListClick);
	}

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			current = model.get(position);		    
			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());

			if (current.getType().equals("sit_down")) {
				types.check(R.id.sit_down);
			}
			else if (current.getType().equals("take_out")) {
				types.check(R.id.take_out);
			}
			else {
				types.check(R.id.delivery);
			}

			getTabHost().setCurrentTab(1);
		}

	};

	private View.OnClickListener onSave = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			current = new Restaurant();
			current.setName(name.getText().toString());
			current.setAddress(address.getText().toString());
			current.setNotes(notes.getText().toString());

			addRadioGroupType(current);

			adapter.add(current);
			addressesAdapter.add(address.getText().toString());
		}

	};

	private void addRadioGroupType(Restaurant r) {

		switch (types.getCheckedRadioButtonId()) {
		case R.id.sit_down:
			r.setType("sit_down");
			break;

		case R.id.take_out:
			r.setType("take_out");
			break;

		case R.id.delivery:
			r.setType("delivery");
			break;
		}
	}    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		return super.onCreateOptionsMenu(menu);		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.toast) {
			String message = "No restaurant selected";

			if (current != null) {
				message = current.getNotes();
			}

			Toast.makeText(this, message, Toast.LENGTH_LONG).show();

			return true;
		}
		else if (item.getItemId() == R.id.run) {
			startWork();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void doSomeLongWork(final int incr) {
		runOnUiThread(new Runnable() {
			public void run() {
				progress += incr;
				setProgress(progress);
			}
		});
		
		SystemClock.sleep(250);
	}

	class RestaurantAdapter extends ArrayAdapter<Restaurant> {

		RestaurantAdapter() {
			super(LunchList.this,
					android.R.layout.simple_list_item_1,
					model);
		}

		public View getView(int position, View convertView,	ViewGroup parent) {
			View row = convertView;
			RestaurantHolder holder = null;

			if (row == null) {                         
				LayoutInflater inflater = getLayoutInflater();

				row = inflater.inflate(R.layout.row, parent, false);
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			}
			else {
				holder = (RestaurantHolder) row.getTag();
			}

			holder.populateFrom(model.get(position));

			return(row);
		}

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

		void populateFrom(Restaurant r) {
			name.setText(r.getName());
			address.setText(r.getAddress());

			if (r.getName().equals("Qdoba"))
				name.setTextColor(Color.YELLOW);

			if (r.getType().equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			}
			else if (r.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			}
			else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}

	}

}

