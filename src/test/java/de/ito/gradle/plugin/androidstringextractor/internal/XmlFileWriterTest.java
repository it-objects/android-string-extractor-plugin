package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class XmlFileWriterTest {
  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private XmlFileWriter xmlFileWriter;

  @Before
  public void setUp() throws Exception {
    xmlFileWriter = new XmlFileWriter();
  }

  @Test
  public void when_writeDocumentToFile_should_writeFileWithContent() throws Exception {
    Document document = dummyDocument();
    File file = new File(folder.getRoot(), "file.xml");
    String expected =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tag>value</tag>";

    xmlFileWriter.write(document, file);

    assertThat(contentOf(file), equalTo(expected));
  }

  private Document dummyDocument() throws ParserConfigurationException {
    Document document = Util.createEmptyDocument();
    Element tag = document.createElement("tag");
    Text value = document.createTextNode("value");

    tag.appendChild(value);
    document.appendChild(tag);

    return document;
  }

  private String contentOf(File file) throws IOException {
    List<String> content = (Files.readAllLines(Paths.get(file.toURI()), Charset.forName("UTF-8")));
    String contentOf = "";
    for (String line : content) {
      contentOf += line;
    }
    
    return contentOf;
  }
}