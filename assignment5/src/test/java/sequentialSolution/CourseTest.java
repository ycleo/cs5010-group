package sequentialSolution;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

class CourseTest {

  @Test
  void testEquals() {
    String[] args1 = new String[]{"abc", "def", "g"};
    String[] args2 = new String[]{"123", "def", "g"};
    String[] args3 = new String[]{"123", "456", "g"};
    String[] args4 = new String[]{"123", "456", "7"};
    Course course1 = new Course(args1, new HashMap<>());
    Course course2 = new Course(args1, new HashMap<>());
    Course course3 = new Course(args2, new HashMap<>());
    Course course4 = new Course(args3, new HashMap<>());
    Course course5 = new Course(args4, new HashMap<>());
    assertTrue(course1.equals(course1));
    assertFalse(course1.equals(null));
    assertTrue(course1.equals(course2));
    assertFalse(course1.equals(args1));
    assertTrue(course1.equals(course3));
    assertTrue(course1.equals(course4));
    assertTrue(course1.equals(course5));
  }

  @Test
  void testHashCode() {
    String[] args = new String[]{"abc", "def", "g"};
    Course course = new Course(args, new HashMap<>());
    assertEquals(course.hashCode(), course.hashCode());
  }

  @Test
  void testToString() {
    String[] args = new String[]{"abc", "def", "g"};
    Course course = new Course(args, new HashMap<>());
    assertEquals("Course: { code module: abc; code presentation: def; course length: g; date to sum clicks tree map: {} }", course.toString());
  }
}