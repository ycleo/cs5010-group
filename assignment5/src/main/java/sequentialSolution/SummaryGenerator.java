package sequentialSolution;

import static sequentialSolution.Constants.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SummaryGenerator {


  public void generateSummary(String summaryOutputPath, HashMap<String, Course> coursesMap)
      throws IOException {


    Iterator courseIterator = coursesMap.entrySet().iterator();
    while (courseIterator.hasNext()) {

      Map.Entry coursePair = (Map.Entry) courseIterator.next();
      String courseModuleAndPresentation = (String) coursePair.getKey();
      Course course = (Course) coursePair.getValue();
      TreeMap<Integer, Integer> dateToSumClicks = course.getDateToSumClicks();
      ArrayList<ArrayList<String>> dateSumClicksArray = mapToArray(dateToSumClicks);
      FileWriter summaryWriter = new FileWriter(new File(summaryOutputPath + SLASH + courseModuleAndPresentation + CSV_EXTENSION));
      summaryWriter.append(OUTPUT_ROW_FORMAT + NEXT_LINE);

      for (ArrayList<String> rowData : dateSumClicksArray) {
        summaryWriter.append(String.join(COMMA, rowData));
        summaryWriter.append(NEXT_LINE);
      }
      summaryWriter.flush();
      summaryWriter.close();

      courseIterator.remove();
    }

  }

  private ArrayList<ArrayList<String>> mapToArray(TreeMap<Integer, Integer> map) {
    ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();
    Set entries = map.entrySet();
    Iterator entriesIterator = entries.iterator();
    while(entriesIterator.hasNext()){
      Map.Entry mapping = (Map.Entry) entriesIterator.next();
      ArrayList<String> item = new ArrayList<>(
          List.of(
              String.valueOf(mapping.getKey()),
              String.valueOf(mapping.getValue()))
          );
      arr.add(item);
    }
    return arr;
  }


}
