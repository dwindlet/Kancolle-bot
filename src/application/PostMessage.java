package application;

import java.security.SecureRandom;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;


public class PostMessage {
	//private static SecureRandom sr = new SecureRandom();
	//private static long clickoffsettime = (sr.nextInt(2)+1)*1000;
	public static final int WM_LBUTTONUP = 514;
	public static final int WM_LBUTTONDOWN = 513;
	public static final int WM_MOUSEMOVE = 512;
	public static final int WM_KEYDOWN = 256;
	public static final int WM_KEYUP = 257;
	public static final int VK_F5 = 116;
	public static final int VK_RETURN = 13;
	public static final int VK_LEFT = 37;
	public static final String CONFIRM = "#32770";
	
	public interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
		void PostMessage(HWND hWnd,int msg,WinDef.WPARAM wParam,WinDef.LPARAM lParam);
		void PostMessage(Pointer hWnd,int msg,WinDef.WPARAM wParam,WinDef.LPARAM lParam);
		
		interface WNDENUMPROC extends StdCallCallback {
            boolean callback(Pointer hWnd, Pointer arg);
        }

        boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);
        
        //boolean EnumChildWindows()

        int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);
        
        HWND FindWindowExA(Pointer hwndParent, Pointer childAfter, String className, String windowName);
        
        boolean EnumChildWindows(Pointer hWndParent,WNDENUMPROC lpEnumFunc,Pointer lParam);
        
        int GetClassName(Pointer hWnd,char[] lpClassName,int nMaxCount);
        
	}
	public interface WinUser extends StdCallLibrary{
		WinUser INSTANCE2 = (WinUser) Native.loadLibrary("winuser", WinUser.class, W32APIOptions.DEFAULT_OPTIONS);
		void MAKEINTRESOURCEA(int i);
	}
		
	/**public static void test(HWND hwnd,int x,int y) throws InterruptedException{
		long z = x + (y << 16);//x + (y << 16)
	    WinDef.LPARAM l = new WinDef.LPARAM(z);
	    WinDef.WPARAM w = new WinDef.WPARAM(0);
	    
	    System.out.printf("click(%d,%d)",x,y);
	    //for(int i = 0;i<10;i++){
	    	User32.INSTANCE.PostMessage(hwnd, WM_MOUSEMOVE, w, l);
	   // }
	    Thread.sleep(1000);
	}*/


	public static void clickbypostmessage(HWND hwnd,int x,int y) throws InterruptedException {
		
		// TODO Auto-generated method stub
	    long z = x + (y << 16);//x + (y << 16)
	    WinDef.LPARAM l = new WinDef.LPARAM(z);
	    WinDef.WPARAM w = new WinDef.WPARAM(0);
	    //780 100
	    SecureRandom sr = new SecureRandom();
	    
	    long random = sr.nextInt(750)+20 + ((sr.nextInt(70)+20) << 16);
	    WinDef.LPARAM randomL = new WinDef.LPARAM(random);
	    
	    //WinDef.LPARAM zero = new WinDef.LPARAM(0);
	    
	    User32.INSTANCE.PostMessage(hwnd, WM_MOUSEMOVE, w, l);
	    Thread.sleep(20);
	    User32.INSTANCE.PostMessage(hwnd, WM_LBUTTONDOWN, w, l);
	    Thread.sleep(10);
	    User32.INSTANCE.PostMessage(hwnd, WM_LBUTTONUP, w, l);
	    Thread.sleep(20);
	    User32.INSTANCE.PostMessage(hwnd, WM_MOUSEMOVE, w, randomL);
	    
	}
	
	public static void clickbypostmessage_pointer(Pointer hwnd,int x,int y) throws InterruptedException {
		
		// TODO Auto-generated method stub
	    long z = x + (y << 16);//x + (y << 16)
	    WinDef.LPARAM l = new WinDef.LPARAM(z);
	    WinDef.WPARAM w = new WinDef.WPARAM(0);
	    //780 100
	    SecureRandom sr = new SecureRandom();
	    
	    long random = sr.nextInt(750)+20 + ((sr.nextInt(70)+20) << 16);
	    WinDef.LPARAM randomL = new WinDef.LPARAM(random);
	    
	    //WinDef.LPARAM zero = new WinDef.LPARAM(0);
	    
	    User32.INSTANCE.PostMessage(hwnd, WM_MOUSEMOVE, w, l);
	    Thread.sleep(20);
	    User32.INSTANCE.PostMessage(hwnd, WM_LBUTTONDOWN, w, l);
	    Thread.sleep(10);
	    User32.INSTANCE.PostMessage(hwnd, WM_LBUTTONUP, w, l);
	    Thread.sleep(20);
	    User32.INSTANCE.PostMessage(hwnd, WM_MOUSEMOVE, w, randomL);
	    
	}
	public static boolean refreshPage(HWND hwnd) throws InterruptedException {
		WinDef.WPARAM w = new WinDef.WPARAM(VK_F5);
		WinDef.LPARAM l = new WinDef.LPARAM(1);
		User32.INSTANCE.PostMessage(hwnd, WM_KEYDOWN, w, l);
		User32.INSTANCE.PostMessage(hwnd, WM_KEYUP, w, l);
		
		Thread.sleep(20);
		
		return User32.INSTANCE.EnumWindows(new User32.WNDENUMPROC() {

//            int count = 0;

            public boolean callback(Pointer hWnd, Pointer userData) {
                byte[] windowText = new byte[512];
                char[] windowText2 = new char[512];
                User32.INSTANCE.GetWindowTextA(hWnd, windowText, 512);
                User32.INSTANCE.GetClassName(hWnd, windowText2, 512);
                String wText = Native.toString(windowText);
                String wText2 = Native.toString(windowText2);
                wText = (wText.isEmpty()) ? "" : wText;
//                System.out.println("Found window " + hWnd + ", total " + ++count + "; text: "+ wText + wText2);
                if(wText2.equals(CONFIRM)) {
                	
//                	HWND hwndChild = User32.INSTANCE.FindWindowExA(hWnd, null, "#32770", null);
//                	System.out.println(hwndChild);
                	User32.INSTANCE.EnumChildWindows(hWnd,new User32.WNDENUMPROC() {

                        int count2 = 0;

                        public boolean callback(Pointer hWnd, Pointer userData) {
                            byte[] windowText = new byte[512];
                            char[] windowText2 = new char[512];
                            User32.INSTANCE.GetWindowTextA(hWnd, windowText, 512);
                            User32.INSTANCE.GetClassName(hWnd, windowText2, 512);
                            String wText = Native.toString(windowText);
                            String wText2 = Native.toString(windowText2);
                            wText = (wText.isEmpty()) ? "" : wText;
                            count2++;
//                            System.out.println("Found window " + hWnd + ", total " + count + wText+wText2);
                            if(count2 == 1 && wText2.equals("Button")) {
                            	try {
									clickbypostmessage_pointer(hWnd,0,0);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
//                            	System.out.println("Found window " + hWnd + ", total " + ++count + wText);
                            }
                            return true;
                        }
                    },null);

                }
                return true;
            }
        }, null);
		
		

	}

}
