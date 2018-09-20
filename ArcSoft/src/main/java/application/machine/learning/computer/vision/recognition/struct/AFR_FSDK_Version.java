package application.machine.learning.computer.vision.recognition.struct;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class AFR_FSDK_Version extends Structure {
	
    public int lCodebase;
    public int lMajor;
    public int lMinor;
    public int lBuild;
    public int lFeatureLevel;
    public String Version;
    public String BuildDate;
    public String CopyRight;
    
    @Override
    protected List<String> getFieldOrder() { 
        return Arrays.asList(new String[] { 
            "lCodebase", "lMajor", "lMinor", "lBuild","lFeatureLevel","Version", "BuildDate", "CopyRight"
        });
    }

	@Override
	public String toString() {
		return "AFR_FSDK_Version [lCodebase=" + lCodebase + ", lMajor=" + lMajor + ", lMinor=" + lMinor + ", lBuild="
				+ lBuild + ", lFeatureLevel=" + lFeatureLevel + ", Version=" + Version + ", BuildDate=" + BuildDate
				+ ", CopyRight=" + CopyRight + "]";
	}
}