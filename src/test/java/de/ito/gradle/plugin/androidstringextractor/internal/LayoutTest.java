package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LayoutTest {
  private File file;
  private XmlFileReader xmlFileReader;
  private LayoutParser layoutParser;
  private XmlFileWriter xmlFileWriter;
  private ReferenceReplacer referenceReplacer;

  private Layout layout;

  @Before public void setUp() throws Exception {
    file = mock(File.class);
    xmlFileReader = mock(XmlFileReader.class);
    layoutParser = mock(LayoutParser.class);
    xmlFileWriter = mock(XmlFileWriter.class);
    referenceReplacer = mock(ReferenceReplacer.class);

    when(file.getName()).thenReturn("layout.xml");

    layout =
        new Layout(file, xmlFileReader, layoutParser, xmlFileWriter, referenceReplacer);
  }

  @Test public void when_resolveName_should_returnLayoutName() throws Exception {
    String expected = "layout";
    File layoutFile = new File(String.format("file://project/res/layout/%s.xml", expected));

    String actual = layout.resolveName(layoutFile);

    assertThat(actual, equalTo(expected));
  }

  @Test public void when_readStrings_should_returnStringOccurrences() throws Exception {
    List<StringOccurrence> expected = dummyStrings();
    when(layoutParser.parse(any(Document.class))).thenReturn(expected);

    List<StringOccurrence> actual = layout.readStrings();

    assertThat(actual, equalTo(expected));
  }

  @Test public void when_writeStrings_should_writeStrings() throws Exception {
    List<StringOccurrence> strings = dummyStrings();
    Document document = mock(Document.class);
    layout.document = document;

    layout.writeStrings(strings);

    verify(xmlFileWriter).write(document, file);
    assertThat(layout.document, is(nullValue()));
  }

  @Test public void when_computeStringReference_should_returnStringReference() throws Exception {
    String expected = "layout_id_attribute";
    StringOccurrence stringOccurrence = new StringOccurrence("id", "attribute", "value");

    String actual = layout.computeStringReference(stringOccurrence);

    assertThat(actual, equalTo(expected));
  }

  private List<StringOccurrence> dummyStrings() {
    return Collections.singletonList(dummyString());
  }

  private StringOccurrence dummyString() {
    return new StringOccurrence("id", "attribute", "value");
  }
}