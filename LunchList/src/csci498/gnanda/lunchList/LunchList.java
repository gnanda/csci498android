package csci498.gnanda.lunchList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class LunchList extends FragmentActivity implements LunchFragment.OnRestaurantListener {
	
	public static final String ID_EXTRA = "apt.tutorial._ID";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		LunchFragment lunch = (LunchFragment) getSupportFragmentManager().findFragmentById(R.id.lunch);
		lunch.setOnRestaurantListener(this);
	}

	@Override
	public void onRestaurantSelected(long id) {
		if (findViewById(R.id.details) == null) {
			Intent i = new Intent(this, DetailForm.class);
			i.putExtra(ID_EXTRA, String.valueOf(id));
			startActivity(i);
		}
		else {
			FragmentManager fragMgr = getSupportFragmentManager();
			DetailFragment details = (DetailFragment) fragMgr.findFragmentById(R.id.details);
			
			if (details == null) {
				details = DetailFragment.newInstance(id);
				FragmentTransaction xaction = fragMgr.beginTransaction();
				
				xaction
					.add(R.id.details, details)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.addToBackStack(null)
					.commit();
			}
			else {
				details.loadRestaurant(String.valueOf(id));
			}
		}
	}

}
