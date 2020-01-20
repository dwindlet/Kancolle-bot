package application;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.*;

public class GetWindowRect {

   public interface User32 extends StdCallLibrary {
      User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class,
               W32APIOptions.DEFAULT_OPTIONS);

      int GetWindowRect(HWND handle, int[] rect);
   }

   public static int[] getRect(HWND hwnd){
      int[] rect = {0, 0, 0, 0};
      User32.INSTANCE.GetWindowRect(hwnd, rect);

      return rect;
   }

}