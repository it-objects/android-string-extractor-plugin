package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

class AndroidProject {
  private final File projectPath;
  private final FlavorScanner flavorScanner;

  AndroidProject(File projectPath, FlavorScanner flavorScanner) {
    this.projectPath = projectPath;
    this.flavorScanner = flavorScanner;
  }

  List<Flavor> readFlavors() throws FileNotFoundException {
    return flavorScanner.scan(projectPath);
  }
}
