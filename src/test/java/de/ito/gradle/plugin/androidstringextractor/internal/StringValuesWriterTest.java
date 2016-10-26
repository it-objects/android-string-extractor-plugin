package de.ito.gradle.plugin.androidstringextractor.internal;

import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.w3c.dom.Document;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StringValuesWriterTest {

  private XmlFileWriter xmlFileWriter;
  private StringValuesWriter stringValuesWriter;

  @Before public void setUp() throws Exception {
    xmlFileWriter = mock(XmlFileWriter.class);
    stringValuesWriter = new StringValuesWriter(xmlFileWriter);
  }

  @Test public void when_writeStringValuesToFile_should_writeValuesToFile() throws Exception {
    Document expectedDocument = createExpectedDocument();
    StringValues stringValues = StringValuesReaderTest.createDummyStringValues();
    File flavorPath = new File("");
    File stringValuesFile = new File(flavorPath, "res/values/string_layouts.xml");

    stringValuesWriter.write(stringValues, flavorPath);

    verify(xmlFileWriter).write(eq(expectedDocument), Matchers.eq(stringValuesFile));
  }

  private Document createExpectedDocument()
      throws ParserConfigurationException, TransformerException {
    Document document = StringValuesReaderTest.createDummyDocument();

    document.insertBefore(document.createComment(StringValuesWriter.DO_NOT_MODIFY_NOTE),
        document.getFirstChild());

    return document;
  }

  private Document eq(Document document) {
    return argThat(new DocumentMatch(document));
  }

  private class DocumentMatch extends BaseMatcher<Document> {
    private final Document expectedDocument;

    private DocumentMatch(Document expectedDocument) {
      this.expectedDocument = expectedDocument;
    }

    @Override public boolean matches(final Object actualDocument) {
      final Document actual = (Document) actualDocument;
      return actual.isEqualNode(expectedDocument);
    }

    @Override public void describeTo(final Description description) {
      description.appendText("Documents do not match.");
    }
  }
}