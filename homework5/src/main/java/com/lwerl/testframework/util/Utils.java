package com.lwerl.testframework.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static com.lwerl.testframework.constant.Literals.*;
import static com.lwerl.testframework.constant.Messages.CLASS_NOT_LOAD_ERROR;

public class Utils {
    private Utils() {
    }

    public static List<Class<?>> getClassesInPackage(String packageName) throws URISyntaxException, IOException {

        List<Path> classpaths = new ArrayList<>();
        List<Class<?>> classes = new ArrayList<>();
        String[] packages = packageName.split(PACKAGE_SEPARATOR_REGEXP);

        URLClassLoader system = (URLClassLoader) ClassLoader.getSystemClassLoader();

        for (URL url : system.getURLs()) {
            if (!url.toString().endsWith(JAR_FILE_EXTENSION)) {
                classpaths.add(Paths.get(url.toURI()));
            }
        }

        for (Path classpath : classpaths) {
            Path dir = Paths.get(classpath.toString(), packages);
            ClassFileVisitor classFileVisitor = new ClassFileVisitor(classpath, classes);
            Files.walkFileTree(dir, classFileVisitor);
        }

        return classes;
    }

    static public class ClassFileVisitor extends SimpleFileVisitor<Path> {
        private Path classpath;
        private List<Class<?>> classes;

        public ClassFileVisitor(Path classpath, List<Class<?>> classes) {
            this.classpath = classpath;
            this.classes = classes;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (attrs.isRegularFile()) {
                String className = classpath.relativize(file)
                        .toString()
                        .replace(FileSystems.getDefault().getSeparator(), PACKAGE_SEPARATOR);

                if (className.endsWith(CLASS_FILE_EXTENSION)) {
                    try {
                        classes.add(Class.forName(className.replace(CLASS_FILE_EXTENSION, EMPTY_STRING)));
                    } catch (ClassNotFoundException e) {
                        System.out.printf(CLASS_NOT_LOAD_ERROR, className.replace(CLASS_FILE_EXTENSION, EMPTY_STRING));
                    }
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }

}
