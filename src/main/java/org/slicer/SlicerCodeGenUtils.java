package org.slicer;

import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class SlicerCodeGenUtils {

    public static String buildAutowired(String name){
        StringBuilder sb = new StringBuilder();
        sb.append("    @Autowired\n");
        sb.append("    ");
        sb.append(name);
        sb.append(" ");
        sb.append(name.substring(0, 1).toLowerCase() + name.substring(1));
        sb.append(";\n\n");
        return sb.toString();
    }

    public static String buildMember(String name){
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" ");
        sb.append(name.substring(0, 1).toLowerCase() + name.substring(1));
        sb.append(";\n");
        return sb.toString();
    }

    public static String buildConstructorParam(String name){
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" ");
        sb.append(name.substring(0, 1).toLowerCase() + name.substring(1));
        sb.append(", ");
        return sb.toString();
    }

    public static String buildConstructorAssignment(String name){
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        StringBuilder sb = new StringBuilder();
        sb.append("        this.");
        sb.append(name);
        sb.append(" = ");
        sb.append(name);
        sb.append(";\n");
        return sb.toString();
    }

    public static String buildTestClass(String name, String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append(buildPackage(packagename));
        sb.append("import org.junit.Before;\n");
        sb.append("import org.junit.Test;\n");
        sb.append("import org.assertj.core.api.Assertions;\n");
        sb.append("\n");
        sb.append("public class ");
        sb.append(name);
        sb.append("Test {\n");
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }

    public static String buildTestClass(String name, String packagename, Tuple2<List<Service>, List<Repository>> mocks){
        StringBuilder sb = new StringBuilder();
        sb.append(buildPackage(packagename));
        sb.append("\n");
        sb.append("import org.junit.Before;\n");
        sb.append("import org.junit.Test;\n");
        sb.append("import org.assertj.core.api.Assertions;\n");
        if (mocks != null && mocks.size() > 0) {
            sb.append("import org.assertj.core.api.Mockito.InjectMocks;\n");
            sb.append("import org.assertj.core.api.Mockito.Mock;\n");
            sb.append("import org.assertj.core.api.Mockito.Mockito;\n");
            sb.append("import org.assertj.core.api.Mockito.MockitoAnnotations;\n");
        }
        sb.append("\n");
        sb.append("public class ");
        sb.append(name);
        sb.append("Test {\n");
        sb.append("\n");
        if (mocks != null && mocks.size() > 0){
            mocks.getT1().forEach(service -> {
                sb.append("    @Mock\n");
                sb.append("    ");
                sb.append(service.getName());
                sb.append(" ");
                sb.append(service.getName().substring(0, 1).toLowerCase() + service.getName().substring(1));
                sb.append("\n\n");
            });
            mocks.getT2().forEach(repository -> {
                sb.append("    @Mock\n");
                sb.append("    ");
                sb.append(repository.getName());
                sb.append(" ");
                sb.append(repository.getName().substring(0, 1).toLowerCase() + repository.getName().substring(1));
                sb.append("\n\n");
            });
            sb.append("\n");
            sb.append("    @InjectMocks\n");
            sb.append("    ");
            sb.append(name);
            sb.append(" ");
            sb.append(name.substring(0, 1).toLowerCase() + name.substring(1));
            sb.append("\n\n");
            sb.append("    @Before\n");
            sb.append("    public void setup(){\n");
            sb.append("        MockitoAnnotations.openMocks(this);\n");
            sb.append("    }\n\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public static String buildPackage(String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append("package ");
        sb.append(packagename.toLowerCase());
        sb.append(";\n");
        return sb.toString();
    }

    public static String buildApplicationClass(String name, String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append(buildPackage(packagename));
        sb.append("import org.springframework.boot.SpringApplication;\n");
        sb.append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n");
        sb.append("\n");
        sb.append("@SpringBootApplication\n");
        sb.append("public class ");
        sb.append(name);
        sb.append("Application {\n");
        sb.append("public static void main(String[] args) { SpringApplication.run(");
        sb.append(name);
        sb.append("Application.class, args); }\n");
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }
}
