package csci498.gnanda.lunchList;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class LunchList extends TabActivity {
	
	private List<Restaurant> model = new ArrayList<Restaurant>();
	private RestaurantAdapter adapter = null;
//	private RadioButton a = null;
	private RadioGroup types = null;
	
	private List<String> addresses = new ArrayList<String>();
	private ArrayAdapter<String> addressesAdapter = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);    
        
        types = (RadioGroup) findViewById(R.id.types);
        
//	    a = new RadioButton(this);
//	    a.setText("testButton");
//	    types.addView(a);

        setUpListAdapter();
        setUpTabs();
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
		//adapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1, model);
		adapter = new RestaurantAdapter();
		list.setAdapter(adapter);
		
		addressesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, addresses);
	}

	private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Restaurant r = new Restaurant();
			
			EditText name = (EditText) findViewById(R.id.name);
			AutoCompleteTextView address = (AutoCompleteTextView) findViewById(R.id.addr);
			address.setAdapter(addressesAdapter);
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());
			
			addRadioGroupType(r);
			
			adapter.add(r);
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
	
