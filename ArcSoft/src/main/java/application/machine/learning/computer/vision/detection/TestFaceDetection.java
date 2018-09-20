package application.machine.learning.computer.vision.detection;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import application.machine.learning.computer.vision.common.library.C_Library;
import application.machine.learning.computer.vision.detection.enumeration._AFD_FSDK_OrientPriority;
import application.machine.learning.computer.vision.detection.library.AFD_FSDK_Library;
import application.machine.learning.computer.vision.detection.struct.AFD_FSDK_Version;

/**
 * 	This is a class to test face detection
 * 
 * @author vinsy
 *
 */
public class TestFaceDetection {

	private final static int WORKBUF_SIZE = (40 * 1024 * 1024);
	private final static String APPID = "C4tGM4jG929umE9ndmNuSpVu1XECgiAviAVie4xmrci7";
	private final static String SDKKey = "Dsa2AT2iLvkRjFyj4WWMPabnJ93K7bCjJnAijD6WGmbG";
	
	private PointerByReference phEngineFD;
	
	/**
	 * 	This is the main method for execution
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Object instantiation
		TestFaceDetection test = new TestFaceDetection();
		
		// Test instance option
		System.out.println("Here tests instance option:");
		test.testInstanceOption();
		
		// Test initialize engine
		System.out.println("\nHere tests initializing engine:");
		test.testInitializeEngine();
		
		// Test get version
		System.out.println("\nHere tests getting version:");
		test.testGetVersion();
	}
	
	/**
	 * 	This is a method to test instance option
	 */
	private void testInstanceOption() {
		
		String optionAllowObjects = AFD_FSDK_Library.OPTION_ALLOW_OBJECTS;
		String optionCallingConvention = AFD_FSDK_Library.OPTION_CALLING_CONVENTION;
		String optionClassLoader = AFD_FSDK_Library.OPTION_CLASSLOADER;
		String optionFuncitonMapper = AFD_FSDK_Library.OPTION_FUNCTION_MAPPER;
		String optionInvocationMapper = AFD_FSDK_Library.OPTION_INVOCATION_MAPPER;
		String optionOpenFlags = AFD_FSDK_Library.OPTION_OPEN_FLAGS;
		String optionStringEncoding = AFD_FSDK_Library.OPTION_STRING_ENCODING;
		String optionStructureAlignment = AFD_FSDK_Library.OPTION_STRUCTURE_ALIGNMENT;
		String optionTypeMapper = AFD_FSDK_Library.OPTION_TYPE_MAPPER;
		
		System.out.println("OPTION_ALLOW_OBJECTS: " + optionAllowObjects);
		System.out.println("OPTION_CALLING_CONVENTION: " + optionCallingConvention);
		System.out.println("OPTION_CLASSLOADER: " + optionClassLoader);
		System.out.println("OPTION_FUNCTION_MAPPER: " + optionFuncitonMapper);
		System.out.println("OPTION_INVOCATION_MAPPER: " + optionInvocationMapper);
		System.out.println("OPTION_OPEN_FLAGS: " + optionOpenFlags);
		System.out.println("OPTION_STRING_ENCODING: " + optionStringEncoding);
		System.out.println("OPTION_STRUCTURE_ALIGNMENT: " + optionStructureAlignment);
		System.out.println("OPTION_TYPE_MAPPER: " + optionTypeMapper);
	}
	
	/**
	 * 	This is a method to test initializing engine
	 */
	private void testInitializeEngine() {
	
		/*	Initialization of variables	*/
		PointerByReference phEngine = new PointerByReference();
		int nScale = 16;
		int nMaxFace = 10;
		Pointer pWorkMem = C_Library.INSTANCE.malloc(WORKBUF_SIZE);
		
		/*	Initialization of engine	*/
		NativeLong nRet = AFD_FSDK_Library.INSTANCE.AFD_FSDK_InitialFaceEngine(
				APPID, SDKKey, pWorkMem, WORKBUF_SIZE, phEngine, 
				_AFD_FSDK_OrientPriority.AFD_FSDK_OPF_0_HIGHER_EXT, nScale, nMaxFace
		);
		
		/*	Engine assignment	*/
		this.phEngineFD = phEngine;
		
		/*	Print information	*/
		System.out.println("Result of initialization of engine: " + nRet.longValue());
		System.out.println(phEngine);
		System.out.println(phEngine.getPointer());
		System.out.println(phEngine.getValue());
	}
	
	/**
	 * 	This is a method to test getting version
	 */
	private void testGetVersion() {
		
		// Get the value from the pointer
		Pointer hEngine = this.phEngineFD.getValue();
		System.out.println(hEngine);
		
		// Get the version information from the engine
		AFD_FSDK_Version version = AFD_FSDK_Library.INSTANCE.AFD_FSDK_GetVersion(hEngine);
		System.out.println(version);
	}
}