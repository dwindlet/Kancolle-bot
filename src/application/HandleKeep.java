package application;

import com.sun.jna.platform.win32.WinDef.HWND;

public class HandleKeep {
	private HWND keep;
	
	public void setKeep(HWND hWnd) {
		keep = hWnd;
		
	}
	
	public HWND getKeep() {
		return keep;
	}

}
