package concurrentSolution;

//import static concurrentSolution.Driver.dataSum;

import static concurrentSolution.Driver.readFinished;
import static sequentialSolution.Constants.*;
import static sequentialSolution.DatasetReader.trimQuotationMark;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;

public class ProducerThread implements Runnable {

  private String studentVleCsvPath;
  private BlockingDeque<ArrayList<String>> studentVleBlockingQueue;
  private Scanner csvScanner;
  private String[] studentVleFormat;

  public ProducerThread(String studentVleCsvPath,
      BlockingDeque<ArrayList<String>> studentVleBlockingQueue) {
    this.studentVleCsvPath = studentVleCsvPath;
    this.studentVleBlockingQueue = studentVleBlockingQueue;
  }

  @Override
  public void run() {
    System.out.println("Starting data producer");
    try {
      this.csvScanner = new Scanner(new File(this.studentVleCsvPath));
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
//        dataSum++;
        System.out.println(data + " was produced");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    readFinished = true;
    System.out.println("Producer done");
  }
}
