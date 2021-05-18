package org.slicer;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // provides @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
@NoArgsConstructor
public class DataSource  {
    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(localName = "Port")
    private String port;

    @JacksonXmlProperty(localName = "Host")
    private String host;

    @JacksonXmlProperty(localName = "Database")
    private String database;

    @JacksonXmlProperty(localName = "Url")
    private String url;

    @JacksonXmlProperty(localName = "Username")
    private String username;

    @JacksonXmlProperty(localName = "Password")
    private String password;

    private static final String SPRING_DATA_DEPENDENCY_MGMT;
    private static final String SPRING_DATA_DEPENDENCY;
    private static final String H2_DRIVER_DEPENDENCY;
    private static final String MARIADB_DRIVER_DEPENDENCY;
    private static final String MSSQL_DRIVER_DEPENDENCY;
    private static final String POSTGRES_DRIVER_DEPENDENCY;
    private static final String ORACLE_DRIVER_DEPENDENCY;

    static{
        StringBuffer sb = new StringBuffer();
        sb.append("<dependency>\n");
        sb.append("            <groupId>org.springframework.data</groupId>\n");
        sb.append("            <artifactId>spring-data-r2dbc</artifactId>\n");
        sb.append("        </dependency>\n");
        SPRING_DATA_DEPENDENCY = sb.toString();

        sb.delete(0, sb.length());
        sb.append("        <dependency>\n");
        sb.append("            <groupId>io.r2dbc</groupId>\n");
        sb.append("            <artifactId>r2dbc-h2</artifactId>\n");
        sb.append("            <version>0.8.4.RELEASE</version>\n");
        sb.append("        </dependency>\n");
        H2_DRIVER_DEPENDENCY = sb.toString();

        sb.delete(0, sb.length());
        sb.append("        <dependency>\n");
        sb.append("            <groupId>org.mariadb</groupId>\n");
        sb.append("            <artifactId>r2dbc-mariadb</artifactId>\n");
        sb.append("            <version>1.0.0</version>\n");
        sb.append("        </dependency>\n");
        MARIADB_DRIVER_DEPENDENCY = sb.toString();

        sb.delete(0, sb.length());
        sb.append("        <dependency>\n");
        sb.append("            <groupId>io.r2dbc</groupId>\n");
        sb.append("            <artifactId>r2dbc-mssql</artifactId>\n");
        sb.append("            <version>0.9.0.M1</version>\n");
        sb.append("        </dependency>\n");
        MSSQL_DRIVER_DEPENDENCY = sb.toString();

        sb.delete(0, sb.length());
        sb.append("        <dependency>\n");
        sb.append("            <groupId>io.r2dbc</groupId>\n");
        sb.append("            <artifactId>r2dbc-postgresql</artifactId>\n");
        sb.append("            <version>0.8.8.RELEASE</version>\n");
        sb.append("        </dependency>\n");
        POSTGRES_DRIVER_DEPENDENCY = sb.toString();

        sb.delete(0, sb.length());
        sb.append("        <dependency>\n");
        sb.append("            <groupId>com.oracle.database.r2dbc</groupId>\n");
        sb.append("            <artifactId>oracle-r2dbc</artifactId>\n");
        sb.append("            <version>0.1.0</version>\n");
        sb.append("        </dependency>\n");
        ORACLE_DRIVER_DEPENDENCY = sb.toString();

        sb.delete(0, sb.length());
        sb.append("    <dependencyManagement>\n");
        sb.append("        <dependencies>\n");
        sb.append("            <dependency>\n");
        sb.append("                <groupId>org.springframework.data</groupId>\n");
        sb.append("                <artifactId>spring-data-r2dbc</artifactId>\n");
        sb.append("                <version>1.2.6</version>\n");
        sb.append("            </dependency>\n");
        sb.append("        </dependencies>\n");
        sb.append("    </dependencyManagement>\n");
        SPRING_DATA_DEPENDENCY_MGMT = sb.toString();
    }

    public String getDependencyManager(){
        return this.SPRING_DATA_DEPENDENCY_MGMT;
    }

    public String getDependencies(){
        StringBuffer sb = new StringBuffer();
        sb.append(SPRING_DATA_DEPENDENCY);
        switch (this.type){
            case("h2"):
                sb.append(H2_DRIVER_DEPENDENCY);
                break;
            case("mariadb"):
                sb.append(MARIADB_DRIVER_DEPENDENCY);
                break;
            case("mssql"):
                sb.append(MSSQL_DRIVER_DEPENDENCY);
                break;
            case("postgres"):
                sb.append(POSTGRES_DRIVER_DEPENDENCY);
                break;
            case("oracle"):
                sb.append(ORACLE_DRIVER_DEPENDENCY);
                break;
        }
        return sb.toString();
    }
}
