package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlavorTest {
  private StringValuesReader stringValuesReader;
  private StringValuesWriter stringValuesWriter;
  private LayoutScanner layoutScanner;
  private File path;

  private Flavor flavor;

  @Before public void setUp() throws Exception {
    stringValuesReader = mock(StringValuesReader.class);
    stringValuesWriter = mock(StringValuesWriter.class);
    layoutScanner = mock(LayoutScanner.class);
    path = mock(File.class);

    flavor = new Flavor(path, stringValuesReader, stringValuesWriter, layoutScanner);
  }

  @Test public void when_readStringValues_should_returnStringValues() throws Exception {
    StringValues expected = dummyStringValues();
    when(stringValuesReader.read(any(File.class))).thenReturn(expected);

    StringValues actual =
        flavor.readStringValues();

    assertThat(actual, equalTo(expected));
  }

  @Test public void when_writeStringValues_should_writeStringValues() throws Exception {
    StringValues stringValues = dummyStringValues();

    flavor.writeStringValues(stringValues);

    verify(stringValuesWriter).write(eq(stringValues), any(File.class));
  }

  @Test public void when_readLayouts_should_returnLayouts() throws Exception {
    List<Layout> expected = dummyLayouts();
    when(layoutScanner.scan(any(File.class))).thenReturn(expected);

    List<Layout> actual = flavor.readLayouts();

    assertThat(actual, equalTo(expected));
  }

  private StringValues dummyStringValues() {
    StringValues stringValues = new StringValues();

    return stringValues;
  }

  private List<Layout> dummyLayouts() {
    Layout layout = new Layout(new File("layout.xml"), null, null, null, null);

    return Collections.singletonList(layout);
  }
}