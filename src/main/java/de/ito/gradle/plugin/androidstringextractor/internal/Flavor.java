package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.xml.sax.SAXException;

class Flavor {
  private final File path;
  private final StringValuesReader stringValuesReader;
  private final StringValuesWriter stringValuesWriter;
  private final LayoutScanner layoutScanner;

  Flavor(File path, StringValuesReader stringValuesReader,
      StringValuesWriter stringValuesWriter, LayoutScanner layoutScanner) {
    this.path = path;
    this.stringValuesReader = stringValuesReader;
    this.stringValuesWriter = stringValuesWriter;
    this.layoutScanner = layoutScanner;
  }

  StringValues readStringValues() throws IOException, SAXException, ParserConfigurationException {
    return stringValuesReader.read(path);
  }

  void writeStringValues(StringValues stringValues)
      throws IOException, TransformerException, ParserConfigurationException {
    stringValuesWriter.write(stringValues, path);
  }

  List<Layout> readLayouts() {
    return layoutScanner.scan(path);
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
