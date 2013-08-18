package kazu.prisonbreak;

import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static String ICICLE_KEY = "CRASH_BALL";
	private PrisonBreakView prisonBreakView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		prisonBreakView = (PrisonBreakView) findViewById(R.id.ball);
		prisonBreakView.setText((TextView) findViewById(R.id.message));
		prisonBreakView.setPointTextView((TextView) findViewById(R.id.stock_balls));

		if (savedInstanceState == null) {
			 prisonBreakView.setMode(PrisonBreakView.READY);
		} else {
			Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
			if (null != map) {
				prisonBreakView.restoreState(map);
			} else {
				 prisonBreakView.setMode(PrisonBreakView.READY);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		prisonBreakView.setMode(PrisonBreakView.PAUSE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return (prisonBreakView.onTouchEvent(event));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Bundle icicle = new Bundle();
		outState.putBundle(ICICLE_KEY, prisonBreakView.saveState(icicle));
	}

}
