package csci498.gnanda.lunchList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DetailForm extends Activity {
	
	private EditText name = null;
	private EditText address = null;
	private EditText notes = null;
	private EditText feed = null;
	private RadioGroup types = null;
	private RestaurantHelper helper = null;
	private String restaurantId = null;		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		helper=new RestaurantHelper(this);
		getWidgetsFromXML();
		restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
		if (restaurantId != null) {
			load();
		}
	}
			
	@Override
	protected void onDestroy() {
		super.onDestroy();
		helper.close();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		name.setText(savedInstanceState.getString("name"));
		address.setText(savedInstanceState.getString("address"));
		notes.setText(savedInstanceState.getString("notes"));
		types.check(savedInstanceState.getInt("type"));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putString("name", name.getText().toString());
		outState.putString("address", address.getText().toString());
		outState.putString("notes", notes.getText().toString());
		outState.putInt("type", types.getCheckedRadioButtonId());		
	}

	@Override
	public void onPause() {
		save();
		super.onPause();
	}
	
	private void getWidgetsFromXML() {
		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.addr);
		types = (RadioGroup) findViewById(R.id.types);
		notes = (EditText) findViewById(R.id.notes);
		feed = (EditText) findViewById(R.id.feed);
	}
	
	private void load() {
		Cursor c = helper.getById(restaurantId);
		
		c.moveToFirst();
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		
		if (helper.getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		}
		else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		}
		else {
			types.check(R.id.delivery);
		}
		c.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.details_options, menu);		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if(isNetworkAvailable()) {
				Intent i = new Intent(this, FeedActivity.class);
				i.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(i);
			}
			else {
				Toast.makeText(this, "Sorry, the Internet is not available", Toast.LENGTH_LONG).show();
			}
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null;
	}

	private void save() {
		String type = null;

		switch (types.getCheckedRadioButtonId()) {
		case R.id.sit_down:
			type = "sit_down";
			break;
		case R.id.take_out:
			type = "take_out";
			break;
		case R.id.delivery:
			type = "delivery";
			break;
		}

		if (restaurantId == null) {
			helper.insert(name.getText().toString(), address.getText().toString(), type, notes.getText().toString(), feed.getText().toString());
		}
		else {
			helper.update(restaurantId, name.getText().toString(), address.getText().toString(), type, notes.getText().toString(), feed.getText().toString());
		}

		finish();
	}
	
}
