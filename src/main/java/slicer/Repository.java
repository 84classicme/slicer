package slicer;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // provides @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
@NoArgsConstructor
public class Repository implements Generatable {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    public String toImpl(){
        StringBuilder sb = new StringBuilder();
        sb.append("package slicer.generated;\n\n");
        sb.append("import org.springframework.stereotype.Repository;\n");
        sb.append("import org.springframework.data.r2dbc.repository.R2dbcRepository;\n\n");
        sb.append("@Repository\n");
        sb.append("public interface ");
        sb.append(name);
        sb.append(" extends R2dbcRepository<String, String> {\n\n");
        sb.append("}");
        return sb.toString();
    }
}
