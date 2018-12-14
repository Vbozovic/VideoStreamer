package app;

import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;


import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpenCVTest {


    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    @Test
    public void train(){
        CascadeClassifier faceDetector = new CascadeClassifier();
    }

    @Test
    public void libTest(){



        Mat m = new Mat();

    }

}