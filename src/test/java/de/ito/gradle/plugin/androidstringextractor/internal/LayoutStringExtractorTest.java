package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LayoutStringExtractorTest {
  private MockAndroidProjectFactory factory;
  private List<StringOccurrence> expectedStrings;
  private StringValues expectedStringValues;

  @Before
  public void setUp() throws Exception {
    factory = new MockAndroidProjectFactory();

    expectedStrings =
        Arrays.asList(new StringOccurrence("id1", "attr1", "@string/layout1_id1_attr1"),
            new StringOccurrence("id2", "attr2", "@string/layout1_id2_attr2"));

    expectedStringValues = new StringValues();
    expectedStringValues.put("layout1_id1_attr1", "referenced string");
    expectedStringValues.put("layout1_id2_attr2", "hardcoded string");
  }

  @Test
  public void when_extract_should_modifyLayoutAndStringValues() throws Exception {
    LayoutStringExtractor extractor = new LayoutStringExtractor(factory);

    extractor.extract("");

    verify(factory.layout).writeStrings(expectedStrings);
    verify(factory.flavor).writeStringValues(expectedStringValues);
  }

  private class MockAndroidProjectFactory extends AndroidProjectFactory {
    final Flavor flavor;
    final Layout layout;

    MockAndroidProjectFactory() throws IOException, SAXException, ParserConfigurationException {
      layoutParser = mock(LayoutParser.class);
      xmlFileReader = mock(XmlFileReader.class);
      xmlFileWriter = mock(XmlFileWriter.class);
      stringValuesReader = mock(StringValuesReader.class);
      stringValuesWriter = mock(StringValuesWriter.class);
      layoutScanner = mock(LayoutScanner.class);
      flavorScanner = mock(FlavorScanner.class);

      flavor = mock(Flavor.class);
      List<Flavor> flavors = Collections.singletonList(flavor);
      StringValues stringValues = generateActualStringValues();
      layout = mock(Layout.class);
      List<Layout> layouts = Collections.singletonList(layout);
      List<StringOccurrence> strings = generateActualStrings();

      when(flavorScanner.scan(any(File.class))).thenReturn(flavors);
      when(flavor.readStringValues()).thenReturn(stringValues);
      when(flavor.readLayouts()).thenReturn(layouts);
      when(layout.readStrings()).thenReturn(strings);
      when(layout.computeStringReference(any(StringOccurrence.class))).thenReturn(
          "layout1_id2_attr2");
    }

    private StringValues generateActualStringValues() {
      StringValues values = new StringValues();
      values.put("layout1_id1_attr1", "referenced string");

      return values;
    }

    private List<StringOccurrence> generateActualStrings() {
      StringOccurrence referencedString =
          new StringOccurrence("id1", "attr1", "@string/layout1_id1_attr1");
      StringOccurrence hardcodedString = new StringOccurrence("id2", "attr2", "hardcoded string");
      return Arrays.asList(referencedString, hardcodedString);
    }
  }
}
