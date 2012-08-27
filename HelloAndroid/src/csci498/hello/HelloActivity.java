package csci498.hello;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelloActivity extends Activity implements View.OnClickListener {
	Button btn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn = new Button(this);
        btn.setOnClickListener(this);
        updateTime();
        setContentView(btn);
    }

	@Override
	public void onClick(View arg0) {
		updateTime();
	}
    
	private void updateTime() {
		btn.setText(new Date().toString());
	}
    
}