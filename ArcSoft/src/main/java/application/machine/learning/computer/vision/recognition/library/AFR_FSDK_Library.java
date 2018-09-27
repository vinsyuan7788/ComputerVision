package application.machine.learning.computer.vision.recognition.library;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.PointerByReference;

import application.machine.learning.computer.vision.common.struct.ASVLOFFSCREEN;
import application.machine.learning.computer.vision.recognition.struct.AFR_FSDK_FACEINPUT;
import application.machine.learning.computer.vision.recognition.struct.AFR_FSDK_FACEMODEL;
import application.machine.learning.computer.vision.recognition.struct.AFR_FSDK_Version;
import application.machine.learning.computer.vision.utils.LoadUtils;
import application.machine.learning.computer.vision.utils.ProjectUtils;

public interface AFR_FSDK_Library extends Library {
	
	AFR_FSDK_Library INSTANCE = (AFR_FSDK_Library) LoadUtils.loadLibrary(Platform.isWindows() ?
			ProjectUtils.DLL_PATH + "libarcsoft_fsdk_face_recognition.dll" :
			ProjectUtils.SO_PATH + "libarcsoft_fsdk_face_recognition.so",
			AFR_FSDK_Library.class);
    
    NativeLong AFR_FSDK_InitialEngine(
    	String appid,
        String sdkid,
        Pointer    pMem,
        int        lMemSize,
        PointerByReference phEngine
    );
    
    NativeLong AFR_FSDK_ExtractFRFeature(
        Pointer hEngine,                  
        ASVLOFFSCREEN pImgData,         
        AFR_FSDK_FACEINPUT pFaceRes,
        AFR_FSDK_FACEMODEL pFaceModels
    ); 
    
    NativeLong AFR_FSDK_FacePairMatching(
        Pointer hEngine,                         
        AFR_FSDK_FACEMODEL reffeature,    
        AFR_FSDK_FACEMODEL probefeature,
        FloatByReference   pfSimilScore
    );
    
    NativeLong AFR_FSDK_UninitialEngine(
		Pointer hEngine
	);
    
    AFR_FSDK_Version AFR_FSDK_GetVersion(
		Pointer hEngine
	);
}