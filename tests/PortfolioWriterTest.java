import parser.PortfolioWriter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PortfolioWriterTest {
  PortfolioWriter writer;
  File test;
  String writtenTestFile;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Before
  public void setUp() throws IOException {
    writtenTestFile = "src/data/portfolios/portfolioTest.xml";
    test = folder.newFile();
  }

  @Test
  public void testPortfolioWriter() {
    try {
      writer = new PortfolioWriter("portfolioTest", "2024-06-06",
              folder.getRoot().getAbsolutePath());
      writer.writeStock( "AAPL", 1);
      writer.writeStock("GOOG", 2);
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // TODO: find out why this assert won't pass - implementation is correct
    assertTrue(Files.exists(Path.of(writtenTestFile)));
    assertTrue(Files.exists(Path.of(test.getAbsolutePath())));
    assertEquals(Path.of(writtenTestFile).toFile().length(), test.length());
  }
}