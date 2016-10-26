package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class LayoutScannerTest {
  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private LayoutScanner layoutScanner;
  private XmlFileReader xmlFileReader;
  private LayoutParser layoutParser;
  private XmlFileWriter xmlFileWriter;
  private ReferenceReplacer referenceReplacer;

  @Before public void setUp() throws Exception {
    xmlFileReader = mock(XmlFileReader.class);
    layoutParser = mock(LayoutParser.class);
    xmlFileWriter = mock(XmlFileWriter.class);
    referenceReplacer = mock(ReferenceReplacer.class);

    layoutScanner =
        new LayoutScanner(xmlFileReader, layoutParser, xmlFileWriter, referenceReplacer);
  }

  @Test public void when_scanFlavor_should_returnLayouts() throws Exception {
    List<Layout> expected = createDummyLayouts();

    List<Layout> actual = layoutScanner.scan(folder.getRoot());

    assertThat(actual, equalTo(expected));
  }

  private List<Layout> createDummyLayouts() throws IOException {
    File layoutFolder = folder.newFolder("res", "layout");
    File layoutFile = new File(layoutFolder, "layout.xml");
    layoutFile.createNewFile();

    Layout layout =
        new Layout(layoutFile, xmlFileReader, layoutParser, xmlFileWriter, referenceReplacer);

    return Collections.singletonList(layout);
  }
}