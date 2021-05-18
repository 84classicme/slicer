package org.slicer;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // provides @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
@NoArgsConstructor
public class Controller implements Writeable {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "Services")
    private List<Service> services;

    @JacksonXmlProperty(localName = "Endpoints")
    private List<Endpoint> endpoints;

    public String toFile(String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append(SlicerUtils.buildPackage(packagename));
        sb.append("import io.swagger.annotations.*;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n");
        sb.append("import org.springframework.http.*;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("\n");
        sb.append(buildController(this.name));
        this.services.forEach(s -> sb.append(SlicerUtils.buildAutowired(s.getName())));
        sb.append("\n");
        this.endpoints.forEach(e -> {
            sb.append(e.toFile(packagename));
            sb.append("\n\n");
        });
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }

    private static String buildController(String name){
        StringBuilder sb = new StringBuilder();
        sb.append("@RestController\n");
        sb.append("public class ");
        sb.append(name);
        sb.append(" {\n");
        sb.append("\n");
        return sb.toString();
    }
}
