package assignment4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is for reading and getting files
 */
public class FileUtils {

  /**
   * Get all paths under the input path
   *
   * @param path path
   * @return a list of paths that under the input path
   */
  public List<String> getPaths(final String path) {
    try (Stream<Path> stream = Files.list(Paths.get(path))) {
      return stream
          .filter(file -> !Files.isDirectory(file))
          .map(Path::getFileName)
          .map(Path::toString)
          .map(filePath -> String.join("/", path, filePath))
          .collect(Collectors.toList());
    } catch (final IOException ex) {
      throw new InvalidCommandException(
          String.format("Invalid path for getting grammar files"), ex);
    }
  }

  /**
   * this method is for reading the grammar file with the input path
   *
   * @param path path
   * @return the grammar
   */
  public Grammar readGrammar(final String path) {
    try {
      final String rawFile = Files.readString(Paths.get(path));
      return new ObjectMapper().readValue(rawFile, Grammar.class);
    } catch (final IOException ex) {
      throw new InvalidJsonException(
          String.format("Invalid path for reading grammar file: %s", path), ex);
    }
  }
}
