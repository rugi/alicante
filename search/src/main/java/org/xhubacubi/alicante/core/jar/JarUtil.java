/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.xhubacubi.alicante.core.jar;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 *
 * @author rugi
 */
public final class JarUtil {

    private StringBuilder sourceFile;
    private Manifest manifest;
    private long lastModif;
    private int size;
    private boolean valid;
    private StringBuilder lastError;
    private List<String> content;

    public JarUtil() {
        this("");
    }

    public JarUtil(String source) {
        super();
        sourceFile = new StringBuilder();
        lastError = new StringBuilder();
        this.valid = false;
        content = new ArrayList<String>();
        setSourceFile(source);
    }

    public Map<String, String> getManifestContentInMap() {
        Map resm = new HashMap<String, String>();

        if (this.manifest != null) {
            StringBuilder row0 = new StringBuilder();
            Map map = this.manifest.getEntries();
            Iterator it = map.keySet().iterator();
            StringBuilder res = new StringBuilder();
            StringBuilder res2 = new StringBuilder();
            while (it.hasNext()) {
                res.delete(0, res.length());
                res.append(it.next());
                Attributes at = (Attributes) map.get(res.toString());
                Iterator llavesAt = at.keySet().iterator();
                while (llavesAt.hasNext()) {
                    res2.delete(0, res2.length());
                    res2.append(llavesAt.next());
                    row0.delete(0, row0.length());
                    row0.append("[").append(res.toString().toUpperCase()).
                            append("]: ").append(res2.toString());
                    resm.put(row0.toString(), at.getValue(res2.toString()));
                }//while next

            }//while              
        }
        return resm;
    }

    /**
     * Establece el target desde el cual se recupera el contenido del jar.
     *
     * @param s
     * @return
     */
    private String target(String s) {
        StringBuilder res = new StringBuilder();
        if (new File(s).isFile()) {
            res.append("file:");
            res.append(s);
        } else {
            res.append(s);
        }
        return res.toString();
    }

    public Manifest getManifest() throws IOException {
        return this.manifest == null ? new Manifest() : this.manifest;
    }

    /**
     * @return the sourceFile
     */
    public String getSourceFile() {
        return sourceFile.toString();
    }

    /**
     * @param sourceFile the sourceFile to set
     */
    public void setSourceFile(String sourceFile) {
        //TODO REvisar bien la optimizacion de este metodo.
        this.valid = false;
        JarURLConnection jarConnection;
        this.sourceFile.delete(0, this.sourceFile.length());
        this.sourceFile.append(target(sourceFile));
        this.lastError.delete(0, this.getLastError().length());
        try {
            URL url = new URL("jar:" + this.sourceFile.toString() + "!/");
            jarConnection = (JarURLConnection) url.openConnection();
            //asignamos el manifest y el jarFile unicamente.
            manifest = null;
            manifest = jarConnection.getManifest();
            this.content.clear();
            this.content.addAll(toList(jarConnection.getJarFile().entries()));
            this.lastModif = jarConnection.getLastModified();
            this.size = jarConnection.getContentLength();
            //la conexion ya no es necesaria
            this.valid = true;
            jarConnection = null;
        } catch (IOException ex) {
            this.valid = false;
            this.lastError.append(ex.toString());
        }

    }

    public void clear() {
        this.valid = false;
        this.manifest = null;
        this.content.clear();
        this.sourceFile.delete(0, this.sourceFile.length());
        this.lastError.delete(0, this.getLastError().length());
        this.lastModif = -1;
        this.size = -1;
    }

    private List<String> toList(Enumeration e) {
        List<String> l = new ArrayList<String>();
        while (e.hasMoreElements()) {
            l.add(e.nextElement().toString());
        }
        return l;
    }

    /**
     * Devuelve un listado de los JarEntrys que contienen clases.
     *
     * @return
     */
    public List<String> getClassInside() {
        return this.content;
    }

    /**
     * @return the lastModif
     */
    public long getLastModif() {
        return lastModif;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @return the lastError
     */
    public String getLastError() {
        return lastError.toString();
    }

    static class Decorator {

        public static String cleanExtencion(String s) {
            return s.replaceAll(".class", "");

        }
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        sourceFile.delete(0, sourceFile.length());
        sourceFile = null;
        manifest = null;
        lastError.delete(0, lastError.length());
        lastError = null;
        content.clear();
        content = null;
        System.out.println("<<<finalize>>>");
    }
}
