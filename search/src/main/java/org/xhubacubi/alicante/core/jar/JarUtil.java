/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.xhubacubi.alicante.core.jar;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 *
 * @author rugi
 */
public final class JarUtil {

    private StringBuilder sourceFile;
    private Manifest manifest;
    private JarFile jarFile;
    private long lastModif;
    private int size;
    private boolean valid;
    private StringBuilder lastError;

    public JarUtil(){
        this("");
    }
    public JarUtil(String source) {
        super();
        sourceFile = new StringBuilder();
        lastError = new StringBuilder();
        setSourceFile(source);
    }

    /**
     * Establece el target desde el cual se recupera el contenido del jar.
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
        return this.manifest==null?new Manifest():this.manifest;
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
        this.valid=  false;
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
            jarFile = null;
            jarFile = jarConnection.getJarFile();                
            this.lastModif = jarConnection.getLastModified();
            this.size=jarConnection.getContentLength();                     
            //la conexion ya no es necesaria
            this.valid= true;            
            jarConnection = null;
        } catch (IOException ex) {            
            this.valid= false;
            this.lastError.append(ex.toString());
        }
    }

    /**
     * Devuelve un listado de los JarEntrys que contienen    
     * clases.
     * @return
     */
    public List<String> getClassInside() {

        List<String> s = new ArrayList<String>();        
        Enumeration<JarEntry> enum1 = jarFile.entries();
        while (enum1.hasMoreElements()) {
            JarEntry temp = enum1.nextElement();
            if (temp.getName().endsWith(".class")) {
                s.add(temp.getName());
            }
        }

        return s;
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
    public void finalize() {
        this.jarFile = null;
        this.sourceFile = null;
        this.jarFile =null;
    }
}
