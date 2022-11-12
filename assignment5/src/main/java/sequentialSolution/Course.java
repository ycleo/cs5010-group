package sequentialSolution;


import static sequentialSolution.FileReader.ZERO;
import static sequentialSolution.FileReader.ONE;
import static sequentialSolution.FileReader.TWO;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Course {

  private String codeModule;
  private String codePresentation;
  private String courseLength;
  private Map<Integer, Integer> dateToSumClicks;

  public Course(String[] courseInfo) {
    this.codeModule = courseInfo[ZERO];
    this.codePresentation = courseInfo[ONE];
    this.courseLength = courseInfo[TWO];
    this.dateToSumClicks = new TreeMap<>();
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

  public TreeMap<Integer, Integer> getDateToSumClicks() {
    return (TreeMap<Integer, Integer>) this.dateToSumClicks;
  }
}
