package sequentialSolution;


import static sequentialSolution.Constants.ONE;
import static sequentialSolution.Constants.TWO;
import static sequentialSolution.Constants.ZERO;

import java.util.Map;
import java.util.Objects;

public class Course {

  private String codeModule;
  private String codePresentation;
  private String courseLength;
  private Map<Integer, Integer> dateToSumClicks;

  public Course(String[] courseInfo, Map<Integer, Integer> dateToSumClicks) {
    this.codeModule = courseInfo[ZERO];
    this.codePresentation = courseInfo[ONE];
    this.courseLength = courseInfo[TWO];
    this.dateToSumClicks = dateToSumClicks;
  }

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

  public Map<Integer, Integer> getDateToSumClicks() {
    return (Map<Integer, Integer>) this.dateToSumClicks;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(this.codeModule, this.codePresentation, this.courseLength,
        this.dateToSumClicks);
  }

  @Override
  public String toString() {
    return "Course: { code module: " + this.codeModule + "; "
        + "code presentation: " + this.codePresentation + "; "
        + "course length: " + this.courseLength + "; "
        + "date to sum clicks tree map: " + this.dateToSumClicks.toString()
        + " }";
  }
}
