package grepJar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility class to search for specific files within a jar
 * using a regex.
 * @author Luke Sandberg
 * Oct 28, 2010
 */
final class JarSearcher {

    private JarSearcher() {
        //prevent construction
    }

    /**
     * The search method looks through the jar file contents
     * and attempts to find files matching the filename. if the
     * jar contains other jar files those are searched recursively.
     * 
     * The search function returns when it finds the first match. 
     *  
     * @param jar the jar to search
     * @param re the regular expression to use to match filenames
     * @return the full name of the matched resource
     */
    public static String search(File jar, Pattern re) {
        InputStream is = null;
        try
        {
            is = new FileInputStream(jar);
            return search_helper(re, is);
        }
        catch(IOException e)
        {
            return null;
        }
        finally
        {
            if(is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    
    private static String search_helper(Pattern re, InputStream is) throws IOException {
        ZipInputStream zin = new ZipInputStream(is);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (re.matcher(ze.getName()).find()) {
                return ze.getName();
            }
            if(ze.getName().toLowerCase().endsWith(".jar")) {
                String recVal = search_helper(re, zin);
                if(recVal != null) {
                    return ze.getName() +"->" + recVal;
                }
            }
        }
        return null;
    }
}
