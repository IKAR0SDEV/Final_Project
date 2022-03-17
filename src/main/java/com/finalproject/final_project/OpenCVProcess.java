package com.finalproject.final_project;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.text.SimpleDateFormat;
import java.util.*;

public class OpenCVProcess {
    //Load OpenCV System Library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    //Blur the image
    public static Mat blur(Mat image, int blurSize) {
        Mat blurred = new Mat();
        Imgproc.medianBlur(image, blurred, blurSize);
        return blurred;
    }

    //convert to gray scale
    public static Mat toGray(Mat image) {
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }

    //threshold the image
    public static Mat adaptiveThreshold(Mat image, int lineSize, int adaptiveMethod, int thresholdType, int blurValue) {
        Mat thresholded = new Mat();

        Imgproc.adaptiveThreshold(image, thresholded, 255, adaptiveMethod, thresholdType, lineSize, blurValue);
        return thresholded;
    }

    //Opencv Pixelate the image
    public static Mat pixelate(Mat image, int pixelSize) {
        Mat pixelated = new Mat();
        Imgproc.pyrMeanShiftFiltering(image, pixelated, pixelSize, pixelSize);
        return pixelated;
    }

    public static void main(String[] args) {
        Mat src = Imgcodecs.imread("src/main/payara5/glassfish/domains/domain1/generated/jsp/Final_Project-1.0-SNAPSHOT/Input.jpg");
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        Mat dst = new Mat();

        //Convert to gray scale
        Mat gray = toGray(src);

        //adaptiveThreshold the image
        Mat thresholded = adaptiveThreshold(gray, 11, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11);
        //Imgcodecs.imwrite("src/main/resources/OutputPic/thresholded.jpg", thresholded);

        //Pixelate the image
        Mat pixelated = pixelate(src, 10);

        //Blur the image
        Mat blurred = blur(pixelated, 11);

        //convert threarshold image from gray scale to BGR
        Imgproc.cvtColor(thresholded, dst, Imgproc.COLOR_GRAY2BGR);


        //Combine thresholded and blurred image
        Core.bitwise_and(dst, blurred, dst);
        //Imgcodecs.imwrite("src/main/resources/OutputPic/final.jpg", dst);

        //Combine blurred and thresholded
        Core.addWeighted(dst, 0.4, blurred, 0.5, 0, dst);
        Imgcodecs.imwrite("src/main/resources/OutputPic/Cartoon-" + timeStamp + ".jpg", dst);
        Imgcodecs.imwrite("src/main/payara5/glassfish/domains/domain1/docroot/img/Output.jpg", dst);



    }



}
