package application.machine.learning.computer.vision.common.library;

import com.sun.jna.Library;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;

import application.machine.learning.computer.vision.utils.LoadUtils;

public interface C_Library extends Library {
	
	C_Library INSTANCE = (C_Library) LoadUtils.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), C_Library.class);

    Pointer malloc(int len);
    void free(Pointer p);
    void printf(String format, Object... args);
    Pointer memcpy(Pointer dst, Pointer src, long size);
}
