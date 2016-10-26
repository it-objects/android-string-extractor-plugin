package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import static de.ito.gradle.plugin.androidstringextractor.internal.Util.assertPathIsDirectory;

class FlavorScanner {
  private static final FilenameFilter DIRECTORY_FILTER = new DirectoryFilter();

  private final StringValuesReader stringValuesReader;
  private final StringValuesWriter stringValuesWriter;
  private final LayoutScanner layoutScanner;

  FlavorScanner(StringValuesReader stringValuesReader, StringValuesWriter stringValuesWriter,
      LayoutScanner layoutScanner) {
    this.stringValuesReader = stringValuesReader;
    this.stringValuesWriter = stringValuesWriter;
    this.layoutScanner = layoutScanner;
  }

  List<Flavor> scan(File projectPath) throws FileNotFoundException {
    File flavorPath = new File(projectPath, "/src/");

    assertPathIsDirectory(flavorPath);
    File[] flavorDirectories = listDirectoriesInFlavorPath(flavorPath);

    return createFlavorForEachDirectory(flavorDirectories);
  }

  private File[] listDirectoriesInFlavorPath(File flavorPath) {
    return flavorPath.listFiles(DIRECTORY_FILTER);
  }

  private List<Flavor> createFlavorForEachDirectory(File[] flavorDirectories) {
    List<Flavor> flavors = new ArrayList<>();

    for (File flavorDirectory : flavorDirectories) {
      flavors.add(
          new Flavor(flavorDirectory, stringValuesReader, stringValuesWriter, layoutScanner));
    }

    return flavors;
  }

  private static class DirectoryFilter implements FilenameFilter {
    @Override public boolean accept(File dir, String name) {
      File directory = new File(dir, name);

      return directory.isDirectory();
    }
  }
}
