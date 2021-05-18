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

    public String toImpl(String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append(SlicerUtils.buildPackage(packagename));
        sb.append("import org.springframework.stereotype.Service;\n");
        if(services != null || repositories != null){
            sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        }
        sb.append("\n");
        sb.append("@Service\n");
        sb.append("public class ");
        sb.append(name);
        sb.append(" {\n");
        sb.append("\n");
        if(services != null) this.services.forEach(s -> sb.append(SlicerUtils.buildAutowired(s.getName())));
        if(repositories != null) this.repositories.forEach(r -> sb.append(SlicerUtils.buildAutowired(r.getName())));
        sb.append("}");
        return sb.toString();
    }
}