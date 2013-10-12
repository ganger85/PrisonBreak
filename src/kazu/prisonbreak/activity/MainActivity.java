package kazu.prisonbreak.activity;

import kazu.prisonbreak.R;
import kazu.prisonbreak.util.WindowInfo;
import kazu.prisonbreak.view.PrisonBreakView;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static String ICICLE_KEY = "CRASH_BALL";
	private PrisonBreakView prisonBreakView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		Point size = new Point();
		disp.getSize(size);
		WindowInfo windowInfo = WindowInfo.getInstance();
		windowInfo.setWindowWidth(size.x);
		windowInfo.setWindowHeight(size.y);
		
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
