package sequentialSolution;

import static sequentialSolution.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class FileReader {

  private Scanner csvScanner;
  private String[] courseFormat;
  private String[] studentVleFormat;

  public void readCoursesCsv(String path, HashMap<String, Course> coursesMap)
      throws FileNotFoundException {
    this.csvScanner = new Scanner(new File(path));
    this.courseFormat = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));

    while (this.csvScanner.hasNext()) {
      String[] courseInfo = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));
      Course newCourse = new Course(courseInfo);
      coursesMap.put(courseInfo[ZERO] + UNDERLINE + courseInfo[ONE],
          newCourse); // module_presentation
    }

    this.csvScanner.close();
  }

  public void readStudentVleCsv(String path, HashMap<String, Course> coursesMap)
      throws FileNotFoundException {
    this.csvScanner = new Scanner(new File(path));
    this.studentVleFormat = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));
    while (this.csvScanner.hasNext()) {
      String[] studentVle = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));

      // Produce course's date and sum clicks
      String course = studentVle[ZERO] + UNDERLINE + studentVle[ONE]; // module_presentation
      String date = studentVle[FOUR];
      String sumClicks = studentVle[FIVE];
      coursesMap.get(course).updateSumClicks(date, sumClicks);
    }
    this.csvScanner.close();
  }

  /**
   * Trims the quotation mark extracting from the csv file
   *
   * @param csvRow A certain row from the csv file
   * @return The row that the quotation marks of every element has been removed
   */
  private String[] trimQuotationMark(String[] csvRow) {
    for (int i = ZERO; i < csvRow.length; i++) {
      csvRow[i] = csvRow[i].replaceAll(QUOTATION_MARK, EMPTY);
    }
    return csvRow;
  }

  public String[] getCourseFormat() {
    return this.courseFormat;
  }

  public String[] getStudentVleFormat() {
    return this.studentVleFormat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    FileReader fileReader = (FileReader) o;
    return Objects.equals(this.csvScanner, fileReader.csvScanner) ||
        Objects.equals(this.courseFormat, fileReader.courseFormat) ||
        Objects.equals(this.studentVleFormat, fileReader.studentVleFormat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.csvScanner, this.courseFormat, this.studentVleFormat);
  }

  @Override
  public String toString() {
    return "FileReader: { CSV Scanner: " + this.csvScanner.toString() + "; "
        + "Course Format: " + this.courseFormat.toString() + "; "
        + "StudentVle Format: " + this.studentVleFormat.toString() + " }";
  }
}
