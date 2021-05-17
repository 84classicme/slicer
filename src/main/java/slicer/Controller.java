package slicer;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // provides @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
@NoArgsConstructor
public class Controller implements Generatable {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "Services")
    private List<Service> services;

    @JacksonXmlProperty(localName = "Endpoints")
    private List<Endpoint> endpoints;

    public String toImpl(){
        StringBuilder sb = new StringBuilder();
        sb.append("import org.springframework.web.bind.annotation.*\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired\n");
        sb.append("\n");
        sb.append(buildServiceClass(this.name));
        this.services.forEach(s -> sb.append(buildAutowiredService(s)));
        sb.append("\n");
        this.endpoints.forEach(e -> {
            sb.append(e.toImpl());
            sb.append("\n\n");
        });
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }

    private static String buildServiceClass(String name){
        StringBuilder sb = new StringBuilder();
        sb.append("@RestController\n");
        sb.append("public class ");
        sb.append(name);
        sb.append(" {\n");
        sb.append("\n");
        return sb.toString();
    }

    private static String buildAutowiredService(Service s){
        StringBuilder sb = new StringBuilder();
        sb.append("    @Autowired\n");
        sb.append("    ");
        sb.append(s.getClass());
        sb.append(" ");
        sb.append(s.getName());
        sb.append("\n\n");
        return sb.toString();
    }
}
