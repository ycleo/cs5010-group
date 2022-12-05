package sequentialSolution;

import static org.junit.jupiter.api.Assertions.*;
import static sequentialSolution.Constants.ONE;
import static sequentialSolution.Constants.TWO;
import static sequentialSolution.Constants.ZERO;

import java.io.FileNotFoundException;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatasetReaderTest {

  private DatasetReader datasetReader1;
  private DatasetReader datasetReader2;
  private String coursesCsvPath = "testFiles/courses.csv";
  private String studentVleCsvPath = "testFiles/studentVle.csv";
  private HashMap<String, Course> coursesMap = new HashMap<>();

  @BeforeEach
  void setUp() throws FileNotFoundException {
    datasetReader1 = new DatasetReader();
    datasetReader1.readCoursesCsv(coursesCsvPath, coursesMap);
    datasetReader1.readStudentVleCsv(studentVleCsvPath, coursesMap);
    datasetReader2 = new DatasetReader();
    datasetReader2.readCoursesCsv(coursesCsvPath, coursesMap);
    datasetReader2.readStudentVleCsv(studentVleCsvPath, coursesMap);
  }

  @Test
  void getCourseFormat() {
    assertFalse(datasetReader1.getCourseFormat().equals(datasetReader2.getCourseFormat()));
  }

  @Test
  void getStudentVleFormat() {
    assertFalse(datasetReader1.getStudentVleFormat().equals(datasetReader2.getStudentVleFormat()));
  }

  @Test
  void testEquals() throws FileNotFoundException {
    datasetReader2 = new DatasetReader();
    assertTrue(datasetReader1.equals(datasetReader1));
    assertFalse(datasetReader1.equals(datasetReader2));
    assertFalse(datasetReader1.equals(null));
    assertFalse(datasetReader1.equals(coursesCsvPath));
    datasetReader2.readCoursesCsv(coursesCsvPath, coursesMap);
    assertFalse(datasetReader1.equals(datasetReader2));
    datasetReader2.readStudentVleCsv(studentVleCsvPath, coursesMap);
    assertFalse(datasetReader1.equals(datasetReader2));
  }

  @Test
  void testHashCode() {
    assertEquals(datasetReader1.hashCode(), datasetReader1.hashCode());
  }

  @Test
  void testToString() {
    assertFalse(datasetReader1.toString().equals(datasetReader2.toString()));
  }
}