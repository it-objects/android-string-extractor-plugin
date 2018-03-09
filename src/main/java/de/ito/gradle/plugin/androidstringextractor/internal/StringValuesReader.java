package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class StringValuesReader {
  private XmlFileReader xmlFileReader;
  private Logger logger = Logger.getAnonymousLogger();

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
      try {
        handleNode(strings.item(i), values);
        throw new RuntimeException ("test");
      }catch(RuntimeException e){
          logNodeHandlingException(strings.item(i), e);
      }
    }
    return values;
  }

    private void logNodeHandlingException(Node node, RuntimeException e)  {
        try {
            logger.log(Level.SEVERE,"an unexpected error occurred while reading string_layouts.\n entry '"+convertNodeToText(node)+"' will not be considered",e);
        } catch (TransformerException e1) {
            logger.log(Level.SEVERE,"an unexpected error occurred while reading string_layouts.\n entry will not be considered",e);
        }
    }

    private String convertNodeToText(Node node )throws TransformerException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter sw = new StringWriter();
        t.transform(new DOMSource(node), new StreamResult(sw));
        return sw.toString();
    }

    private void handleNode(Node stringNode, Map<String, String> values) {
    Node stringNodeNameAttribute = stringNode.getAttributes().getNamedItem("name");
    Node stringNodeValueAttribute = stringNode.getFirstChild();
    if (stringNodeNameAttribute != null && stringNodeValueAttribute != null) {
      values.put(stringNodeNameAttribute.getNodeValue(), stringNodeValueAttribute.getNodeValue());
    }
  }
}
