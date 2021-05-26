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

    public String toFile(Slice slice){
        StringBuilder sb = new StringBuilder();
        sb.append(SlicerCodeGenUtils.buildPackage(slice.getName()));
        sb.append("\n");
        sb.append(buildImports());
        sb.append("\n");
        sb.append(buildClassDefinition());
        sb.append("\n");
        sb.append(buildMembers());
        sb.append("\n");
        sb.append(buildConstructor());
        sb.append("\n");
        this.endpoints.forEach(e -> {
            sb.append(e.toFile(slice));
            sb.append("\n\n");
        });
        sb.append("}");
        return sb.toString();
    }

    private String buildImports(){
        StringBuilder sb = new StringBuilder();
        sb.append("import io.swagger.annotations.*;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n");
        sb.append("import org.springframework.http.*;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        return sb.toString();
    }

    private String buildClassDefinition(){
        StringBuilder sb = new StringBuilder();
        sb.append("@RestController\n");
        sb.append("public class ");
        sb.append(this.name);
        sb.append(" {\n");
        return sb.toString();
    }

    private  String buildMembers(){
        StringBuilder sb = new StringBuilder();
        this.services.forEach(s -> {
            sb.append("    ");
            sb.append(SlicerCodeGenUtils.buildMember(s.getName()));
        });
        return sb.toString();
    }

    private  String buildConstructor(){
        StringBuilder sb = new StringBuilder();
        sb.append("    @Autowired\n");
        sb.append("    public ");
        sb.append(this.name);
        sb.append(" (");
        this.services.forEach(s -> sb.append(SlicerCodeGenUtils.buildConstructorParam(s.getName())));
        if(sb.lastIndexOf(", ") == sb.length() - 2) {
            sb.replace(sb.lastIndexOf(", "), sb.length() - 1, "");
        }
        sb.append("    )}\n");
        this.services.forEach(s -> sb.append(SlicerCodeGenUtils.buildConstructorAssignment(s.getName())));
        sb.append("    }\n");
        return sb.toString();
    }
}
