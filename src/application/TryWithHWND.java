package application;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public class TryWithHWND {
	private HWND test;
public interface User32 extends StdCallLibrary {
    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class,W32APIOptions.DEFAULT_OPTIONS);

    boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);

    WinDef.HWND SetFocus(WinDef.HWND hWnd);

    int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);

    boolean SetForegroundWindow(WinDef.HWND hWnd);
    boolean EnumChildWindows(HWND hWndParent,WNDENUMPROC lpEnumFunc,LPARAM lParam);
    int GetClassName(HWND hWnd,char[] textBuffer,int nMaxCount);
    int GetWindowText(HWND hWnd,char[] textBuffer,int nMaxCount);
    long GetWindowLong(HWND hWnd, int index);
    int GetClassNameA(HWND in, byte[] lpString, int size);
    HWND FindWindowEx(HWND hwndParent, HWND hwndChildAfter, String lpszClass, String lpszWindow);


}

public static void main(String[] args) {
    /**final User32 user32 = User32.INSTANCE;
    user32.EnumWindows(new WNDENUMPROC() {
        int count = 0;

        public boolean callback(HWND hWnd, Pointer arg1) {
            byte[] windowText = new byte[512];
            user32.GetWindowTextA(hWnd, windowText, 512);
            String wText = Native.toString(windowText);

            // get rid of this if block if you want all windows regardless
            // of whether
            // or not they have text
            if (wText.isEmpty()) {
                return true;
            }

            System.out.println("Found window with text " + hWnd
                    + ", total " + ++count + " Text: " + wText); 
            
            if (wText.equals("七四式")) {
            	
            	user32.SetForegroundWindow(hWnd);
            	
            	
               
                return false;
            }
            return true;
        }
    }, null);*/
	
	
    //enumchildwin();
    
	
	
    // user32.SetFocus(hWnd);
}
public HWND test(HWND assist) {
	final User32 user32 = User32.INSTANCE;
	//System.setProperty("jna.encoding","BIG5");
    user32.EnumWindows(new WNDENUMPROC() {
       //int count = 0;
       @Override
       public boolean callback(HWND hWnd, Pointer arg1) {
          byte[] windowText = new byte[512];
          user32.GetWindowTextA(hWnd, windowText, 512);
          String wText = Native.toString(windowText);
          
          byte[] assistwindowText = new byte[512];
          user32.GetWindowTextA(assist, assistwindowText, 512);
          String awText = Native.toString(assistwindowText);

          // get rid of this if block if you want all windows regardless of whether
          // or not they have text
          if (wText.isEmpty()) {
             return true;
          }
          //if (wText.contains("七四式")) {
          if(wText.contains(awText)) {
        	  //System.out.println("OK");
        	  test = enumchildwin(hWnd);
        	  return true;
           }

          //System.out.println("Found window with text " + hWnd + ", total " + ++count
          //      + " Text: " + wText);
          return true;
       }
    }, null);
    return test;
	/**HWND childHwnd = User32.INSTANCE.FindWindowEx(hwnd, null, null, "Chrome_WidgetWin_0");
	return childHwnd;*/
}
public static HWND enumchildwin(HWND hwnd){
	HandleKeep tte = new HandleKeep();

	User32.INSTANCE.EnumWindows(new WNDENUMPROC() {
        public boolean callback(HWND hWnd, Pointer userData) { // this will be called for each parent window found by EnumWindows(). the hWnd parameter is the HWND of the window that was found.
            byte[] textBuffer = new byte[512];
            
            User32.INSTANCE.GetWindowTextA(hWnd, textBuffer, 512);
            String wText = Native.toString(textBuffer);
            
            byte[] assisttextBuffer = new byte[512];
            
            User32.INSTANCE.GetWindowTextA(hwnd, assisttextBuffer, 512);
            String awText = Native.toString(assisttextBuffer);
            //System.out.println("Window found: " + wText);

            //if (wText.contains("七四式")) {
            if(wText.contains(awText)) {
          	  //System.out.println("OK");
          	  //enumchildwin(hWnd);
          	  
          	  // now call EnumChildWindows() giving the previously found parent window as the first parameter
              User32.INSTANCE.EnumChildWindows(hWnd, new WNDENUMPROC() {
                  public boolean callback(HWND hWnd, Pointer userData) { // this is called for each child window that EnumChildWindows() finds - just like before with EnumWindows().
                      byte[] textBuffer = new byte[512];
                     
                      User32.INSTANCE.GetClassNameA(hWnd, textBuffer, 512);
                      //System.out.println(" - Found sub window / control class: " + hWnd + "/" + new String(textBuffer).trim());
                      
                      if (new String(textBuffer).trim().contains("Chrome_WidgetWin_0")) {
                          tte.setKeep(hWnd);
                          System.out.println(hWnd);
                      }
                      
                      return true;
                  }
              }, null);
             }
            
            
            return true;
        }
    }, null);
	return tte.getKeep();
}

}