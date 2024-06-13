package parser;

import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 * A class that writes a stock portfolio to a file. The portfolio is written in XML format.
 */
public class PortfolioWriter implements StockWriter {
  StreamResult result;
  XMLOutputFactory factory = XMLOutputFactory.newFactory();
  XMLStreamWriter tempWriter;
  int counter = 0;
  File tmp = File.createTempFile("xml", null);
  FileWriter stream = new FileWriter(tmp);

  public PortfolioWriter(String name, String date, String directory) throws IOException {
    try {
      FileWriter streamWriter = new FileWriter(directory + name + ".xml");
      result = new StreamResult(streamWriter);
      tempWriter = factory.createXMLStreamWriter(stream);
      tempWriter.writeStartDocument();
      tempWriter.writeStartElement(name);
      tempWriter.writeAttribute("date", date);
    } catch (XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }

  @Override
  public void writeStock(String symbol, double shares) {
    try {
      tempWriter.writeStartElement("stock");
      tempWriter.writeAttribute("index", String.valueOf(counter));
      tempWriter.writeStartElement("symbol");
      tempWriter.writeCharacters(symbol);
      tempWriter.writeEndElement();
      tempWriter.writeStartElement("shares");
      tempWriter.writeCharacters(String.valueOf(shares));
      tempWriter.writeEndElement();
      tempWriter.writeEndElement();
      counter++;
    } catch (XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }

  @Override
  public void close() {
    try {
      tempWriter.writeEndElement();
      tempWriter.writeEndDocument();
      tempWriter.flush();
      tempWriter.close();
      Transformer transformer = SAXTransformerFactory.newDefaultInstance().newTransformer();
      SAXSource sax = new SAXSource(new InputSource(new FileReader(tmp)));
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "4");
      transformer.transform(sax, result);
    } catch (TransformerException | IOException | IllegalArgumentException | XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }
}
