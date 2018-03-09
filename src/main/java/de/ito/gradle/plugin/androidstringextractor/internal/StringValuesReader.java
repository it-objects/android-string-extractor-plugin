package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class StringValuesReader {
  private XmlFileReader xmlFileReader;

  StringValuesReader(XmlFileReader xmlFileReader) {
    this.xmlFileReader = xmlFileReader;
  }

  StringValues read(File flavorPath)
          throws ParserConfigurationException, SAXException, IOException {
    File stringValuesFile = new File(flavorPath, "res/values/string_layouts.xml");

    return resolveStringValues(stringValuesFile);
  }

  private StringValues resolveStringValues(File stringValuesFile)
          throws ParserConfigurationException, SAXException, IOException {
    if (!stringValuesFile.exists()) return new StringValues();

    Document document = xmlFileReader.read(stringValuesFile);
    NodeList strings = document.getElementsByTagName("string");

    Map<String, String> values = resolveStringValues(strings);

    return new StringValues(values);
  }

  private Map<String, String> resolveStringValues(NodeList strings) {
    Map<String, String> values = new LinkedHashMap<>();
    for (int i = 0; i < strings.getLength(); i++) {
      handleNode(strings.item(i), values);
    }
    return values;
  }

  private void handleNode(Node stringNode, Map<String, String> values) {
    Node stringNodeNameAttribute = stringNode.getAttributes().getNamedItem("name");
    Node stringNodeValueAttribute = stringNode.getFirstChild();
    if (stringNodeNameAttribute != null && stringNodeValueAttribute != null) {
      values.put(stringNodeNameAttribute.getNodeValue(), stringNodeValueAttribute.getNodeValue());
    }
  }
}
