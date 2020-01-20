package application;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public class CursorGetHandle {
	public static final int POINT_Y(long i)
	{
	    return (int) (i  >> 32);
	}
	 
	public static final int POINT_X(long i)
	{
	    return (int) (i & 0xFFFF);
	}
	 
	public interface User32Ex extends StdCallLibrary 
	{  
	 User32Ex instance = (User32Ex) Native.loadLibrary("user32", User32Ex.class, W32APIOptions.DEFAULT_OPTIONS);  
	 boolean GetCursorPos(long[] lpPoint); //use macros POINT_X() and POINT_Y() on long lpPoint[0]
	    HWND WindowFromPoint(long point);
	}
	 
	
	

public static HWND main(String[] args) {
	 
	long[] getPos = new long [1];
	User32Ex.instance.GetCursorPos(getPos);
	HWND hwnd = User32Ex.instance.WindowFromPoint(getPos[0]);
	return hwnd;
}
}