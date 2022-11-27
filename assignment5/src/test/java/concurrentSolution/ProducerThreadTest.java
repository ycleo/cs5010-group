package concurrentSolution;

import static org.junit.jupiter.api.Assertions.*;
import static sequentialSolution.Constants.FIVE;
import static sequentialSolution.Constants.THREE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProducerThreadTest {

  @Test
  void testEquals() {
    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(FIVE);
    ProducerThread producerThread1 = new ProducerThread(studentVleBlockingQueue);
    ProducerThread producerThread2 = new ProducerThread(studentVleBlockingQueue);
    assertTrue(producerThread1.equals(producerThread1));
    assertFalse(producerThread1.equals(null));
    assertFalse(producerThread1.equals(studentVleBlockingQueue));
    assertTrue(producerThread1.equals(producerThread2));
    studentVleBlockingQueue = new LinkedBlockingDeque<>(THREE);
    producerThread2 = new ProducerThread(studentVleBlockingQueue);
    assertFalse(producerThread1.equals(producerThread2));
  }

  @Test
  void testHashCode() {
    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(FIVE);
    ProducerThread producerThread = new ProducerThread(studentVleBlockingQueue);
    assertEquals(producerThread.hashCode(), producerThread.hashCode());
  }

  @Test
  void testToString() {
    BlockingDeque<ArrayList<String>> studentVleBlockingQueue = new LinkedBlockingDeque<>(FIVE);
    ProducerThread producerThread = new ProducerThread(studentVleBlockingQueue);
    Thread producer = new Thread(producerThread);
    producer.start();
    assertEquals("Producer Thread: { blocking queue: []; student data format: null }", producerThread.toString());
  }
}
