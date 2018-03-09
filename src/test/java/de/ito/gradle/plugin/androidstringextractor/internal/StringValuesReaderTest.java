package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringValuesReaderTest {
  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private StringValuesReader stringValuesReader;
  private XmlFileReader xmlFileReader;

  @Before public void setUp() throws Exception {
    xmlFileReader = mock(XmlFileReader.class);

    stringValuesReader = new StringValuesReader(xmlFileReader);
  }

  @Test public void when_readStringValuesFromFile_should_returnStringValues() throws Exception {
    StringValues expected = createDummyStringValues();
    Document dummyStringValues = createDummyDocument();
    when(xmlFileReader.read(any(File.class))).thenReturn(dummyStringValues);
    File flavorPath = createFileStructure();

    StringValues actual = stringValuesReader.read(flavorPath);

    assertThat(actual, equalTo(expected));
  }

  @Test
  public void given_invalidFormatNode_when_readStringValuesFromFile_should_returnStringValues_andIgnoreInvalidNode()
          throws Exception {
    StringValues expected = createDummyStringValues();
    Document dummyStringValues = createDummyDocumentWithInvalidNode();
    when(xmlFileReader.read(any(File.class))).thenReturn(dummyStringValues);
    File flavorPath = createFileStructure();

    StringValues actual = stringValuesReader.read(flavorPath);

    assertThat(actual, equalTo(expected));
  }

  private File createFileStructure() throws IOException {
    File flavorPath = new File(folder.getRoot(), "flavor/");
    File stringValuesFile = new File(flavorPath, "res/values/string_layouts.xml");
    stringValuesFile.mkdirs();
    stringValuesFile.createNewFile();
    return flavorPath;
  }

  static StringValues createDummyStringValues() {
    Map<String, String> values = new LinkedHashMap<>();

    values.put("name", "value");

    return new StringValues(values);
  }

  static Document createDummyDocument() throws ParserConfigurationException {
    Document document = Util.createEmptyDocument();

    Element resources = document.createElement("resources");
    Element string = createStringNodeEntry(document);

    resources.appendChild(string);
    document.appendChild(resources);

    return document;
  }

  private static Element createStringNodeEntry(Document document) {
    Element string = document.createElement("string");
    Attr name = document.createAttribute("name");
    name.setValue("name");
    Text value = document.createTextNode("value");

    string.setAttributeNode(name);
    string.appendChild(value);
    return string;
  }

  static Document createDummyDocumentWithInvalidNode() throws ParserConfigurationException {
    Document document = createDummyDocument();

    document.getElementsByTagName("resources").item(0).appendChild(createStringNodeEntryWithoutValue(document));

    return document;
  }

  private static Element createStringNodeEntryWithoutValue(Document document) {
    Element string = document.createElement("string");
    Attr name = document.createAttribute("name");
    name.setValue("withoutValue");

    string.setAttributeNode(name);
    return string;
  }
}