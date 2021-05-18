package org.slicer;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // provides @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
@NoArgsConstructor
public class Slice {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JacksonXmlProperty(isAttribute = true)
    private String groupId;

    @JacksonXmlProperty(isAttribute = true)
    private String artifactId;

    @JacksonXmlProperty(localName = "Controllers")
    private List<Controller> controllers;

    @JacksonXmlProperty(localName = "DataSource")
    private DataSource datasource;
}
