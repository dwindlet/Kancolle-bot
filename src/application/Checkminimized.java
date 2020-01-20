package application;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;


public class Checkminimized {
	public interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
		boolean IsIconic(HWND hWnd);
		boolean ShowWindow(HWND hWnd,int nCmdShow);
	}

	public static void minimizedcheckandshow(HWND hWnd){
		if(User32.INSTANCE.IsIconic(hWnd) != false){
			User32.INSTANCE.ShowWindow(hWnd, 9);
		}
	}

}
