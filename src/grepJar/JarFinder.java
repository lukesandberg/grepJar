package grepJar;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A simple class that you can iterate to find
 * all the jar files in sub-directories.
 * @author Luke Sandberg
 * Oct 28, 2010
 */
public class JarFinder implements Iterable<File> {
    private final File directory;

    /**
     * The Constructor takes the base directory to search
     * from. 
     * 
     * @param base the base directory to search
     */
    public JarFinder(File base) {
        assert base.isDirectory() : "you must provide a directory";
        directory = base;
    }

    @Override
    public Iterator<File> iterator() {
        return new JarIterator();
    }

    private class JarIterator implements Iterator<File> {
        private final Queue<File> directories = new LinkedList<File>();
        private final Queue<File> files = new LinkedList<File>();
        private File next = null;

        public JarIterator() {
            directories.add(directory);
            findNext();
        }

        private File getNextFile() {
            while (files.isEmpty()) {
                if (directories.isEmpty())
                    return null;
                File dir = directories.remove();
                File[] children = dir.listFiles();
                if (children != null) {
                    for (File f : dir.listFiles()) {
                        if (f.isDirectory())
                            directories.add(f);
                        else
                            files.add(f);
                    }
                }
            }
            return files.remove();
        }

        private void findNext() {
            next = null;
            File tmp;
            while ((tmp = getNextFile()) != null) {
                String n = tmp.getName();
                if (n.toLowerCase().endsWith("jar")) {
                    next = tmp;
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public File next() {
            if (next == null) {
                throw new UnsupportedOperationException();
            }
            File r = next;
            findNext();
            return r;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
