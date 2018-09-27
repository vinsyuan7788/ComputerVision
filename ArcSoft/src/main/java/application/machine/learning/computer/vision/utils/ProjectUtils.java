package application.machine.learning.computer.vision.utils;

public class ProjectUtils {
	
	public static final String CLASS_PATH = "D:/GitRepository/Application/MachineLearning/ComputerVision/ArcSoft/target/classes/";
	public static final String DLL_PATH = "D:/GitRepository/Application/MachineLearning/ComputerVision/ArcSoft/target/classes/win32-x86-64/";
	public static final String SO_PATH = "D:/GitRepository/Application/MachineLearning/ComputerVision/ArcSoft/target/classes/linux_x64/";
	
	private static volatile ProjectUtils INSTANCE;
	
	private ProjectUtils() {}
	
	public static ProjectUtils getInstance() {
		
		if (INSTANCE == null || INSTANCE.equals(null)) {
			synchronized(ProjectUtils.class){
				if (INSTANCE == null || INSTANCE.equals(null)) {
					INSTANCE = new ProjectUtils();
				}
			}
		}
		
		return INSTANCE;
	}
	
	public String getTargetPath() {
		
		/*
		 * 	This value would vary according to how this project is actually being run
		 * 	-- If running this project within an IDE, the value would be like:
		 *     -- "/D:/GitRepository/Application/MachineLearning/ComputerVision/ArcSoft/target/classes/"
		 *  -- If running this project with a jar, the value would be like:
		 *     -- "/D:/GitRepository/Application/MachineLearning/ComputerVision/ArcSoft/target/ArcSoft-0.0.1-SNAPSHOT.jar"
		 *  -- Hence we need to further process this value
		 */
		String fullPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		
		String targetPath = fullPath.substring(0, fullPath.lastIndexOf("/")) + "/";
		
		return targetPath;
	}
}