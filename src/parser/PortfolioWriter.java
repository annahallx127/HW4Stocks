package parser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class PortfolioWriter {
  String fileName;
  FileWriter file;
  XMLOutputFactory factory = XMLOutputFactory.newFactory();
  XMLStreamWriter writer;
  int counter = 0;

  public PortfolioWriter(String name, String date, String dir) {
    try {
      file = new FileWriter(dir + name + ".xml");
      writer = factory.createXMLStreamWriter(file);
      writer.writeStartDocument();
      writer.writeStartElement(name);
      writer.writeAttribute("date", date);
    } catch (IOException | XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }

  public void writeStock(String symbol, double shares) {
    try {
      writer.writeStartElement("stock");
      writer.writeAttribute("index", String.valueOf(counter));
      writer.writeEndElement();
      writer.writeStartElement("symbol");
      writer.writeCharacters(symbol);
      writer.writeEndElement();
      writer.writeStartElement("shares");
      writer.writeCharacters(String.valueOf(shares));
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
