package org.slicer;

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

    public static String buildTestClass(String name, String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append(buildPackage(packagename));
        sb.append("import org.junit.jupiter.api.*;\n");
        sb.append("import org.assertj.core.api.Assertions;\n");
        sb.append("\n");
        sb.append("public class ");
        sb.append(name);
        sb.append("Test {\n");
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }

    public static String buildPackage(String packagename){
        StringBuilder sb = new StringBuilder();
        sb.append("package ");
        sb.append(packagename.toLowerCase());
        sb.append(";\n\n");
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
