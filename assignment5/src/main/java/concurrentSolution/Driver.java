package concurrentSolution;

import static sequentialSolution.Constants.INFO_DELIMITER;
import static sequentialSolution.Constants.ONE;
import static sequentialSolution.Constants.THREE;
import static sequentialSolution.Constants.TWO;
import static sequentialSolution.Constants.UNDERLINE;
import static sequentialSolution.Constants.ZERO;
import static sequentialSolution.DatasetReader.trimQuotationMark;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Driver {

  static String coursesCsvPath;
  static String studentVleCsvPath;
  static String summaryOutputPath;
  public static boolean readFinished;
  public static boolean summaryGenerated;
//  public static int dataSum;


  public static void main(String[] args) throws IOException, InterruptedException {
    readFinished = false;
    summaryGenerated = false;
    if (args.length != THREE) {
      throw new IllegalArgumentException();
    }
    coursesCsvPath = args[ZERO].trim();
    studentVleCsvPath = args[ONE].trim();
    summaryOutputPath = args[TWO].trim();
    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(10);
    HashMap<String, ConcurrentHashMap<String, AtomicInteger>> coursesMap = new HashMap<>(); // module_presentation -> { date -> sum clicks }

    // Read courses.csv and establish courses map
    readCourses(coursesCsvPath, coursesMap);

    // Concurrently read studentVle.csv and update the courses map (sum clicks)
    Thread producer = new Thread(new ProducerThread(studentVleCsvPath, studentVleBlockingQueue));
    Thread consumer1 = new Thread(
        new ConsumerThread(summaryOutputPath, studentVleBlockingQueue, coursesMap));
    Thread consumer2 = new Thread(
        new ConsumerThread(summaryOutputPath, studentVleBlockingQueue, coursesMap));

    producer.start();
    consumer1.start();
    consumer2.start();
  }

  public static void readCourses(String path,
      Map<String, ConcurrentHashMap<String, AtomicInteger>> coursesMap)
      throws FileNotFoundException {
    Scanner csvScanner = new Scanner(new File(path));
    String[] courseFormat = trimQuotationMark(csvScanner.nextLine().split(INFO_DELIMITER));

    while (csvScanner.hasNext()) {
      String[] courseInfo = trimQuotationMark(csvScanner.nextLine().split(INFO_DELIMITER));
      coursesMap.put(courseInfo[ZERO] + UNDERLINE + courseInfo[ONE], new ConcurrentHashMap<>());
    }
  }
}
