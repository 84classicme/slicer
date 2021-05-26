package org.slicer;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // provides @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
@NoArgsConstructor
public class Repository implements Writeable {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    public String toFile(Slice slice){
        boolean reactive = slice.getDatasource() != null &&
            slice.getDatasource().getType() != null &&
            slice.getDatasource().getType().contains("reactive");

        StringBuilder sb = new StringBuilder();
        sb.append(SlicerCodeGenUtils.buildPackage(slice.getName()));
        sb.append("\n");
        sb.append(buildImports(reactive));
        sb.append("\n");
        sb.append(buildClassDeclaration(reactive));
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String buildImports(boolean reactive){
        StringBuilder sb = new StringBuilder();
        sb.append("import org.springframework.stereotype.Repository;\n");
        if(reactive) {
            sb.append("import org.springframework.data.repository.reactive.ReactiveCrudRepository;\n");
        } else {
            sb.append("import org.springframework.data.repository.CrudRepository;\n");
        }
        return sb.toString();
    }

    private String buildClassDeclaration(boolean reactive){
        StringBuilder sb = new StringBuilder();
        sb.append("@Repository\n");
        sb.append("public interface ");
        sb.append(name);
        if(reactive) {
            sb.append(" extends ReactiveCrudRepository<String, String> {\n");
        } else {
            sb.append(" extends CrudRepository<String, String> {\n");
        }
        return sb.toString();
    }
}
