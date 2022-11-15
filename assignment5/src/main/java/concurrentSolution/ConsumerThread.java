package concurrentSolution;

//import static concurrentSolution.Driver.dataSum;
import static concurrentSolution.Driver.readFinished;
import static concurrentSolution.Driver.summaryGenerated;
import static sequentialSolution.Constants.ONE;
import static sequentialSolution.Constants.TWO;
import static sequentialSolution.Constants.ZERO;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
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
//    while (!readFinished || this.studentVleBlockingQueue.size() > ZERO) {
    while (true) {
      System.out.println("consumer " + Thread.currentThread().getId() + " awake");
      try {
        ArrayList<String> data = this.studentVleBlockingQueue.poll(5000, TimeUnit.MILLISECONDS);
        if (readFinished && data == null) {
          System.out.println("consumer " + Thread.currentThread().getId() + " starving too long.... close the thread");
          break;
        }
        System.out.println(data);
        String course = data.get(ZERO); // module_presentation
        String date = data.get(ONE);
        String sumClicks = data.get(TWO);
        this.courseConcurrentHashMap.get(course).updateSumClicks(date, sumClicks);

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    synchronized (ConsumerThread.class) {
      if(!summaryGenerated) {
        generateSummary();
        summaryGenerated = true;
      }
    }
    System.out.println("consumer " + Thread.currentThread().getId() + " done");
  }

//  private void checkConsumeDone() {
//    System.out.println("consumer " + Thread.currentThread().getId() + " data sum before: " + dataSum);
//    dataSum--;
//    if (readFinished && dataSum == ZERO) {
//      generateSummary();
//    }
//    System.out.println("consumer " + Thread.currentThread().getId() + " data sum after: " + dataSum);
//  }
  private void generateSummary() {
    System.out.println("consumer " + Thread.currentThread().getId() + " output summary!");

    this.summaryGenerator = new SummaryGenerator(this.summaryOutputPath, this.courseConcurrentHashMap);
    try {
      this.summaryGenerator.generateSummary();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


//  public static void updateCSV(String fileToUpdate, String date, String updatedSumClicked) throws IOException, CsvException {
//
//    File inputFile = new File(fileToUpdate);
//
//// Read existing file
//    CSVReader reader = new CSVReader(new FileReader(inputFile));
//    List<String[]> csvBody = reader.readAll();
//// get CSV row column  and replace with by using row and column
//    csvBody.get(csvBody.indexOf(date))[1] = updatedSumClicked;
//    reader.close();
//
//// Write to CSV file which is open
//    CSVWriter writer = new CSVWriter(new FileWriter(inputFile));
//    writer.writeAll(csvBody);
//    writer.flush();
//    writer.close();
//  }
}
