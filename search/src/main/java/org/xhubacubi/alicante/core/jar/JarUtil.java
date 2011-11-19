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

    public JarUtil(String source) {
        super();
        sourceFile = new StringBuilder();
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
        return this.manifest;
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
        JarURLConnection jarConnection;
        this.sourceFile.delete(0, this.sourceFile.length());
        this.sourceFile.append(target(sourceFile));
        try {
            URL url = new URL("jar:" + this.sourceFile.toString() + "!/");
            jarConnection = (JarURLConnection) url.openConnection();
            //asignamos el manifest y el jarFile unicamente.
            manifest = jarConnection.getManifest();
            jarFile = jarConnection.getJarFile();
            //la conexion ya no es necesari
            jarConnection = null;
        } catch (IOException ex) {
            throw new RuntimeException("Origen desconocido.No se puede procesar. " + ex);
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
