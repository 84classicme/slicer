package slicer;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // provides @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
@NoArgsConstructor
public class Service implements Generatable {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "Repositories")
    private List<Repository> repositories;

    public String toImpl(){
        StringBuilder sb = new StringBuilder();
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("\n");
        sb.append("@Service\n");
        sb.append("public class ");
        sb.append(name);
        sb.append(" {\n");
        sb.append("\n");
        repositories.forEach(r -> {
            sb.append("    ");
            sb.append("@Autowired\n");
            sb.append("    ");
            sb.append(r.getClass());
            sb.append(" ");
            sb.append(r.getName());
            sb.append("\n\n");
        });
        sb.append("}");
        return sb.toString();
    }
}
