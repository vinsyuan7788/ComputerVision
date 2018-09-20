package application.machine.learning.computer.vision.detection.struct;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class AFD_FSDK_Version extends Structure {

	/**
	 *  Codebase version number <br/>
	 *  -- Original type is MInt32
	 */
    public int lCodebase;
    
    /**
     *  Major version number <br/>
	 *  -- Original type is MInt32
     */
    public int lMajor;
    
    /** 
     * 	Minor version number <br/>
	 *  -- Original type is MInt32
     */
    public int lMinor;
    
    /**
     *  Build version number, increasable only <br/>
	 *  -- Original type is MInt32
     */
    public int lBuild;
    
    /**
     *  Version in string form <br/>
     *  -- Original type is MPChar
     */
    public String Version;   
    
    /**
     * 	Latest build Date <br/>
     *  -- Original type is MPChar
     */
    public String BuildDate;
    
    /**
     * 	Copyright <br/>
     *  -- Original type is MPChar
     */
    public String CopyRight;

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] {
			"lCodebase", "lMajor", "lMinor", "lBuild", "Version", "BuildDate", "CopyRight"
		});
	}
	
	@Override
	public String toString() {
		return "AFD_FSDK_Version [lCodebase=" + lCodebase + ", lMajor=" + lMajor + ", lMinor=" + lMinor + ", lBuild="
				+ lBuild + ", Version=" + Version + ", BuildDate=" + BuildDate + ", CopyRight=" + CopyRight + "]";
	}
}
