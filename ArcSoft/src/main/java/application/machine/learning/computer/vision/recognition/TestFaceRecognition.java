package application.machine.learning.computer.vision.recognition;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import application.machine.learning.computer.vision.common.library.C_Library;
import application.machine.learning.computer.vision.recognition.library.AFR_FSDK_Library;
import application.machine.learning.computer.vision.recognition.struct.AFR_FSDK_Version;

/**
 * 	This is a class to test face recognition
 *  
 * @author vinsy
 *
 */
public class TestFaceRecognition {

	private final static int WORKBUF_SIZE = (40 * 1024 * 1024);
	private final static String APPID = "C4tGM4jG929umE9ndmNuSpVu1XECgiAviAVie4xmrci7";
	private final static String SDKKey = "Dsa2AT2iLvkRjFyj4WWMPacGwk61damJRq4UgqujBpse";
	
	private PointerByReference phEngineFR;
	
	/**
	 * 	This is the main method for execution
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Object instantiation
		TestFaceRecognition test = new TestFaceRecognition();
		
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
		
		String optionAllowObjects = AFR_FSDK_Library.OPTION_ALLOW_OBJECTS;
		String optionCallingConvention = AFR_FSDK_Library.OPTION_CALLING_CONVENTION;
		String optionClassLoader = AFR_FSDK_Library.OPTION_CLASSLOADER;
		String optionFuncitonMapper = AFR_FSDK_Library.OPTION_FUNCTION_MAPPER;
		String optionInvocationMapper = AFR_FSDK_Library.OPTION_INVOCATION_MAPPER;
		String optionOpenFlags = AFR_FSDK_Library.OPTION_OPEN_FLAGS;
		String optionStringEncoding = AFR_FSDK_Library.OPTION_STRING_ENCODING;
		String optionStructureAlignment = AFR_FSDK_Library.OPTION_STRUCTURE_ALIGNMENT;
		String optionTypeMapper = AFR_FSDK_Library.OPTION_TYPE_MAPPER;
		
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
		Pointer pWorkMem = C_Library.INSTANCE.malloc(WORKBUF_SIZE);
		
		/*	Initialization of engine	*/
		NativeLong nRet = AFR_FSDK_Library.INSTANCE.AFR_FSDK_InitialEngine(
				APPID, SDKKey, pWorkMem, WORKBUF_SIZE, phEngine
		);
		
		/*	Engine assignment	*/
		this.phEngineFR = phEngine;
		
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
		Pointer hEngine = this.phEngineFR.getValue();
		System.out.println(hEngine);
		
		// Get the version information from the engine
		AFR_FSDK_Version version = AFR_FSDK_Library.INSTANCE.AFR_FSDK_GetVersion(hEngine);
		System.out.println(version);
	}
}
