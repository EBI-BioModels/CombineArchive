/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of
 * the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.mbine.co.archive;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import static org.mbine.co.archive.ZipFileAssert.assertEquals;

/**
 * 
 * @author Stuart Moodie
 * @author Mihai Glont
 *
 */
public class CreateNewArchiveTest {
    private static final String PSFILE_NAME = "test_sheet.ps";
    /*
     * This file is used in a byte-by-byte comparison with the OMEX file produced by the
     * testCreateArtifact method below.
     *
     * Because Windows and Unix use different line endings, we need to maintain a reference file
     * for each platform. This convenience method helps us ensure that these unit tests don't
     * compare an OMEX file that has LF (Unix-style) line endings with one that has CRLF separators
     * (Windows-style).
     */
    private static final String EXAMPLE_OMEX = getOSDependentExampleFile();
    private static final String[] IGNORE_FILE_CONTENT = { "metadata.rdf" };
    private static String EXAMPLE_PATH="example_files/example1_test/example_omex";
    private Path omexPath;
    private ICombineArchive arch;

    @Before
    public void setUp() throws Exception {
        if (System.getProperty("os.name").startsWith("Windows")) {
            omexPath = Files.createTempFile("omexTest", ".omex");
        } else {
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-r--r--");
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
            omexPath = Files.createTempFile("omexTest", ".omex", attr);
        }
        CombineArchiveFactory fact = new CombineArchiveFactory();
        String zipPathStr = omexPath.toString();
        Files.delete(omexPath);
        arch = fact.openArchive(zipPathStr, true);
    }


    @After
    public void tearDown() throws Exception{
        if(this.arch.isOpen()){
            this.arch.close();
        }
        this.arch = null;
        Files.deleteIfExists(omexPath);
    }

    @Test
    public void testCreateArtifact() throws Exception {
        Path readMeSrc = FileSystems.getDefault().getPath(EXAMPLE_PATH, "readme.txt");

        // create the artifact 1 from the readme.txt
        String readMeTgt1 = readMeSrc.getFileName().toString();
        ArtifactInfo entry1 = arch.createArtifact(readMeTgt1, "text/plain", true);
        OutputStream writer1 = arch.writeArtifact(entry1);
        Files.copy(readMeSrc, writer1);
        writer1.close();

        // create the artifact 2 from the test_sheet.ps
        Path psFile = FileSystems.getDefault().getPath(EXAMPLE_PATH, PSFILE_NAME);
        ArtifactInfo entry2 = arch.createArtifact(PSFILE_NAME, "application/postscript", false);
        OutputStream writer2 = arch.writeArtifact(entry2);
        Files.copy(psFile, writer2);
        writer2.close();

        // create the artifact 3 from the readme.txt but put it in a subdirectory
        String readMeTgt2 = "abc/foo/" + readMeSrc.getFileName();
        arch.createArtifact(readMeTgt2, "text/plain", readMeSrc, false);

        // sort the entries in the manifest
        IManifestManager mfm = arch.getManifest();
        mfm.sortByLocation();
        mfm.save();

        arch.close();
        assertEquals(omexPath.toFile(), new File(EXAMPLE_OMEX), IGNORE_FILE_CONTENT);
    }

    private static String getOSDependentExampleFile() {
        final String prefix = "example_files/example1_test/example";
        final String WIN    = "_win";
        final String suffix = ".omex";
        StringBuilder result = new StringBuilder(prefix);
        if (System.getProperty("os.name").startsWith("Windows")) {
            result.append(WIN);
        }
        result.append(suffix);
        return result.toString();
    }
}
