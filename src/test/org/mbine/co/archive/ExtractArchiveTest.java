package org.mbine.co.archive;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExtractArchiveTest {
   private static final String modelCoV2 = "omex_files/iAB_AMO1410_SARS-CoV-2.omex";
   private static final String modelBIOMD1000 = "biomd1000/BIOMD0000001000.omex";
   private ICombineArchive archive;
   private Path tmpFile;
   private Path zipPath;

   @Before
   public void setUp() throws Exception {
      File file = pickTestOmexFile(modelBIOMD1000);
      zipPath = Paths.get(file.toURI());
      Set<PosixFilePermission> perms = PosixFilePermissions.fromString("r--r--r--");
      FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
      tmpFile = Files.createTempFile("tmp", ".zip", attr);
      Files.copy(zipPath, tmpFile, StandardCopyOption.REPLACE_EXISTING);
      CombineArchiveFactory factory = new CombineArchiveFactory();
      archive = factory.openArchive(tmpFile.toString(), false);
   }

   @After
   public void tearDown() throws Exception {
      if (archive != null && archive.isOpen()) {
         archive.close();
      }
      Files.deleteIfExists(tmpFile);
      tmpFile = null;
      zipPath = null;
   }

   @Test
   public void testExtractMasterFile() throws Exception {
      boolean res = archive.isOpen();
      assertEquals(true, res);
      System.out.println(tmpFile.toAbsolutePath());
      System.out.println(tmpFile.getParent().toString());
      Iterator<ArtifactInfo> iterator = archive.artifactIterator();
      while (iterator.hasNext()) {
         ArtifactInfo entry = iterator.next();
         String path = entry.getPath();
         System.out.println(entry);
         assertNotNull(path);
         String format = entry.getFormat();
         boolean master = entry.isMaster();
         if (format.contains("sbml.level-") && master) {
            InputStream stream = archive.readArtifact(entry);
            String result = convertInputStreamToString(stream);
            System.out.println(result);
         }
      }
   }

   private static String convertInputStreamToString(InputStream is) throws IOException {
      String newLine = System.getProperty("line.separator");
      String result;
      try (Stream<String> lines = new BufferedReader(new InputStreamReader(is)).lines()) {
         result = lines.collect(Collectors.joining(newLine));
      }

      return result;
   }

   private static File pickTestOmexFile(final String path) {
      ClassLoader classLoader = ExtractArchiveTest.class.getClassLoader();
      return new File(classLoader.getResource(path).getFile());
   }
}
