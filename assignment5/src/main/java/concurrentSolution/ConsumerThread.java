package concurrentSolution;

//import static concurrentSolution.Driver.dataSum;

import static concurrentSolution.Driver.readFinished;
import static concurrentSolution.Driver.summaryGenerated;
import static sequentialSolution.Constants.COMMA;
import static sequentialSolution.Constants.CSV_EXTENSION;
import static sequentialSolution.Constants.NEXT_LINE;
import static sequentialSolution.Constants.ONE;
import static sequentialSolution.Constants.OUTPUT_ROW_FORMAT;
import static sequentialSolution.Constants.QUOTE;
import static sequentialSolution.Constants.SLASH;
import static sequentialSolution.Constants.TWO;
import static sequentialSolution.Constants.ZERO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import sequentialSolution.SummaryGenerator;

public class ConsumerThread implements Runnable {

  private String summaryOutputPath;
  private SummaryGenerator summaryGenerator;
  private BlockingDeque<ArrayList<String>> studentVleBlockingQueue;
  private Map<String, ConcurrentHashMap<String, AtomicInteger>> coursesMap;

  public ConsumerThread(String summaryOutputPath,
      BlockingDeque<ArrayList<String>> studentVleBlockingQueue,
      Map<String, ConcurrentHashMap<String, AtomicInteger>> coursesMap) {
    this.summaryOutputPath = summaryOutputPath;
    this.studentVleBlockingQueue = studentVleBlockingQueue;
    this.coursesMap = coursesMap;
  }

  @Override
  public void run() {
    System.out.println("Starting data Consumer");
    while (true) {
      System.out.println("consumer " + Thread.currentThread().getId() + " awake");
      try {
        ArrayList<String> data = this.studentVleBlockingQueue.poll(5000, TimeUnit.MILLISECONDS);
        if (data == null) {
          if (readFinished) {
            System.out.println("consumer " + Thread.currentThread().getId()
                + " starving too long.... close the thread");
            break;
          } else {
            continue;
          }
        }
        System.out.println(data + " has been consumed");
        String courseName = data.get(ZERO); // module_presentation
        String date = data.get(ONE);
        Integer sumClicks = Integer.valueOf(data.get(TWO));
        ConcurrentHashMap<String, AtomicInteger> courseData = this.coursesMap.get(courseName);

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


  private void generateSummary() throws IOException {
    System.out.println("consumer " + Thread.currentThread().getId() + " output summary!");

    Iterator<Entry<String, ConcurrentHashMap<String, AtomicInteger>>> courseIterator = this.coursesMap.entrySet()
        .iterator();
    while (courseIterator.hasNext()) {
      Map.Entry coursePair = (Map.Entry) courseIterator.next();

      String courseName = (String) coursePair.getKey();
      ConcurrentHashMap<String, Integer> courseData = (ConcurrentHashMap<String, Integer>) coursePair.getValue();

      FileWriter summaryWriter = new FileWriter(
          new File(summaryOutputPath + SLASH + courseName + CSV_EXTENSION));
      summaryWriter.append(OUTPUT_ROW_FORMAT + NEXT_LINE);

      for (Map.Entry<String, Integer> entry : courseData.entrySet()) {
        String date = entry.getKey();
        String sumClicks = String.valueOf(entry.getValue());
        summaryWriter.append(QUOTE + date + QUOTE + COMMA + QUOTE + sumClicks + QUOTE + NEXT_LINE);
      }
      summaryWriter.flush();
      summaryWriter.close();
    }
  }
}
