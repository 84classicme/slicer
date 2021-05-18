package org.slicer;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class SlicerService {

    public void serveSlice(Slice slice) {
        try {
            createSourceFiles(slice);
            createPomFile();
            createYamlFile();
            zipFiles();
            // TODO: clean up the source files
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot serve slice.  Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createSourceFiles(Slice slice){
        String packagename = slice.getName();
        createApplicationClass(packagename);
        slice.getControllers().forEach(controller -> {
            this.writeSourceClassToFile(packagename, controller, controller.getName());
            this.writeTestClassToFile(packagename, controller.getName());
            controller.getServices().forEach(service -> {
                this.writeSourceClassToFile(packagename, service, service.getName());
                this.writeTestClassToFile(packagename, service.getName());
                service.getServices().forEach(autowired -> {
                    this.writeSourceClassToFile(packagename, autowired, autowired.getName());
                    this.writeTestClassToFile(packagename, autowired.getName());
                });
                service.getRepositories().forEach(repository -> {
                    this.writeSourceClassToFile(packagename, repository, repository.getName());
                });
            });
        });
    }

    private void createApplicationClass(String packagename){
        String name = packagename.substring(0, 1).toUpperCase() + packagename.substring(1);
        System.out.println("Writing application class file: " + name + "Application.java");
        try {
            Path path = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "generated", "src", "main", "java",  packagename.toLowerCase());
            File file = new File(path.toString() + "/" + name + "Application.java");
            writeFile(path, file, SlicerUtils.buildApplicationClass(name, packagename));
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot write application class file for "+ name + "Application. Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createPomFile(){
        Path sourcePath = FileSystems.getDefault().getPath("src", "main", "resources", "templates", "pom.xml.template");
        Path destinationPath = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "generated", "pom.xml");
        copyFile(sourcePath, destinationPath);
    }

    private void createYamlFile(){
        Path sourcePath = FileSystems.getDefault().getPath("src", "main", "resources", "templates", "application.yaml.template");
        Path destinationPath = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "generated", "src", "main", "resources", "application.yaml");
        copyFile(sourcePath, destinationPath);
    }

    private void writeTestClassToFile(String packagename, String classname) {
        System.out.println("Writing test class file: " + classname + "Test.java");
        try {
            Path path = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "generated", "src", "test", "java", packagename.toLowerCase());
            File file = new File(path.toString() + "/" + classname + "Test.java");
            writeFile(path, file, SlicerUtils.buildTestClass(classname, packagename.toLowerCase()));
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot write test class file for "+ classname + "Test. Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeSourceClassToFile(String packagename, Writeable w, String classname) {
        System.out.println("Writing file: " + classname + ".java");
        try {
            Path path = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "generated", "src", "main", "java", packagename.toLowerCase());
            File file = new File(path.toString() + "/" + classname + ".java");
            writeFile(path, file, w.toImpl(packagename));
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot write class file for "+ classname + ". Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeFile(Path path, File file, String content) throws IOException{
        checkDirectory(path);
        if(file.exists()) {
            System.out.println("File already created. Skipping.");
            return;
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
    }

    public void zipFiles(){
        Path destinationPath = SlicerUtils.ZIP_LOCATION;
        Path mysourcePath = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "generated");
        try (FileOutputStream fos = new FileOutputStream(destinationPath.toString());
                    ZipOutputStream zos = new ZipOutputStream(fos)) {
            Path sourcePath = Paths.get(mysourcePath.toString());
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                    if(!sourcePath.equals(dir)){
                        zos.putNextEntry(new ZipEntry(sourcePath.relativize(dir).toString() + "/"));
                        zos.closeEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(sourcePath.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // TODO handle exception
            e.printStackTrace();
        }
    }

    private File copyFile(Path sourcePath, Path destinationPath){
        File destination = null;
        try {
            File source = new File(sourcePath.toString());
            destination = new File(destinationPath.toString());
            Files.copy(source.toPath(), destination.toPath());
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot copy pom.xml. Reason: " + e.getMessage());
            e.printStackTrace();
        }
        return destination;
    }

    private void checkDirectory(Path path) throws IOException{
        File tmpDir = new File(path.toString());
        System.out.println("Looking for directory: " + path);
        if (!tmpDir.isDirectory()) {
            System.out.println("Directory not found. Creating.");
            Files.createDirectory(path).toFile();
        }
    }
}
