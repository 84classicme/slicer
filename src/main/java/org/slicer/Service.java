package org.slicer;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // provides @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
@NoArgsConstructor
public class Service implements Writeable {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "Services")
    private List<Service> services;

    @JacksonXmlProperty(localName = "Repositories")
    private List<Repository> repositories;

    public String toFile(Slice slice){
        StringBuilder sb = new StringBuilder();
        sb.append(SlicerCodeGenUtils.buildPackage(slice.getName()));
        sb.append("\n");
        sb.append(buildImports());
        sb.append("\n");
        sb.append(buildClassDeclaration());
        sb.append("\n");
        if(services != null || repositories != null) {
            sb.append(buildMembers());
            sb.append("\n");
            sb.append(buildConstructor());
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    private String buildClassDeclaration(){
        StringBuilder sb = new StringBuilder();
        sb.append("@Service\n");
        sb.append("public class ");
        sb.append(name);
        sb.append(" {\n");
        return sb.toString();
    }

    private String buildImports(){
        StringBuilder sb = new StringBuilder();
        sb.append("import org.springframework.stereotype.Service;\n");
        if(services != null || repositories != null){
            sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        }
        return sb.toString();
    }

    private  String buildMembers(){
        StringBuilder sb = new StringBuilder();
        if(services != null){
            this.services.forEach(s -> {
                sb.append("    ");
                sb.append(SlicerCodeGenUtils.buildMember(s.getName()));
            });
        }
        if(repositories != null) {
            this.repositories.forEach(s -> {
                sb.append("    ");
                sb.append(SlicerCodeGenUtils.buildMember(s.getName()));
            });
        }
        return sb.toString();
    }

    private  String buildConstructor(){
        StringBuilder sb = new StringBuilder();
        sb.append("    @Autowired\n");
        sb.append("    public ");
        sb.append(this.name);
        sb.append(" (");
        if(services != null) {
            this.services.forEach(s -> sb.append(SlicerCodeGenUtils.buildConstructorParam(s.getName())));
        }
        if(repositories != null) {
            this.repositories.forEach(r -> sb.append(SlicerCodeGenUtils.buildConstructorParam(r.getName())));
        }
        if(sb.lastIndexOf(", ") == sb.length() - 2) {
            sb.replace(sb.lastIndexOf(", "), sb.length() - 1, "){\n");
        }
        if(services != null) {
            this.services.forEach(s -> sb.append(SlicerCodeGenUtils.buildConstructorAssignment(s.getName())));
        }
        if(repositories != null) {
            this.repositories.forEach(r -> sb.append(SlicerCodeGenUtils.buildConstructorAssignment(r.getName())));
        }
        sb.append("    }\n");
        return sb.toString();
    }
}
