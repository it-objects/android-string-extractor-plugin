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

public class FlavorScannerTest {
  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private StringValuesReader stringValuesReader;
  private StringValuesWriter stringValuesWriter;
  private LayoutScanner layoutScanner;

  private FlavorScanner flavorScanner;

  @Before public void setUp() throws IOException {
    stringValuesReader = mock(StringValuesReader.class);
    stringValuesWriter = mock(StringValuesWriter.class);
    layoutScanner = mock(LayoutScanner.class);

    flavorScanner = new FlavorScanner(stringValuesReader, stringValuesWriter, layoutScanner);
  }

  @Test public void when_scanFolder_should_returnFlavors() throws Exception {
    List<Flavor> expected = createDummyFlavors();
    File projectPath = createDummyFlavorsInPath();

    List<Flavor> actual = flavorScanner.scan(projectPath);

    assertThat(actual, equalTo(expected));
  }

  private List<Flavor> createDummyFlavors() {
    return Collections.singletonList(
        new Flavor(new File(folder.getRoot(), "app/src/main/"), stringValuesReader,
            stringValuesWriter,
            layoutScanner));
  }

  private File createDummyFlavorsInPath() {
    File temporaryProjectPath = folder.getRoot();
    File flavorPath = new File(temporaryProjectPath, "app/src/main/");

    flavorPath.mkdirs();

    return new File(temporaryProjectPath.getPath() + "/app");
  }
}