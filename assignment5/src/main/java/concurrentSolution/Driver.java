package concurrentSolution;

import static sequentialSolution.Constants.*;
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

/**
 * The driver class that runs the main function of the program
 */
public class Driver {

  /**
   * Path of the course.csv file
   */
  public static String coursesCsvPath;
  /**
   * Path of the studentVle.csv file
   */
  public static String studentVleCsvPath;
  /**
   * Path of the output directory
   */
  public static String summaryOutputPath;
  /**
   * Threshold value provided by the user
   */
  public static String threshold;
  /**
   * Boolean indicates the producer has done reading all data
   */
  public static boolean readFinished;
  /**
   * Boolean indicates the summary files have been all generated
   */
  public static boolean summaryGenerated;

  /**
   * The main function of the program that processes the CSV files and output the summary
   *
   * @param args arguments that contains the paths of input CSV files, output directory, and the
   *             threshold value
   * @throws IOException exception when the I/O operation is failed or interrupted
   */
  public static void main(String[] args) throws IOException {
    readFinished = false;
    summaryGenerated = false;
    if (args.length != FOUR) {
      throw new IllegalArgumentException();
    }
    coursesCsvPath = args[ZERO].trim();
    studentVleCsvPath = args[ONE].trim();
    summaryOutputPath = args[TWO].trim();
    threshold = args[THREE].trim();

    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(FIVE);
    HashMap<String, ConcurrentHashMap<Integer, AtomicInteger>> coursesMap = new HashMap<>(); // module_presentation -> { date -> sum clicks }

    // Read courses.csv and establish courses map
    readCourses(coursesCsvPath, coursesMap);

    // Concurrently read studentVle.csv and update the courses map (sum clicks)
    Thread producer = new Thread(new ProducerThread(studentVleBlockingQueue));
    Thread consumer1 = new Thread(new ConsumerThread(studentVleBlockingQueue, coursesMap));
    Thread consumer2 = new Thread(new ConsumerThread(studentVleBlockingQueue, coursesMap));

    producer.start();
    consumer1.start();
    consumer2.start();
  }

  /**
   * Reads and processes the course.csv file to establish the courses map
   *
   * @param path       the path of the course.csv file
   * @param coursesMap courses map that contains course name and corresponding course information
   * @throws FileNotFoundException exception when the file can not be found
   */
  public static void readCourses(String path,
      Map<String, ConcurrentHashMap<Integer, AtomicInteger>> coursesMap)
      throws FileNotFoundException {
    Scanner csvScanner = new Scanner(new File(path));
    String[] courseFormat = trimQuotationMark(csvScanner.nextLine().split(INFO_DELIMITER));

    while (csvScanner.hasNext()) {
      String[] courseInfo = trimQuotationMark(csvScanner.nextLine().split(INFO_DELIMITER));
      coursesMap.put(courseInfo[ZERO] + UNDERLINE + courseInfo[ONE], new ConcurrentHashMap<>());
    }
  }
}
