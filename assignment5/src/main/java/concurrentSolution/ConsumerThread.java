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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerThread implements Runnable {

  private BlockingDeque<ArrayList<String>> studentVleBlockingQueue;
  private Map<String, ConcurrentHashMap<String, AtomicInteger>> coursesMap;

  public ConsumerThread(
      BlockingDeque<ArrayList<String>> studentVleBlockingQueue,
      Map<String, ConcurrentHashMap<String, AtomicInteger>> coursesMap) {
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
    FileWriter thresholdReportWriter = new FileWriter(new File(summaryOutputPath + SLASH + ACTIVITY_PREFIX + threshold + CSV_EXTENSION));
    thresholdReportWriter.append(THRESHOLD_REPORT_ROW_FORMAT + NEXT_LINE);
    System.out.println("consumer " + Thread.currentThread().getId() + " output summary!");

    Iterator<Entry<String, ConcurrentHashMap<String, AtomicInteger>>> courseIterator = this.coursesMap.entrySet()
        .iterator();
    while (courseIterator.hasNext()) {
      Map.Entry coursePair = (Map.Entry) courseIterator.next();

      String courseName = (String) coursePair.getKey();
      ConcurrentHashMap<String, AtomicInteger> courseData = (ConcurrentHashMap<String, AtomicInteger>) coursePair.getValue();

      FileWriter summaryWriter = new FileWriter(
          new File(summaryOutputPath + SLASH + courseName + CSV_EXTENSION));
      summaryWriter.append(OUTPUT_ROW_FORMAT + NEXT_LINE);

      for (Map.Entry<String, AtomicInteger> entry : courseData.entrySet()) {
        String date = entry.getKey();
        int sumClicksNum = entry.getValue().intValue();
        String sumClicks = String.valueOf(sumClicksNum);
        summaryWriter.append(QUOTE + date + QUOTE + COMMA + QUOTE + sumClicks + QUOTE + NEXT_LINE);

        if (sumClicksNum >= Integer.parseInt(threshold)) {
          thresholdReportWriter.append(QUOTE + courseName + QUOTE + COMMA + QUOTE + date + QUOTE + COMMA + QUOTE + sumClicks + QUOTE + NEXT_LINE);
        }
      }
      summaryWriter.flush();
      summaryWriter.close();
    }
    thresholdReportWriter.flush();
    thresholdReportWriter.close();
  }
}
