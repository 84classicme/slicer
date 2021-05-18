package org.slicer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SlicerUtils {

    public static final Path ZIP_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "slicer.zip");
    public static final Path GENERATED_SOURCE_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "generated");

    public static String buildAutowired(String name){
        StringBuilder sb = new StringBuilder();
        sb.append("    @Autowired\n");
        sb.append("    ");
        sb.append(name);
        sb.append(" ");
        sb.append(name.substring(0, 1).toLowerCase() + name.substring(1));
        sb.append(";\n\n");
        return sb.toString();
    }

    public static String buildTestClass(String name, String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append(buildPackage(packagename));
        sb.append("import org.junit.jupiter.api.*;\n");
        sb.append("import org.assertj.core.api.Assertions;\n");
        sb.append("\n");
        sb.append("public class ");
        sb.append(name);
        sb.append("Test {\n");
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }

    public static String buildPackage(String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append("package ");
        sb.append(packagename.toLowerCase());
        sb.append(";\n\n");
        return sb.toString();
    }

    public static String buildApplicationClass(String name, String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append(buildPackage(packagename));
        sb.append("import org.springframework.boot.SpringApplication;\n");
        sb.append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n");
        sb.append("\n");
        sb.append("@SpringBootApplication\n");
        sb.append("public class ");
        sb.append(name);
        sb.append("Application {\n");
        sb.append("public static void main(String[] args) { SpringApplication.run(");
        sb.append(name);
        sb.append("Application.class, args); }\n");
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }

    public static void copyFile(Path sourcePath, Path destinationPath){
        File destination = null;
        try {
            File source = new File(sourcePath.toString());
            destination = new File(destinationPath.toString());
            Files.copy(source.toPath(), destination.toPath());
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot copy file. Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void checkDirectory(Path path){
        File tmpDir = new File(path.toString());
        System.out.println("Looking for directory: " + path);
        if (!tmpDir.isDirectory()) {
            System.out.println("Directory not found. Creating.");
            boolean success = tmpDir.mkdirs();
            System.out.println(success);
        }
    }

    public static void deleteFiles() {
        Path path = FileSystems.getDefault().getPath("src", "main", "resources", "generated");
        try {
            Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e){
            System.err.println("EXCEPTION: Cannot delete files. Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeFile(Path path, File file, String content) throws IOException{
        SlicerUtils.checkDirectory(path);
        if(file.exists()) {
            System.out.println("File already created. Skipping.");
            return;
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void zipFiles(){
        try (FileOutputStream fos = new FileOutputStream(ZIP_LOCATION.toString());
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            Files.walkFileTree(GENERATED_SOURCE_LOCATION, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                    if(!GENERATED_SOURCE_LOCATION.equals(dir)){
                        zos.putNextEntry(new ZipEntry(GENERATED_SOURCE_LOCATION.relativize(dir).toString() + "/"));
                        zos.closeEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(GENERATED_SOURCE_LOCATION.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fillTemplate(Path path, String oldString, String newString){
        final String newval = newString != null ? newString : "";
        try {
            Stream<String> lines = Files.lines(path);
            List<String> replaced = lines.map(line -> line.replaceAll(oldString, newval)).collect(Collectors.toList());
            Files.write(path, replaced);
            lines.close();
        } catch (IOException e) {
            System.err.println("EXCEPTION: Cannot fill template for value " + oldString + ". Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
