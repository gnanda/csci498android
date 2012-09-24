package csci498.gnanda.lunchList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	
	private EditText name = null;
	private AutoCompleteTextView address = null;
	private EditText notes = null;
	private RadioGroup types = null;
	private RestaurantHelper helper = null;
	private String restaurantId = null;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		getWidgetsFromXML();
		restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
	}
	
	private void getWidgetsFromXML() {
		name = (EditText) findViewById(R.id.name);
		address = (AutoCompleteTextView) findViewById(R.id.addr);
		types = (RadioGroup) findViewById(R.id.types);
		notes = (EditText) findViewById(R.id.notes);
		
		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(onSave);
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
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
		}
	};
}
