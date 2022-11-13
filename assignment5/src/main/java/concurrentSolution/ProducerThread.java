package concurrentSolution;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import sequentialSolution.Course;

public class ProducerThread implements Runnable {

  private String studentVleCsvPath;
  private ConcurrentHashMap<String, Course> courseConcurrentHashMap;
  private Scanner csvScanner;

  public ProducerThread(String studentVleCsvPath, ConcurrentHashMap<String, Course> courseConcurrentHashMap) {
    this.studentVleCsvPath = studentVleCsvPath;
    this.courseConcurrentHashMap = courseConcurrentHashMap;
  }

  @Override
  public void run() {
    System.out.println("Starting data producer");
    while (true) {

    }
  }
}
