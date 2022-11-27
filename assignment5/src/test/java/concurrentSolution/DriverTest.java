package concurrentSolution;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DriverTest {

  @Test
  void main() {
    String[] args = new String[]{"testFiles/courses.csv", "testFiles/studentVle.csv", "testFiles/output/"};
    Throwable exception = assertThrows(IllegalArgumentException.class, () -> Driver.main(args));
  }
}