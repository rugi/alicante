/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.xhubacubi.alicante.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rugi
 */
public class SearchFilesUtil implements ISearchable {

    public SearchFilesUtil() {
        super();
    }

    @Override
    public List<String> getPathFilesInFolder(String folder, String pattern) throws IOException {
        List<String> res = new ArrayList<String>();
        if (new File(folder).isDirectory()) {
            searchInFolder(new File(folder), pattern, res);
        } else {
            throw new IOException("El origen proporcionado no es v‡lido.");
        }
        return res;
    }

    private void searchInFolder(File folder, String pattern, List<String> r) {
        File[] fs = folder.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                searchInFolder(f, pattern, r);
            } else {
                if (f.getName().matches(pattern)) {
                    r.add(f.getAbsolutePath());                    
                }
            }
        }
    }
}
