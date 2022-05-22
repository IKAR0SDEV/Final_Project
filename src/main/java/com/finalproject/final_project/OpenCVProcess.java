package com.finalproject.final_project;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Integer.parseInt;

public class OpenCVProcess {
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

    //Pixelate the image
    public static Mat pixelate(Mat image, int pixelSize) {
        Mat pixelated = new Mat();
        Imgproc.pyrMeanShiftFiltering(image, pixelated, pixelSize, pixelSize);
        return pixelated;
    }


//Method run from the web application
    public void processor(String lineThick, String blur, String pixelate) {
        int lineThickness = parseInt(lineThick);
        int blurSize = parseInt(blur);
        int pixelSize = parseInt(pixelate);


        Mat src = Imgcodecs.imread("src/main/payara5/glassfish/domains/domain1/generated/jsp/Final_Project-1.0-SNAPSHOT/Input.jpg");
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        Mat dst = new Mat();

        //Convert to gray scale
        Mat gray = toGray(src);

        //adaptiveThreshold the image
        Mat thresholded = adaptiveThreshold(gray, lineThickness , Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, blurSize);
        //Imgcodecs.imwrite("src/main/resources/OutputPic/thresholded.jpg", thresholded);

        //Pixelate the image
        Mat pixelated = pixelate(src, pixelSize);

        //Blur the image
        Mat blurred = blur(pixelated, blurSize);

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

    public static void main(String[] args) throws Exception {
        //Variables
        int lineThickness = 25;
        int blurSize = 21;
        int pixelSize = 12;

        //Time stamp
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());

        //File path
        String filePath = "src/main/payara5/glassfish/domains/domain1/generated/jsp/Final_Project-1.0-SNAPSHOT/Input.jpg";
        String inputPath = "src/main/webapp/Resources/InPic/Input-" + timeStamp + ".jpg";
        Files.copy(new File(filePath).toPath(), new File(inputPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

        Mat src = Imgcodecs.imread(filePath);

        Mat dst = new Mat();


        //Convert to gray scale
        Mat gray = toGray(src);

        //adaptiveThreshold the image
        Mat thresholded = adaptiveThreshold(gray, lineThickness , Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, blurSize);
        //Imgcodecs.imwrite("src/main/resources/OutputPic/thresholded.jpg", thresholded);

        //Pixelate the image
        Mat pixelated = pixelate(src, pixelSize);

        //Blur the image
        Mat blurred = blur(pixelated, blurSize);

        //convert threarshold image from gray scale to BGR
        Imgproc.cvtColor(thresholded, dst, Imgproc.COLOR_GRAY2BGR);

        //Combine thresholded and blurred image
        Core.bitwise_and(dst, blurred, dst);
        //Imgcodecs.imwrite("src/main/resources/OutputPic/final.jpg", dst);

        //Combine blurred and thresholded
        Core.addWeighted(dst, 0.4, blurred, 0.5, 0, dst);
        Imgcodecs.imwrite("src/main/payara5/glassfish/domains/domain1/docroot/img/Output.jpg", dst);

        //Output image path
        String outputPath = "src/main/webapp/Resources/OutPic/Output-" + timeStamp + ".jpg";
        Files.copy(new File("src/main/payara5/glassfish/domains/domain1/docroot/img/Output.jpg").toPath(), new File(outputPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

        //Random id number generator
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        String ID = Integer.toString(number);

        CSVWriter csvwriter = new CSVWriter();
        csvwriter.writeCSV(ID, inputPath, outputPath, timeStamp);

    }
}
