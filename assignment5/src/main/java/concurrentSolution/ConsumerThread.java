package concurrentSolution;

import static concurrentSolution.Driver.readFinished;
import static concurrentSolution.Driver.summaryGenerated;
import static concurrentSolution.Driver.summaryOutputPath;
import static concurrentSolution.Driver.threshold;
import static sequentialSolution.Constants.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Consumer Thread class that takes out and processes the data from the blocking queue and generates
 * the summary files
 */
public class ConsumerThread implements Runnable {

  private final BlockingDeque<ArrayList<String>> studentVleBlockingQueue;
  private final Map<String, ConcurrentHashMap<Integer, AtomicInteger>> coursesMap;

  /**
   * Constructs the consumer thread object
   *
   * @param studentVleBlockingQueue the blocking queue that contains the data produced by the
   *                                producer thread
   * @param coursesMap              courses map that contains course name and corresponding course
   *                                information
   */
  public ConsumerThread(
      BlockingDeque<ArrayList<String>> studentVleBlockingQueue,
      Map<String, ConcurrentHashMap<Integer, AtomicInteger>> coursesMap) {
    this.studentVleBlockingQueue = studentVleBlockingQueue;
    this.coursesMap = coursesMap;
  }

  /**
   * Runs the consumer thread and concurrently updates the courses map
   */
  @Override
  public void run() {
    System.out.println("Starting data Consumer");
    while (true) {
      System.out.println("consumer " + Thread.currentThread().getId() + " awake");
      try {
        ArrayList<String> data = this.studentVleBlockingQueue.poll(FIVE * ONE_THOUSAND,
            TimeUnit.MILLISECONDS);
        if (data == null) {
          if (readFinished) {
            System.out.println(
                "producer has done his job, and consumer " + Thread.currentThread().getId()
                    + " waits for empty queue too long..... time to close the thread!");
            break;
          } else {
            continue;
          }
        }
        System.out.println(data + " has been consumed");
        String courseName = data.get(ZERO); // module_presentation
        int date = Integer.parseInt(data.get(ONE));
        int sumClicks = Integer.parseInt(data.get(TWO));
        ConcurrentHashMap<Integer, AtomicInteger> courseData = this.coursesMap.get(courseName);

        if (courseData.putIfAbsent(date, new AtomicInteger(sumClicks)) != null) {
          courseData.get(date).addAndGet(sumClicks);
        }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    synchronized (ConsumerThread.class) {
      if (!summaryGenerated) {
        try {
          generateSummary();
          summaryGenerated = true;
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
    System.out.println("consumer " + Thread.currentThread().getId() + " done");
  }

  /**
   * Generate the summary and the threshold report CSV files
   *
   * @throws IOException exception when the I/O operation is failed or interrupted
   */
  private void generateSummary() throws IOException {
    FileWriter thresholdReportWriter = new FileWriter(
        new File(summaryOutputPath + SLASH + ACTIVITY_PREFIX + threshold + CSV_EXTENSION));
    thresholdReportWriter.append(THRESHOLD_REPORT_ROW_FORMAT + NEXT_LINE);
    System.out.println("consumer " + Thread.currentThread().getId() + " output summary!");

    for (Entry<String, ConcurrentHashMap<Integer, AtomicInteger>> courseEntry : this.coursesMap.entrySet()) {
      String courseName = courseEntry.getKey();
      ConcurrentHashMap<Integer, AtomicInteger> courseData = courseEntry.getValue();
      FileWriter summaryWriter = new FileWriter(
          new File(summaryOutputPath + SLASH + courseName + CSV_EXTENSION));
      summaryWriter.append(OUTPUT_ROW_FORMAT + NEXT_LINE);

      courseData.entrySet().stream().sorted(Entry.comparingByKey()).forEach(entry -> {
        int date = entry.getKey();
        int sumClicks = entry.getValue().intValue();
        try {
          summaryWriter.append(
              QUOTE + date + QUOTE + COMMA + QUOTE + sumClicks + QUOTE + NEXT_LINE);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        if (sumClicks >= Integer.parseInt(threshold)) {
          try {
            thresholdReportWriter.append(
                QUOTE + courseName + QUOTE + COMMA + QUOTE + date + QUOTE + COMMA + QUOTE
                    + sumClicks
                    + QUOTE + NEXT_LINE);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      });

      summaryWriter.flush();
      summaryWriter.close();
    }
    thresholdReportWriter.flush();
    thresholdReportWriter.close();
  }

  /**
   * Tests the Consumer Thread object equals to the passed Object o
   *
   * @param o the passed Object o
   * @return boolean that indicates the equality
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    ConsumerThread consumerThread = (ConsumerThread) o;
    return Objects.equals(this.studentVleBlockingQueue, consumerThread.studentVleBlockingQueue) ||
        Objects.equals(this.coursesMap, consumerThread.coursesMap);
  }

  /**
   * Returns the hash code of the Consumer Thread object
   *
   * @return a hash code integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.studentVleBlockingQueue, this.coursesMap);
  }

  /**
   * Returns the string represents the Consumer Thread information
   *
   * @return a string about the Consumer Thread object
   */
  @Override
  public String toString() {
    return "Consumer Thread: { blocking queue: " + this.studentVleBlockingQueue.toString() + "; "
        + "courses map: " + this.coursesMap.toString()
        + " }";
  }
}
