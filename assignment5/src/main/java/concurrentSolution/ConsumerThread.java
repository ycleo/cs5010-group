package concurrentSolution;

import static concurrentSolution.Driver.progress;
import static sequentialSolution.Constants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import sequentialSolution.Course;
import sequentialSolution.SummaryGenerator;

public class ConsumerThread implements Runnable {
  private String summaryOutputPath;
  private SummaryGenerator summaryGenerator;
  private BlockingDeque<ArrayList<String>> studentVleBlockingQueue;
  private ConcurrentHashMap<String, Course> courseConcurrentHashMap;

  public ConsumerThread(String summaryOutputPath, BlockingDeque<ArrayList<String>> studentVleBlockingQueue, ConcurrentHashMap<String, Course> courseConcurrentHashMap) {
    this.summaryOutputPath = summaryOutputPath;
    this.studentVleBlockingQueue = studentVleBlockingQueue;
    this.courseConcurrentHashMap = courseConcurrentHashMap;
  }

  @Override
  public void run() {
    System.out.println("Starting data Consumer");
    while (true) {
//      System.out.println("Consumer is awake");

      try {
        ArrayList<String> data = this.studentVleBlockingQueue.take();
        String course = data.get(ZERO); // module_presentation
        String date = data.get(ONE);
        String sumClicks = data.get(TWO);
        System.out.println(course);
        this.courseConcurrentHashMap.get(course).updateSumClicks(date, sumClicks);

        if (progress == "read done" && this.studentVleBlockingQueue.size() == 0) {
          generateSummary();
          break;
        }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private synchronized void generateSummary() {
    System.out.println("output summary!");
    this.summaryGenerator = new SummaryGenerator(this.summaryOutputPath, this.courseConcurrentHashMap);
    try {
      this.summaryGenerator.generateSummary();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
