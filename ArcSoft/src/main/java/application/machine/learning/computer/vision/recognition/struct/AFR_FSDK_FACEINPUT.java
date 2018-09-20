package application.machine.learning.computer.vision.recognition.struct;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

import application.machine.learning.computer.vision.common.struct.MRECT;

public class AFR_FSDK_FACEINPUT extends Structure {
    
    public MRECT.ByValue rcFace;
    public int lOrient;
    
    @Override
    protected List<String> getFieldOrder() { 
        return Arrays.asList(new String[] { 
             "rcFace", "lOrient"
        });
    }
}