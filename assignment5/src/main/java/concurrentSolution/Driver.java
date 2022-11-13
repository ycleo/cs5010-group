package concurrentSolution;

import static sequentialSolution.Constants.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import sequentialSolution.Course;
import sequentialSolution.FileReader;

public class Driver {

  static String coursesCsvPath;
  static String studentVleCsvPath;
  static String summaryOutputPath;
  static ConcurrentHashMap<String, Course> courseConcurrentHashMap;

  public static void main(String[] args) throws IOException {
    if (args.length != THREE) {
      throw new IllegalArgumentException();
    }
    coursesCsvPath = args[ZERO].trim();
    studentVleCsvPath = args[ONE].trim();
    summaryOutputPath = args[TWO].trim();
    courseConcurrentHashMap = new ConcurrentHashMap<>();

    // Read courses.csv and establish courses map
    FileReader fileReader = new FileReader();
    fileReader.readCoursesCsv(coursesCsvPath, courseConcurrentHashMap);

    // Read studentVle.csv and update the courses map (sum clicks)
    // Concurrently update the output CSV file
    Thread dataProducer = new Thread(new ProducerThread(studentVleCsvPath, courseConcurrentHashMap));
    Thread dataConsumer = new Thread(new ConsumerThread(summaryOutputPath, courseConcurrentHashMap));

    dataProducer.start();
    dataConsumer.start();
  }
}
