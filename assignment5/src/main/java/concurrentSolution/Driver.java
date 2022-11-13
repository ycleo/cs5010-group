package concurrentSolution;

import static sequentialSolution.Constants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import sequentialSolution.Course;
import sequentialSolution.FileReader;
import sequentialSolution.SummaryGenerator;

public class Driver {

  static String coursesCsvPath;
  static String studentVleCsvPath;
  static String summaryOutputPath;
  public static String progress;

  public static void main(String[] args) throws IOException {
    progress = "start";
    if (args.length != THREE) {
      throw new IllegalArgumentException();
    }
    coursesCsvPath = args[ZERO].trim();
    studentVleCsvPath = args[ONE].trim();
    summaryOutputPath = args[TWO].trim();
    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(1000000);
    ConcurrentHashMap<String, Course> courseConcurrentHashMap = new ConcurrentHashMap<>();

    // Read courses.csv and establish courses map
    FileReader fileReader = new FileReader();
    fileReader.readCoursesCsv(coursesCsvPath, courseConcurrentHashMap);

    // Concurrently read studentVle.csv and update the courses map (sum clicks)
    Thread dataProducer = new Thread(new ProducerThread(studentVleCsvPath, studentVleBlockingQueue));
    Thread dataConsumer1 = new Thread(new ConsumerThread(summaryOutputPath, studentVleBlockingQueue, courseConcurrentHashMap));
    Thread dataConsumer2 = new Thread(new ConsumerThread(summaryOutputPath, studentVleBlockingQueue, courseConcurrentHashMap));
    Thread dataConsumer3 = new Thread(new ConsumerThread(summaryOutputPath, studentVleBlockingQueue, courseConcurrentHashMap));
    Thread dataConsumer4 = new Thread(new ConsumerThread(summaryOutputPath, studentVleBlockingQueue, courseConcurrentHashMap));
    Thread dataConsumer5 = new Thread(new ConsumerThread(summaryOutputPath, studentVleBlockingQueue, courseConcurrentHashMap));

    dataProducer.start();
    dataConsumer1.start();
    dataConsumer2.start();
    dataConsumer3.start();
    dataConsumer4.start();
    dataConsumer5.start();
  }
}
