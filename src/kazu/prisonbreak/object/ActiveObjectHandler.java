package kazu.prisonbreak.object;

import android.os.Handler;
import android.os.Message;

public class ActiveObjectHandler extends Handler {

	private ActiveObject activeObject;

	public ActiveObjectHandler(ActiveObject activeObject) {
		this.activeObject = activeObject;
	}

	@Override
	public void handleMessage(Message message) {
		this.activeObject.update();
	}

	public void sleep(long sleepTime) {
		removeMessages(0);
		sendMessageDelayed(obtainMessage(0), sleepTime);
	}

}
