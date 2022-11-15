package sequentialSolution;


import static sequentialSolution.Constants.ONE;
import static sequentialSolution.Constants.TWO;
import static sequentialSolution.Constants.ZERO;

import java.util.Map;
import java.util.Objects;

/**
 * Class Course that stores the course information
 */
public class Course {

  private String codeModule;
  private String codePresentation;
  private String courseLength;
  private Map<Integer, Integer> dateToSumClicks;

  /**
   * Constructs the Course object
   *
   * @param courseInfo      String array that stores the class information including code_module,
   *                        code_presentation, course_length
   * @param dateToSumClicks Map that stores the date and its corresponding sum clicks
   */
  public Course(String[] courseInfo, Map<Integer, Integer> dateToSumClicks) {
    this.codeModule = courseInfo[ZERO];
    this.codePresentation = courseInfo[ONE];
    this.courseLength = courseInfo[TWO];
    this.dateToSumClicks = dateToSumClicks;
  }

  /**
   * Updates the date to sum clicks map
   *
   * @param dateString      date information
   * @param sumClicksString sum clicks information
   */
  public void updateSumClicks(String dateString, String sumClicksString) {
    int date = Integer.parseInt(dateString);
    int sumClicks = Integer.parseInt(sumClicksString);
    if (!this.dateToSumClicks.containsKey(date)) {
      this.dateToSumClicks.put(date, sumClicks);
    } else {
      Integer prevSumClicks = this.dateToSumClicks.get(date);
      this.dateToSumClicks.put(date, prevSumClicks + sumClicks);
    }
  }

  /**
   * Gets the map that contains the date and its corresponding sum clicks
   *
   * @return date to sum clicks map
   */
  public Map<Integer, Integer> getDateToSumClicks() {
    return (Map<Integer, Integer>) this.dateToSumClicks;
  }

  /**
   * Tests the Course object equals to the passed Object o
   *
   * @param o the passed Object o
   * @return boolean that indicates the equality
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Course course = (Course) o;
    return Objects.equals(this.codeModule, course.codeModule) ||
        Objects.equals(this.codePresentation, course.codePresentation) ||
        Objects.equals(this.courseLength, course.courseLength) ||
        Objects.equals(this.dateToSumClicks, course.dateToSumClicks);
  }

  /**
   * Returns the hash code of the Course object
   *
   * @return a hash code integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.codeModule, this.codePresentation, this.courseLength,
        this.dateToSumClicks);
  }

  /**
   * Returns the string represents the Course information
   *
   * @return a string about the Course object
   */
  @Override
  public String toString() {
    return "Course: { code module: " + this.codeModule + "; "
        + "code presentation: " + this.codePresentation + "; "
        + "course length: " + this.courseLength + "; "
        + "date to sum clicks tree map: " + this.dateToSumClicks.toString()
        + " }";
  }
}
