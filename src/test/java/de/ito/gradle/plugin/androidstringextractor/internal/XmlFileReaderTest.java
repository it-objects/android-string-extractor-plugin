package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.gradle.internal.impldep.com.google.common.io.Files;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Document;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class XmlFileReaderTest {
  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private XmlFileReader xmlFileReader;

  @Before public void setUp() throws IOException {
    this.xmlFileReader = new XmlFileReader();
  }

  @Test
  public void when_readDocumentFromFile_should_readFileContent() throws Exception {
    String expected =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tag>value</tag>";
    File file = createXmlFileFrom(expected);

    Document actual = xmlFileReader.read(file);

    assertThat(contentOf(actual), equalTo(expected));
  }

  private File createXmlFileFrom(String content) throws IOException {
    File file = new File(folder.getRoot(), "file.xml");

    Files.write(content, file, Charset.forName("UTF-8"));

    return file;
  }

  private String contentOf(Document document) throws TransformerException {
    DOMSource domSource = new DOMSource(document);
    StringWriter writer = new StringWriter();
    StreamResult result = new StreamResult(writer);

    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.INDENT, "no");
    transformer.transform(domSource, result);

    return writer.toString();
  }
}