/**
 * Copyright 2013 EMBL - European Bioinformatics Institute
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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Stuart Moodie
 */
public interface IManifestManager {

    void load();

    void addEntry(String path, Map<String, String> data);

    void removeEntry(String path);

    boolean hasEntry(String path);

    String getFileType(String path);

    /**
     * Checks an entry marked as master or not. The entry is given via the path parameter.
     *
     * @param path A String indicating the path/location of the entry in the manifest file.
     * @return true or false depending on the input
     */
    boolean isMasterFile(String path);

    Iterator<String> filePathIterator();

    void save();

    int numEntries();

    /**
     * Sorts the entries in the manifest in lexicographical order by the locations. The sorting strategy is:
     * <ul>
     * <li>The dot (.) being the root is placed firstly.
     * <li>The ./manifest.xml is the second order.
     * <li>The ./metadata.rdf is the third place.
     * <li>The lasting entries are sorted in alphabetical order.
     * </ul>
     * This method will let clients call after adding entries into a specific manifest manager instance and before
     * calling the {@link org.mbine.co.archive.ManifestManager#save()} method to write the content out the file.
     */
    void sortByLocation();

    void print() throws IOException;
}
