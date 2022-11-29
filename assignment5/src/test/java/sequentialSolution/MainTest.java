package sequentialSolution;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class MainTest {

  @Test
  void mainTest() throws IOException {
    String[] args = new String[]{"testFiles/courses.csv", "testFiles/studentVle.csv", "testFiles/output/", ""};
    String[] finalArgs = args;
    Throwable exception = assertThrows(IllegalArgumentException.class, () -> Main.main(finalArgs));
    args = new String[]{"testFiles/courses.csv", "testFiles/studentVle.csv", "testFiles/output/"};
    Main.main(args);
  }
}