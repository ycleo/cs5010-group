package sequentialSolution;

import java.util.HashMap;
import java.util.Map;

public class Course {
  private String codeModule;
  private String codePresentation;
  private String courseLength;
  private Map<Integer, Integer> dateToSumClicks;

  public Course(String[] courseInfo) {
    this.codeModule = courseInfo[0];
    this.codePresentation = courseInfo[1];
    this.courseLength = courseInfo[2];
    this.dateToSumClicks = new HashMap<>();
  }

  public void updateSumClicks(String dateString, String sumClicksString) {
    int date = Integer.parseInt(dateString);
    int sumClicks = Integer.parseInt(sumClicksString);
    if (!this.dateToSumClicks.containsKey(date)) {
      this.dateToSumClicks.put(date, sumClicks);
    } else {
      int prevSumClicks = this.dateToSumClicks.get(date);
      this.dateToSumClicks.put(date, prevSumClicks + sumClicks);
    }
  }

  public HashMap<Integer, Integer> getDateToSumClicks() {
    return (HashMap<Integer, Integer>) this.dateToSumClicks;
  }
}
