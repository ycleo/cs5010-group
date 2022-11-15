package concurrentSolution;

import static concurrentSolution.Driver.readFinished;
import static concurrentSolution.Driver.studentVleCsvPath;
import static sequentialSolution.Constants.*;
import static sequentialSolution.DatasetReader.trimQuotationMark;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;

/**
 * Producer Thread class that reads the studentVle.csv and formats the data into the blocking queue
 */
public class ProducerThread implements Runnable {

  private final BlockingDeque<ArrayList<String>> studentVleBlockingQueue;
  private Scanner csvScanner;
  private String[] studentVleFormat;

  /**
   * Constructs the producer thread object
   *
   * @param studentVleBlockingQueue the blocking queue that contains the data produced by the
   *                                producer thread
   */
  public ProducerThread(BlockingDeque<ArrayList<String>> studentVleBlockingQueue) {
    this.studentVleBlockingQueue = studentVleBlockingQueue;
  }

  /**
   * Runs the producer thread and puts the read data into the blocking queue
   */
  @Override
  public void run() {
    System.out.println("Starting data producer");
    try {
      this.csvScanner = new Scanner(new File(studentVleCsvPath));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    this.studentVleFormat = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));
    while (this.csvScanner.hasNext()) {
      System.out.println("Producer is awake");
      try {
        String[] studentVle = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));

        // Produce course's date and sum clicks
        String course = studentVle[ZERO] + UNDERLINE + studentVle[ONE]; // module_presentation
        String date = studentVle[FOUR];
        String sumClicks = studentVle[FIVE];
        ArrayList<String> data = new ArrayList<>(
            List.of(course, date, sumClicks)
        );

        this.studentVleBlockingQueue.put(data);
        System.out.println(data + " was produced");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    readFinished = true;
    System.out.println("Producer done");
  }

  /**
   * Tests the Producer Thread object equals to the passed Object o
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
    ProducerThread producerThread = (ProducerThread) o;
    return Objects.equals(this.studentVleBlockingQueue, producerThread.studentVleBlockingQueue) ||
        Objects.equals(this.csvScanner, producerThread.csvScanner) ||
        Arrays.equals(this.studentVleFormat, producerThread.studentVleFormat);
  }

  /**
   * Returns the hash code of the Producer Thread object
   *
   * @return a hash code integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.studentVleBlockingQueue, this.csvScanner,
        Arrays.hashCode(this.studentVleFormat));
  }

  /**
   * Returns the string represents the Producer Thread information
   *
   * @return a string about the Producer Thread object
   */
  @Override
  public String toString() {
    return "Producer Thread: { blocking queue: " + this.studentVleBlockingQueue.toString() + "; "
        + "CSV scanner: " + this.csvScanner.toString() + "; "
        + "student data format: " + Arrays.toString(this.studentVleFormat)
        + " }";
  }
}
