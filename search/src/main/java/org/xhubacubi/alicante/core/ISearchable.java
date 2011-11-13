/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.xhubacubi.alicante.core;

import java.io.IOException;
import java.util.List;

/**
 * Interface principal.
 * @author rugi
 */
public interface ISearchable {
    /**
     * Devuelve una lista de paths (incluyendo el nombre del archivo).
     * Busca a partir de <code>folder</code> de manera recursiva
     * archivos cuyo nombre debe cumplir con el criterio de <code>pattern</code>
     * @param folder
     * @param pattern
     * @return 
     */
    public  List<String> getPathFilesInFolder(String folder, String pattern) throws IOException;
}
