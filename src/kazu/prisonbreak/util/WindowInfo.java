package kazu.prisonbreak.util;

public class WindowInfo {
	
	private static WindowInfo instance;
	
	private int windowWidth;
	private int windowHeight;
	
	private WindowInfo(){
	}
	
	public static WindowInfo getInstance(){
		if(instance != null){
			return instance;
		}
		WindowInfo windowInfo = new WindowInfo();
		instance = windowInfo;
		return instance;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}
}
