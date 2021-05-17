package slicer;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Service
public class SlicerService {

    public void serveSlice(Slice slice) throws IOException {
        ArrayList<File> files = createSourceFiles(slice);
        Path sourcePath = FileSystems.getDefault().getPath("src", "main", "resources", "templates", "pom.xml.template");
        Path destinationPath = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "generated", "pom.xml");
        files.add(copyFile(sourcePath, destinationPath));
        sourcePath = FileSystems.getDefault().getPath("src", "main", "resources", "templates", "application.yaml.template");
        destinationPath = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "generated", "src", "main", "resources","application.yaml");
        files.add(copyFile(sourcePath, destinationPath));
        System.out.println("Number of files created: " + files.size());
    }

    private ArrayList<File> createSourceFiles(Slice slice){
        ArrayList<File> files = new ArrayList<>();
        String packagename = slice.getName();
        slice.getControllers().forEach(controller -> {
            this.implToFile(packagename, controller, controller.getName(), files);
            controller.getServices().forEach(service -> {
                this.implToFile(packagename, service, service.getName(), files);
                service.getServices().forEach(autowired -> {
                    this.implToFile(packagename, autowired, autowired.getName(), files);
                });
                service.getRepositories().forEach(repository -> {
                    this.implToFile(packagename, repository, repository.getName(), files);
                });
            });
        });
        return files;
    }

    private ArrayList<File> implToFile(String packagename, Writeable w, String classname, ArrayList<File> files) {
        System.out.println("Writing file: " + classname + ".java");
        File implToWrite = null;
        try {
            Path path = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "generated", "src", "main", "java", packagename.toLowerCase());
            checkDirectory(path);
            implToWrite = new File(path.toString() + "/" + classname + ".java");
            if(implToWrite.exists()) {
                System.out.println("File already created. Skipping.");
                return files;
            }
            FileWriter fileWriter = new FileWriter(implToWrite);
            fileWriter.write(w.toImpl());
            fileWriter.flush();
            fileWriter.close();
            files.add(implToWrite);
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot write class file for "+ classname + ". Reason: " + e.getMessage());
            e.printStackTrace();
        }
        return files;
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
