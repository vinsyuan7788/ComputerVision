package application.machine.learning.computer.vision.recognition.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.PointerByReference;

import application.machine.learning.computer.vision.common.enumeration.ASVL_COLOR_FORMAT;
import application.machine.learning.computer.vision.common.library.C_Library;
import application.machine.learning.computer.vision.common.struct.ASVLOFFSCREEN;
import application.machine.learning.computer.vision.common.struct.MRECT;
import application.machine.learning.computer.vision.detection.enumeration._AFD_FSDK_OrientPriority;
import application.machine.learning.computer.vision.detection.library.AFD_FSDK_Library;
import application.machine.learning.computer.vision.detection.struct.AFD_FSDK_FACERES;
import application.machine.learning.computer.vision.detection.struct.AFD_FSDK_Version;
import application.machine.learning.computer.vision.recognition.library.AFR_FSDK_Library;
import application.machine.learning.computer.vision.recognition.struct.AFR_FSDK_FACEINPUT;
import application.machine.learning.computer.vision.recognition.struct.AFR_FSDK_FACEMODEL;
import application.machine.learning.computer.vision.recognition.struct.AFR_FSDK_Version;
import application.machine.learning.computer.vision.utils.BufferInfo;
import application.machine.learning.computer.vision.utils.FaceInfo;
import application.machine.learning.computer.vision.utils.ImageLoader;
import application.machine.learning.computer.vision.utils.TimeUtils;

public class TestDemo_FR {

	public static final String APPID     = "C4tGM4jG929umE9ndmNuSpVu1XECgiAviAVie4xmrci7";
	public static final String FD_SDKKEY = "Dsa2AT2iLvkRjFyj4WWMPabnJ93K7bCjJnAijD6WGmbG";
	public static final String FR_SDKKEY = "Dsa2AT2iLvkRjFyj4WWMPacGwk61damJRq4UgqujBpse";
	
    public static final int FD_WORKBUF_SIZE = 20 * 1024 * 1024;
    public static final int FR_WORKBUF_SIZE = 40 * 1024 * 1024;
    public static final int MAX_FACE_NUM = 50;

    public static final boolean bUseRAWFile = false;
    public static final boolean bUseBGRToEngine = true;

    public static void main(String[] args) {
    	
    	// Record the starting time
    	long startTime = System.currentTimeMillis();
    	
        // init FDEngine
    	System.out.println("Here starts to initialize FD engine...");
        Pointer pFDWorkMem = C_Library.INSTANCE.malloc(FD_WORKBUF_SIZE);
        PointerByReference phFDEngine = new PointerByReference();
        Pointer hFDEngine = phFDEngine.getValue();
        System.out.println("FD engine before initialization: " + phFDEngine + " whose value is " + hFDEngine);
        NativeLong ret = AFD_FSDK_Library.INSTANCE.AFD_FSDK_InitialFaceEngine(APPID, FD_SDKKEY, pFDWorkMem, FD_WORKBUF_SIZE, phFDEngine, _AFD_FSDK_OrientPriority.AFD_FSDK_OPF_0_HIGHER_EXT, 32, MAX_FACE_NUM);
        if (ret.longValue() != 0) {
            C_Library.INSTANCE.free(pFDWorkMem);
            System.out.println(String.format("AFD_FSDK_InitialFaceEngine ret 0x%x",ret.longValue()));
            System.exit(0);
        }
        hFDEngine = phFDEngine.getValue();
        System.out.println("FD engine after initialization: " + phFDEngine + " whose value is " + hFDEngine);
        System.out.println("FD engine initialization succeeds!");
        long endTime1 = TimeUtils.computeTimeCost(startTime, "init FDEngine");

        // print FDEngine version
        System.out.println("\nHere prints the version information of FD engine...");
        AFD_FSDK_Version versionFD = AFD_FSDK_Library.INSTANCE.AFD_FSDK_GetVersion(hFDEngine);
        System.out.println(String.format("%d %d %d %d", versionFD.lCodebase, versionFD.lMajor, versionFD.lMinor, versionFD.lBuild));
        System.out.println(versionFD.Version);
        System.out.println(versionFD.BuildDate);
        System.out.println(versionFD.CopyRight);
        long endTime2 = TimeUtils.computeTimeCost(endTime1, "print FDEngine version");

        // init FREngine
        System.out.println("\nHere starts to initialize FR engine...");
        Pointer pFRWorkMem = C_Library.INSTANCE.malloc(FR_WORKBUF_SIZE);
        PointerByReference phFREngine = new PointerByReference();
        Pointer hFREngine = phFREngine.getValue();
        System.out.println("FR engine before initialization: " + phFREngine + " whose value is " + hFREngine);
        ret = AFR_FSDK_Library.INSTANCE.AFR_FSDK_InitialEngine(APPID, FR_SDKKEY, pFRWorkMem, FR_WORKBUF_SIZE, phFREngine);
        if (ret.longValue() != 0) {
            AFD_FSDK_Library.INSTANCE.AFD_FSDK_UninitialFaceEngine(hFDEngine);
            C_Library.INSTANCE.free(pFDWorkMem);
            C_Library.INSTANCE.free(pFRWorkMem);
            System.out.println(String.format("AFR_FSDK_InitialEngine ret 0x%x" ,ret.longValue()));
            System.exit(0);
        }
        hFREngine = phFREngine.getValue();
        System.out.println("FR engine after initialization: " + phFREngine + " whose value is " + hFREngine);
        System.out.println("FR engine initialization succeeds!");
        long endTime3 = TimeUtils.computeTimeCost(endTime2, "init FREngine");

        // print FREngine version
        System.out.println("\nHere prints the version information of FR engine...");
        AFR_FSDK_Version versionFR = AFR_FSDK_Library.INSTANCE.AFR_FSDK_GetVersion(hFREngine);
        System.out.println(String.format("%d %d %d %d", versionFR.lCodebase, versionFR.lMajor, versionFR.lMinor, versionFR.lBuild));
        System.out.println(versionFR.Version);
        System.out.println(versionFR.BuildDate);
        System.out.println(versionFR.CopyRight);
        long endTime4 = TimeUtils.computeTimeCost(endTime3, "print FREngine version");

        // load Image Data
        System.out.println("\nHere loads the images...");
        ASVLOFFSCREEN inputImgA;
        ASVLOFFSCREEN inputImgB;
        if (bUseRAWFile) {
            String filePathA = "001_640x480_I420.YUV";
            int yuv_widthA = 640;
            int yuv_heightA = 480;
            int yuv_formatA = ASVL_COLOR_FORMAT.ASVL_PAF_I420;
            String filePathB = "003_640x480_I420.YUV";
            int yuv_widthB = 640;
            int yuv_heightB = 480;
            int yuv_formatB = ASVL_COLOR_FORMAT.ASVL_PAF_I420;
            inputImgA = loadRAWImage(filePathA, yuv_widthA, yuv_heightA, yuv_formatA);
            inputImgB = loadRAWImage(filePathB, yuv_widthB, yuv_heightB, yuv_formatB);
        } else {
            String filePathA = "003.jpg";
            /* 
             * 	Here is the URL with Aliyun CDN acceleration to OSS
             * 	-- In this way, remote accessing Aliyun OSS will be 2x faster
             */
            String filePathB = "http://face.sz-xuanyu.com/user/174.jpg";
            /* 
             * 	Here is the URL from Aliyun OSS
             * 	-- In this way, can access an image stored remotely in Aliyun OSS, but with relatively slow speed
             */
//            String filePathB = "https://xy-face.oss-cn-shenzhen.aliyuncs.com/user/174.jpg";
            /*
             *  Here is the URL from local file system (under root directory of current project)
             * 	-- In this way, all registered human-face images will be stored locally
             */
//            String filePathB = "174.jpg";
            inputImgA = loadImage(filePathA);
            inputImgB = loadImage(filePathB);
        }
        System.out.println("Image loading succeeds!");
        long endTime5 = TimeUtils.computeTimeCost(endTime4, "load image data");

        // Here do the comparison through face detections and feature extraction
        System.out.println("\nHere starts to compare images...");
        System.out.println(String.format("similarity between faceA and faceB is %f" , compareFaceSimilarity(hFDEngine, hFREngine, inputImgA, inputImgB)));
        System.out.println("Image comparison succeeds!");
        long endTime6 = TimeUtils.computeTimeCost(endTime5, "face comparison");
        
        // release Engine
        System.out.println("\nHere starts to free the engines and memories...");
        System.out.println("FD engine before uninitialization: " + hFDEngine);
        AFD_FSDK_Library.INSTANCE.AFD_FSDK_UninitialFaceEngine(hFDEngine);
        System.out.println("FD engine after uninitialization: " + hFDEngine);
        System.out.println("FR engine before uninitialization: " + hFREngine);
        AFR_FSDK_Library.INSTANCE.AFR_FSDK_UninitialEngine(hFREngine);
        System.out.println("FR engine after uninitializaiton: " + hFREngine);
        C_Library.INSTANCE.free(pFDWorkMem);
        C_Library.INSTANCE.free(pFRWorkMem);
        System.out.println("Engine and memories freeing succeeds!");
        TimeUtils.computeTimeCost(endTime6, "release engine");
        
        // Total time cost
        System.out.println("Total time cost: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public static FaceInfo[] doFaceDetection(Pointer hFDEngine, ASVLOFFSCREEN inputImg) {
    	
        FaceInfo[] faceInfo = new FaceInfo[0];

        PointerByReference ppFaceRes = new PointerByReference();
        NativeLong ret = AFD_FSDK_Library.INSTANCE.AFD_FSDK_StillImageFaceDetection(hFDEngine, inputImg, ppFaceRes);
        if (ret.longValue() != 0) {
            System.out.println(String.format("AFD_FSDK_StillImageFaceDetection ret 0x%x" , ret.longValue()));
            return faceInfo;
        }

        AFD_FSDK_FACERES faceRes = new AFD_FSDK_FACERES(ppFaceRes.getValue());
        if (faceRes.nFace > 0) {
            faceInfo = new FaceInfo[faceRes.nFace];
            for (int i = 0; i < faceRes.nFace; i++) {
                MRECT rect = new MRECT(new Pointer(Pointer.nativeValue(faceRes.rcFace.getPointer()) + faceRes.rcFace.size() * i));
                int orient = faceRes.lfaceOrient.getPointer().getInt(i * 4);
                faceInfo[i] = new FaceInfo();

                faceInfo[i].left = rect.left;
                faceInfo[i].top = rect.top;
                faceInfo[i].right = rect.right;
                faceInfo[i].bottom = rect.bottom;
                faceInfo[i].orient = orient;

                System.out.println(String.format("Face[%d]: Rect[%d, %d, %d, %d]. Face orient: %d", i, rect.left, rect.top, rect.right, rect.bottom, orient));
            }
        }
        return faceInfo;
    }

    public static AFR_FSDK_FACEMODEL extractFRFeature(Pointer hFREngine, ASVLOFFSCREEN inputImg, FaceInfo faceInfo) {

        AFR_FSDK_FACEINPUT faceinput = new AFR_FSDK_FACEINPUT();
        faceinput.lOrient = faceInfo.orient;
        faceinput.rcFace.left = faceInfo.left;
        faceinput.rcFace.top = faceInfo.top;
        faceinput.rcFace.right = faceInfo.right;
        faceinput.rcFace.bottom = faceInfo.bottom;

        AFR_FSDK_FACEMODEL faceFeature = new AFR_FSDK_FACEMODEL();
        NativeLong ret = AFR_FSDK_Library.INSTANCE.AFR_FSDK_ExtractFRFeature(hFREngine, inputImg, faceinput, faceFeature);
        if (ret.longValue() != 0) {
            System.out.println(String.format("AFR_FSDK_ExtractFRFeature ret 0x%x" ,ret.longValue()));
            return null;
        }

        try {
            return faceFeature.deepCopy();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static float compareFaceSimilarity(Pointer hFDEngine, Pointer hFREngine, ASVLOFFSCREEN inputImgA, ASVLOFFSCREEN inputImgB) {
    	
        // Do Face Detect
        FaceInfo[] faceInfosA = doFaceDetection(hFDEngine, inputImgA);
        if (faceInfosA.length < 1) {
            System.out.println("no face in Image A ");
            return 0.0f;
        }

        FaceInfo[] faceInfosB = doFaceDetection(hFDEngine, inputImgB);
        if (faceInfosB.length < 1) {
            System.out.println("no face in Image B ");
            return 0.0f;
        }

        // Extract Face Feature
        AFR_FSDK_FACEMODEL faceFeatureA = extractFRFeature(hFREngine, inputImgA, faceInfosA[0]);
        if (faceFeatureA == null) {
            System.out.println("extract face feature in Image A failed");
            return 0.0f;
        }

        AFR_FSDK_FACEMODEL faceFeatureB = extractFRFeature(hFREngine, inputImgB, faceInfosB[0]);
        if (faceFeatureB == null) {
            System.out.println("extract face feature in Image B failed");
            faceFeatureA.freeUnmanaged();
            return 0.0f;
        }

        // calc similarity between faceA and faceB
        FloatByReference fSimilScore = new FloatByReference(0.0f);
        NativeLong ret = AFR_FSDK_Library.INSTANCE.AFR_FSDK_FacePairMatching(hFREngine, faceFeatureA, faceFeatureB, fSimilScore);
        faceFeatureA.freeUnmanaged();
        faceFeatureB.freeUnmanaged();
        if (ret.longValue() != 0) {
            System.out.println(String.format("AFR_FSDK_FacePairMatching failed:ret 0x%x" ,ret.longValue()));
            return 0.0f;
        }
        return fSimilScore.getValue();
    }

    public static ASVLOFFSCREEN loadRAWImage(String yuv_filePath, int yuv_width, int yuv_height, int yuv_format) {
        
    	int yuv_rawdata_size = 0;

        ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();
        inputImg.u32PixelArrayFormat = yuv_format;
        inputImg.i32Width = yuv_width;
        inputImg.i32Height = yuv_height;
        if (ASVL_COLOR_FORMAT.ASVL_PAF_I420 == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width / 2;
            inputImg.pi32Pitch[2] = inputImg.i32Width / 2;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3 / 2;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV12 == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3 / 2;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV21 == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3 / 2;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_YUYV == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width * 2;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 2;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8 == inputImg.u32PixelArrayFormat) {
            inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
            yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3;
        } else {
            System.out.println("unsupported  yuv format");
            System.exit(0);
        }

        // load YUV Image Data from File
        byte[] imagedata = new byte[yuv_rawdata_size];
        File f = new File(yuv_filePath);
        InputStream ios = null;
        try {
            ios = new FileInputStream(f);
            ios.read(imagedata,0,yuv_rawdata_size);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error in loading yuv file");
            System.exit(0);
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }

        if (ASVL_COLOR_FORMAT.ASVL_PAF_I420 == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = new Memory(inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height + inputImg.pi32Pitch[1] * inputImg.i32Height / 2, inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV12 == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV21 == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_YUYV == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = Pointer.NULL;
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else if (ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8 == inputImg.u32PixelArrayFormat) {
            inputImg.ppu8Plane[0] = new Memory(imagedata.length);
            inputImg.ppu8Plane[0].write(0, imagedata, 0, imagedata.length);
            inputImg.ppu8Plane[1] = Pointer.NULL;
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else {
            System.out.println("unsupported yuv format");
            System.exit(0);
        }

        inputImg.setAutoRead(false);
        return inputImg;
    }

    public static ASVLOFFSCREEN loadImage(String filePath) {
        
    	ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();

        if (bUseBGRToEngine) {
            BufferInfo bufferInfo = ImageLoader.getBGRFromFile(filePath);
            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8;
            inputImg.i32Width = bufferInfo.width;
            inputImg.i32Height = bufferInfo.height;
            inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = Pointer.NULL;
            inputImg.ppu8Plane[2] = Pointer.NULL;
            inputImg.ppu8Plane[3] = Pointer.NULL;
        } else {
            BufferInfo bufferInfo = ImageLoader.getI420FromFile(filePath);
            inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_I420;
            inputImg.i32Width = bufferInfo.width;
            inputImg.i32Height = bufferInfo.height;
            inputImg.pi32Pitch[0] = inputImg.i32Width;
            inputImg.pi32Pitch[1] = inputImg.i32Width / 2;
            inputImg.pi32Pitch[2] = inputImg.i32Width / 2;
            inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
            inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[1].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height, inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2] = new Memory(inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[2].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height + inputImg.pi32Pitch[1] * inputImg.i32Height / 2, inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
            inputImg.ppu8Plane[3] = Pointer.NULL;
        }

        inputImg.setAutoRead(false);
        return inputImg;
    }
}