package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

class Layout {
  private final static Pattern FILE_NAME_PATTERN = Pattern.compile("(?<name>\\w+)\\.xml");

  private final String name;
  private final File file;
  private final XmlFileReader xmlFileReader;
  private final LayoutParser layoutParser;
  private final XmlFileWriter xmlFileWriter;
  private final ReferenceReplacer referenceReplacer;

  Document document;

  Layout(File file, XmlFileReader xmlFileReader, LayoutParser layoutParser,
      XmlFileWriter xmlFileWriter, ReferenceReplacer referenceReplacer) {
    this.name = resolveName(file);
    this.file = file;
    this.layoutParser = layoutParser;
    this.xmlFileReader = xmlFileReader;
    this.xmlFileWriter = xmlFileWriter;
    this.referenceReplacer = referenceReplacer;
  }

  String resolveName(File file) {
    Matcher matcher = FILE_NAME_PATTERN.matcher(file.getName());

    if (!matcher.matches()) {
      throw new IllegalArgumentException();
    }

    return matcher.group("name");
  }

  List<StringOccurrence> readStrings()
      throws ParserConfigurationException, SAXException, IOException {
    document = xmlFileReader.read(file);

    return layoutParser.parse(document);
  }

  void writeStrings(List<StringOccurrence> strings)
      throws TransformerException, IOException, XPathExpressionException {
    if (document == null) throw new IllegalStateException("You must read strings first.");

    referenceReplacer.replaceHardCodedValues(document, strings);

    xmlFileWriter.write(document, file);

    document = null;
  }

  String computeStringReference(StringOccurrence occurrence) {
    String template = "%s_%s_%s";
    return String.format(template, name, occurrence.getId(), occurrence.getAttribute());
  }

  @Override public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }

  @Override public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  @Override public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
