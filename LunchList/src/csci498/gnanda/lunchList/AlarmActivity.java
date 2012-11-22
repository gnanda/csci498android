package csci498.gnanda.lunchList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class AlarmActivity extends Activity implements MediaPlayer.OnPreparedListener {
	
	public static final String ALARM_RINGTONE = "alarm_ringtone";
	MediaPlayer player = new MediaPlayer();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String sound = prefs.getString(ALARM_RINGTONE, null);
		
		if (sound != null) {
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			
			try {
				player.setDataSource(sound);
				player.setOnPreparedListener(this);
				player.prepareAsync();
			}
			catch (Exception e) {
				Log.e("LunchList", "Exception in playing ringtone", e);
			}
		}
	}
	
	@Override
	public void onPause() {
		if (player.isPlaying()) {
			player.stop();
		}
		super.onPause();
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		player.start();
	}

}
