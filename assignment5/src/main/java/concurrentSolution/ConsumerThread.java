package concurrentSolution;

import java.util.concurrent.ConcurrentHashMap;
import sequentialSolution.Course;

public class ConsumerThread implements Runnable {

  private String summaryOutputPath;
  private ConcurrentHashMap<String, Course> courseConcurrentHashMap;

  public ConsumerThread(String summaryOutputPath, ConcurrentHashMap<String, Course> courseConcurrentHashMap) {
    this.summaryOutputPath = summaryOutputPath;
    this.courseConcurrentHashMap = courseConcurrentHashMap;
  }

  @Override
  public void run() {

  }
}
