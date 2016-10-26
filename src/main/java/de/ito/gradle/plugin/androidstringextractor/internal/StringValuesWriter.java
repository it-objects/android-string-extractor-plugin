package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

class StringValuesWriter {
  static final String DO_NOT_MODIFY_NOTE =
      "\tThis file is auto-generated DO NOT insert new strings here manually!!!\n\t\tJust insert the string value"
          + " in the respective widget text within the layout file, this value will then be automatically added to this file"
          + " with the correct reference\t\n";

  private XmlFileWriter xmlFileWriter;

  StringValuesWriter(XmlFileWriter xmlFileWriter) {
    this.xmlFileWriter = xmlFileWriter;
  }

  void write(StringValues stringValues, File flavorPath)
      throws ParserConfigurationException, TransformerException, IOException {
    Document document = buildStringValuesDocument(stringValues);
    File stringValuesFile = new File(flavorPath, "res/values/string_layouts.xml");

    xmlFileWriter.write(document, stringValuesFile);
  }

  private Document buildStringValuesDocument(StringValues stringValues) throws
      ParserConfigurationException {
    Document stringValuesDocument = Util.createEmptyDocument();
    Comment doNotEditManuallyNote = createNote(stringValuesDocument);
    Element resources = stringValuesDocument.createElement("resources");

    appendStrings(stringValuesDocument, resources, stringValues);

    stringValuesDocument.appendChild(doNotEditManuallyNote);
    stringValuesDocument.appendChild(resources);

    return stringValuesDocument;
  }

  private Comment createNote(Document stringValuesDocument) {
    return stringValuesDocument.createComment(DO_NOT_MODIFY_NOTE);
  }

  private void appendStrings(Document document, Element resources, StringValues stringValues) {
    Map<String, String> values = stringValues.getValues();
    for (String key : values.keySet()) {
      Element string = buildString(document, key, values.get(key));

      resources.appendChild(string);
    }
  }

  private Element buildString(Document document, String key, String stringValue) {
    Element string = document.createElement("string");

    Attr name = document.createAttribute("name");
    name.setValue(key);
    string.setAttributeNode(name);

    Text value = document.createTextNode(stringValue);
    string.appendChild(value);

    return string;
  }
}
