package parser;

import java.io.FileWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class PortfolioWriter {
  String fileName;
  FileWriter file;
  XMLOutputFactory factory = XMLOutputFactory.newFactory();
  XMLStreamWriter writer;
  int counter = 0;

  public PortfolioWriter(String name) {
    try {
      fileName = "src/data/portfolios/" + name + ".xml";
      file = new FileWriter(fileName);
      writer = factory.createXMLStreamWriter(file);
      writer.writeStartDocument();
      writer.writeStartElement("portfolio");
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public void writeStock(String date, String symbol, double shares, double value) {
    try {
      writer.writeStartElement("stock");
      writer.writeAttribute("index", String.valueOf(counter));
      writer.writeStartElement("date");
      writer.writeCharacters(date);
      writer.writeEndElement();
      writer.writeStartElement("symbol");
      writer.writeCharacters(symbol);
      writer.writeEndElement();
      writer.writeStartElement("shares");
      writer.writeCharacters(String.valueOf(shares));
      writer.writeEndElement();
      writer.writeStartElement("value");
      writer.writeCharacters(String.valueOf(value));
      writer.writeEndElement();
      writer.writeEndElement();
      counter++;
    } catch (XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }

  public void close() {
    try {
      writer.writeEndElement();
      writer.writeEndDocument();
      writer.flush();
      writer.close();
      file.close();
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
}
