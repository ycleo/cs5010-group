package concurrentSolution;

import static org.junit.jupiter.api.Assertions.*;
import static sequentialSolution.Constants.FIVE;
import static sequentialSolution.Constants.INFO_DELIMITER;
import static sequentialSolution.Constants.ONE;
import static sequentialSolution.Constants.THREE;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumerThreadTest {

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

  @Test
  void run() throws IOException {
    String[] args = new String[]{"testFiles/courses.csv", "testFiles/studentVle.csv", "testFiles/output/",
        ""};
    Driver driver = new Driver();
    driver.main(args);
    String coursesCsvPath = "testFiles/courses.csv";
    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(FIVE);
    HashMap<String, ConcurrentHashMap<Integer, AtomicInteger>> coursesMap = new HashMap<>();
    readCourses(coursesCsvPath, coursesMap);
    Thread producer = new Thread(new ProducerThread(studentVleBlockingQueue));
    Thread consumer1 = new Thread(new ConsumerThread(studentVleBlockingQueue, coursesMap));
    Thread consumer2 = new Thread(new ConsumerThread(studentVleBlockingQueue, coursesMap));
    producer.start();
    consumer1.start();
    consumer2.start();
  }

  @Test
  void testEquals() throws IOException {
    String coursesCsvPath = "testFiles/courses.csv";
    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(FIVE);
    HashMap<String, ConcurrentHashMap<Integer, AtomicInteger>> coursesMap = new HashMap<>();
    readCourses(coursesCsvPath, coursesMap);
    ConsumerThread consumer1 = new ConsumerThread(studentVleBlockingQueue, coursesMap);
    ConsumerThread consumer2 = new ConsumerThread(studentVleBlockingQueue, coursesMap);
    assertTrue(consumer1.equals(consumer1));
    assertFalse(consumer1.equals(null));
    assertFalse(consumer1.equals(coursesCsvPath));
    assertTrue(consumer1.equals(consumer2));
    studentVleBlockingQueue = new LinkedBlockingDeque<>(THREE);
    consumer2 = new ConsumerThread(studentVleBlockingQueue, coursesMap);
    assertTrue(consumer1.equals(consumer2));
    coursesMap = new HashMap<>();
    consumer2 = new ConsumerThread(studentVleBlockingQueue, coursesMap);
    assertFalse(consumer1.equals(consumer2));
  }

  @Test
  void testHashCode() throws IOException {
    String coursesCsvPath = "testFiles/courses.csv";
    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(FIVE);
    HashMap<String, ConcurrentHashMap<Integer, AtomicInteger>> coursesMap = new HashMap<>();
    readCourses(coursesCsvPath, coursesMap);
    ConsumerThread consumer = new ConsumerThread(studentVleBlockingQueue, coursesMap);
    assertEquals(consumer.hashCode(), consumer.hashCode());
  }

  @Test
  void testToString() throws IOException {
    String coursesCsvPath = "testFiles/courses.csv";
    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(FIVE);
    HashMap<String, ConcurrentHashMap<Integer, AtomicInteger>> coursesMap = new HashMap<>();
    readCourses(coursesCsvPath, coursesMap);
    ConsumerThread consumer = new ConsumerThread(studentVleBlockingQueue, coursesMap);
    assertEquals(
        "Consumer Thread: { blocking queue: []; courses map: {FFF_2014J={}, CCC_2014J={}, FFF_2013J={}, EEE_2014B={}, BBB_2013B={}, BBB_2014B={}, CCC_2014B={}, EEE_2014J={}, BBB_2013J={}, BBB_2014J={}, GGG_2014J={}, GGG_2013J={}, EEE_2013J={}, GGG_2014B={}, DDD_2014J={}, DDD_2013J={}, FFF_2014B={}, AAA_2013J={}, FFF_2013B={}, AAA_2014J={}, DDD_2014B={}, DDD_2013B={}} }",
        consumer.toString());
  }
}