package application.machine.learning.computer.vision.detection.library;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import application.machine.learning.computer.vision.common.struct.ASVLOFFSCREEN;
import application.machine.learning.computer.vision.detection.struct.AFD_FSDK_Version;
import application.machine.learning.computer.vision.utils.LoadUtils;

public interface AFD_FSDK_Library extends Library {
    
	AFD_FSDK_Library INSTANCE = (AFD_FSDK_Library) LoadUtils.loadLibrary(Platform.isWindows() ? "libarcsoft_fsdk_face_detection.dll" : "libarcsoft_fsdk_face_detection.so", AFD_FSDK_Library.class);
    
    NativeLong AFD_FSDK_InitialFaceEngine(
		String appid, 
		String sdkid, 
		Pointer pMem, 
		int lMemSize, 
		PointerByReference phEngine, 
		int iOrientPriority, 
		int nScale, 
		int nMaxFaceNum
	);

    NativeLong AFD_FSDK_StillImageFaceDetection(
    	Pointer hEngine,
    	ASVLOFFSCREEN pImgData,
    	PointerByReference pFaceRes
	);

    NativeLong AFD_FSDK_UninitialFaceEngine(
		Pointer hEngine
	);

    AFD_FSDK_Version AFD_FSDK_GetVersion(
		Pointer hEngine
	);
}
