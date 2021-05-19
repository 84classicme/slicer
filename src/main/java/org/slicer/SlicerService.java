package org.slicer;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
public class SlicerService {

    public void serveSlice(Slice slice) {
        try {
            createSourceFiles(slice);
            createPomFile(slice);
            createYamlFile(slice);
            SlicerIO.zipFiles();
            SlicerIO.deleteFiles();
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot serve slice.  Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createSourceFiles(Slice slice){
        String packagename = slice.getName();
        createApplicationClass(packagename);
        createSwaggerConfigClass(packagename);
        slice.getControllers().forEach(controller -> {
            this.writeSourceClassToFile(slice, controller, controller.getName());
            this.writeTestClassToFile(slice, controller.getName());
            controller.getServices().forEach(service -> {
                this.writeSourceClassToFile(slice, service, service.getName());
                this.writeTestClassToFile(slice, service.getName());
                service.getServices().forEach(autowired -> {
                    this.writeSourceClassToFile(slice, autowired, autowired.getName());
                    this.writeTestClassToFile(slice, autowired.getName());
                });
                service.getRepositories().forEach(repository -> {
                    this.writeSourceClassToFile(slice, repository, repository.getName());
                    this.writeTestClassToFile(slice, repository.getName());
                });
            });
        });
    }

    private void createApplicationClass(String packagename){
        String name = packagename.substring(0, 1).toUpperCase() + packagename.substring(1);
        System.out.println("Writing application class file: " + name + "Application.java");
        try {
            Path path = FileSystems.getDefault().getPath("src", "main", "resources", "generated", "src", "main", "java",  packagename.toLowerCase());
            File file = new File(path.toString() + "/" + name + "Application.java");
            SlicerIO.writeFile(path, file, SlicerCodeGenUtils.buildApplicationClass(name, packagename));
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot write application class file for "+ name + "Application. Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createSwaggerConfigClass(String packagename){
        Path sourcePath = SlicerConstants.SWAGGER_TEMPLATE_LOCATION;
        Path destinationDir = FileSystems.getDefault().getPath("src", "main", "resources", "generated", "src", "main", "java",  packagename.toLowerCase());
        Path destinationPath = FileSystems.getDefault().getPath("src", "main", "resources", "generated","src", "main", "java",  packagename.toLowerCase(), "SwaggerConfig.java");
        SlicerIO.checkDirectory(destinationDir);
        SlicerIO.copyFile(sourcePath, destinationPath);
        addSwaggerConfigValues(destinationPath, packagename);
    }

    private void createPomFile(Slice slice){
        Path sourcePath = SlicerConstants.POM_TEMPLATE_LOCATION;
        Path destinationDir = SlicerConstants.GENERATED_SOURCE_LOCATION;
        Path destinationPath = SlicerConstants.GENERATED_POM_LOCATION;
        SlicerIO.checkDirectory(destinationDir);
        SlicerIO.copyFile(sourcePath, destinationPath);
        addPomValues(destinationPath, slice);
    }

    private void addPomValues(Path destinationPath, Slice slice){
        SlicerIO.fillTemplate(destinationPath, "%%GROUP_ID%%", slice.getGroupId());
        SlicerIO.fillTemplate(destinationPath, "%%ARTIFACT_ID%%", slice.getArtifactId());
        SlicerIO.fillTemplate(destinationPath, "%%DESCRIPTION%%", slice.getDescription());
        if (slice.getType() != null) {
            switch (slice.getType()) {
                case ("spring-web"):
                    SlicerIO.fillTemplate(destinationPath, "%%CORE_APPLICATION_DEPENDENCIES%%", SlicerConstants.SPRING_STARTER_WEB_DEPENDENCY);
                    break;
                case ("spring-webflux"):
                    SlicerIO.fillTemplate(destinationPath, "%%CORE_APPLICATION_DEPENDENCIES%%", SlicerConstants.SPRING_STARTER_WEBFLUX_DEPENDENCY);
                    break;
            }
        }
        if (slice.getDatasource() != null) {
            SlicerIO.fillTemplate(destinationPath, "%%SPRING_DATA_DEPENDENCY_MGMT%%", slice.getDatasource().getDependencyManager());
            SlicerIO.fillTemplate(destinationPath, "%%DATABASE_DRIVER_DEPENDENCY%%", slice.getDatasource().getDependencies());
        }
    }

    private void addSwaggerConfigValues(Path destinationPath, String packagename){
        SlicerIO.fillTemplate(destinationPath, "%%PACKAGE%%", SlicerCodeGenUtils.buildPackage(packagename));
    }

    private void createYamlFile(Slice slice){
        Path sourcePath = SlicerConstants.YAML_TEMPLATE_LOCATION;
        Path destinationDir = SlicerConstants.GENERATED_RESOURCES_LOCATION;
        Path destinationPath = SlicerConstants.GENERATED_YAML_LOCATION;
        SlicerIO.checkDirectory(destinationDir);
        SlicerIO.copyFile(sourcePath, destinationPath);
        addYamlValues(destinationPath, slice);
    }

    private void addYamlValues(Path destinationPath, Slice slice){
        SlicerIO.fillTemplate(destinationPath, "%%DATASOURCE_PORT%%", slice.getDatasource().getPort());
        SlicerIO.fillTemplate(destinationPath, "%%DATASOURCE_HOST%%", slice.getDatasource().getHost());
        SlicerIO.fillTemplate(destinationPath, "%%DATASOURCE_DATABASE_NAME%%", slice.getDatasource().getDatabase());
        SlicerIO.fillTemplate(destinationPath, "%%DATASOURCE_URL%%", slice.getDatasource().getUrl());
        SlicerIO.fillTemplate(destinationPath, "%%DATASOURCE_USERNAME%%", slice.getDatasource().getUsername());
        SlicerIO.fillTemplate(destinationPath, "%%DATASOURCE_PASSWORD%%", slice.getDatasource().getPassword());
        SlicerIO.fillTemplate(destinationPath, "%%APPLICATION_NAME%%", slice.getName());
        SlicerIO.fillTemplate(destinationPath, "%%APPLICATION_DESCRIPTION%%", slice.getDescription());
        String fqdn = slice.getName().toLowerCase() + "." + slice.getControllers().get(0).getName();
        SlicerIO.fillTemplate(destinationPath, "%%APPLICATION_CONTROLLER_FQDN%%", fqdn );
    }

    private void writeTestClassToFile(Slice slice, String classname) {
        String packagename = slice.getName();
        System.out.println("Writing test class file: " + classname + "Test.java");
        try {
            Path path = FileSystems.getDefault().getPath("src", "main", "resources", "generated", "src", "test", "java", packagename.toLowerCase());
            File file = new File(path.toString() + "/" + classname + "Test.java");
            SlicerIO.writeFile(path, file, SlicerCodeGenUtils.buildTestClass(classname, packagename.toLowerCase()));
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot write test class file for "+ classname + "Test. Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeSourceClassToFile(Slice slice, Writeable w, String classname) {
        String packagename = slice.getName();
        System.out.println("Writing file: " + classname + ".java");
        try {
            Path path = FileSystems.getDefault().getPath("src", "main", "resources", "generated", "src", "main", "java", packagename.toLowerCase());
            File file = new File(path.toString() + "/" + classname + ".java");
            SlicerIO.writeFile(path, file, w.toFile(slice));
        } catch (Exception e){
            System.err.println("EXCEPTION: Cannot write class file for "+ classname + ". Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
