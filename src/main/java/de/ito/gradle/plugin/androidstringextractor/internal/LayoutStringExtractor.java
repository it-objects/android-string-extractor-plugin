package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

public class LayoutStringExtractor {
  private final AndroidProjectFactory factory;

  public LayoutStringExtractor(AndroidProjectFactory factory) {
    this.factory = factory;
  }

  public void extract(String projectPath)
      throws TransformerException, IOException, ParserConfigurationException, SAXException,
      XPathExpressionException {
    AndroidProject project = factory.create(new File(projectPath));
    List<Flavor> flavors = project.readFlavors();
    for (Flavor flavor : flavors) {
      handleFlavor(flavor);
    }
  }

  private void handleFlavor(Flavor flavor)
      throws TransformerException, IOException, ParserConfigurationException, SAXException,
      XPathExpressionException {
    StringValues stringValues = flavor.readStringValues();

    List<Layout> layouts = flavor.readLayouts();
    for (Layout layout : layouts) {
      handleLayout(layout, stringValues);
    }

    if(stringValues.hasChanged()) flavor.writeStringValues(stringValues);
  }

  private void handleLayout(Layout layout, StringValues stringValues)
      throws TransformerException, IOException, ParserConfigurationException, SAXException,
      XPathExpressionException {
    List<StringOccurrence> stringOccurrences = layout.readStrings();
    for (StringOccurrence string : stringOccurrences) {
      handleString(string, layout, stringValues);
    }

    layout.writeStrings(stringOccurrences);
  }

  private void handleString(StringOccurrence occurrence, Layout layout, StringValues stringValues) {
    if (occurrence.hasHardCodedValue()) {
      String key = layout.computeStringReference(occurrence);
      stringValues.put(key, occurrence.getValue());
      occurrence.replaceHardCodedValueByReference(key);
    }
  }
}
