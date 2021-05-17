package slicer;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class SlicerService {

    public void serveSlice(Slice slice) throws IOException {
        slice.getControllers().forEach(controller -> {
            this.implToFile(controller, controller.getName());
            controller.getServices().forEach(service -> {
                this.implToFile(service, service.getName());
                service.getRepositories().forEach(repository -> {
                    this.implToFile(repository, repository.getName());
                });
            });
        });
    }

    private void implToFile(Generatable g, String name) {
        try (FileWriter fileWriter = new FileWriter("src/main/java/slicer/generated/" + name + ".java")) {
            fileWriter.write(g.toImpl());
        } catch (Exception e){
            System.err.println("EXCEPTION: " + e.getMessage());
        }

    }
}
