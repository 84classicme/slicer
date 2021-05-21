package org.slicer;

import javax.xml.crypto.Data;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class SlicerConstants {
    public static final Path GENERATED_RESOURCES_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "generated", "src", "main", "resources");
    public static final Path SWAGGER_TEMPLATE_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "templates", "SwaggerConfig.java.template");
    public static final Path POM_TEMPLATE_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "templates", "pom.xml.template");
    public static final Path YAML_TEMPLATE_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "templates", "application.yaml.template");
    public static final Path GENERATED_YAML_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "generated", "src", "main", "resources", "application.yaml");
    public static final Path GENERATED_POM_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "generated", "pom.xml");
    public static final Path ZIP_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "slicer.zip");
    public static final Path GENERATED_SOURCE_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "generated");
    public static final String SPRING_STARTER_WEB_DEPENDENCY;
    public static final String SPRING_STARTER_WEBFLUX_DEPENDENCY;
    public static final String SPRING_STARTER_TEST_DEPENDENCY;
    public static final String SPRING_BUILD_CONFIGURATION;
    public static final String DATASOURCE_YAML_TEMPLATE;

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("        <dependency>\n");
        sb.append("            <groupId>org.springframework.boot</groupId>\n");
        sb.append("            <artifactId>spring-boot-starter-test</artifactId>\n");
        sb.append("            <scope>test</scope>\n");
        sb.append("             <exclusions>\n");
        sb.append("                 <exclusion>\n");
        sb.append("                    <groupId>org.junit.vintage</groupId>\n");
        sb.append("                    <artifactId>junit-vintage-engine</artifactId>\n");
        sb.append("                 </exclusion>\n");
        sb.append("            </exclusions>\n");
        sb.append("        </dependency>\n");
        SPRING_STARTER_TEST_DEPENDENCY = sb.toString();

        sb.delete(0, sb.length());
        sb.append("<dependency>\n");
        sb.append("            <groupId>org.springframework.boot</groupId>\n");
        sb.append("            <artifactId>spring-boot-starter-web</artifactId>\n");
        sb.append("        </dependency>\n");
        sb.append(SlicerConstants.SPRING_STARTER_TEST_DEPENDENCY);
        SPRING_STARTER_WEB_DEPENDENCY = sb.toString();

        sb.delete(0, sb.length());
        sb.append("<dependency>\n");
        sb.append("            <groupId>org.springframework.boot</groupId>\n");
        sb.append("            <artifactId>spring-boot-starter-webflux</artifactId>\n");
        sb.append("        </dependency>\n");
        sb.append("        <dependency>\n");
        sb.append("            <groupId>io.projectreactor</groupId>\n");
        sb.append("            <artifactId>reactor-test</artifactId>\n");
        sb.append("            <scope>test</scope>\n");
        sb.append("        </dependency>\n");
        sb.append(SlicerConstants.SPRING_STARTER_TEST_DEPENDENCY);
        SPRING_STARTER_WEBFLUX_DEPENDENCY = sb.toString();

        sb.delete(0, sb.length());
        sb.append("<build>\n");
        sb.append("        <plugins>\n");
        sb.append("            <plugin>\n");
        sb.append("                <groupId>org.springframework.boot</groupId>\n");
        sb.append("                <artifactId>spring-boot-maven-plugin</artifactId>\n");
        sb.append("            </plugin>\n");
        sb.append("        </plugins>\n");
        sb.append("    </build>\n");
        SPRING_BUILD_CONFIGURATION = sb.toString();

        sb.delete(0, sb.length());
        sb.append("datasource:\n");
        sb.append("    port: %%DATASOURCE_PORT%%\n");
        sb.append("    host: %%DATASOURCE_HOST%%\n");
        sb.append("    database: %%DATASOURCE_DATABASE_NAME%%\n");
        sb.append("    url: %%DATASOURCE_URL%%\n");
        sb.append("    username: %%DATASOURCE_USERNAME%%\n");
        sb.append("    password: %%DATASOURCE_PASSWORD%%\n");
        DATASOURCE_YAML_TEMPLATE = sb.toString();
    }
}
