package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.ito.gradle.plugin.androidstringextractor.internal.Util.assertPathIsDirectory;

class LayoutScanner {
  private static final FilenameFilter XML_FILTER = new XmlFilter();

  private final XmlFileReader xmlFileReader;
  private final LayoutParser layoutParser;
  private final XmlFileWriter xmlFileWriter;
  private final ReferenceReplacer referenceReplacer;

  LayoutScanner(XmlFileReader xmlFileReader, LayoutParser layoutParser, XmlFileWriter xmlFileWriter,
      ReferenceReplacer referenceReplacer) {
    this.xmlFileReader = xmlFileReader;
    this.layoutParser = layoutParser;
    this.xmlFileWriter = xmlFileWriter;
    this.referenceReplacer = referenceReplacer;
  }

  List<Layout> scan(File flavorPath) {
    File layoutPath = new File(flavorPath, "/res/layout");

    if(!layoutPath.exists()) return Collections.emptyList();
    
    assertPathIsDirectory(layoutPath);
    File[] layoutFiles = layoutPath.listFiles(XML_FILTER);

    return createLayoutForEachFile(layoutFiles);
  }

  private List<Layout> createLayoutForEachFile(File[] layoutFiles) {
    List<Layout> layouts = new ArrayList<>();

    for (File layoutFile : layoutFiles) {
      layouts.add(new Layout(layoutFile, xmlFileReader, layoutParser, xmlFileWriter,
          referenceReplacer));
    }

    return layouts;
  }

  private static class XmlFilter implements FilenameFilter {
    @Override public boolean accept(File dir, String name) {
      return name.endsWith(".xml");
    }
  }
}
