package org.slicer;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class SlicerUtils {

    public static final Path ZIP_LOCATION = FileSystems.getDefault().getPath("src", "main", "resources", "slicer", "slicer.zip");

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

    public static String buildTestClass(String name){
        StringBuilder sb = new StringBuilder();
        sb.append("package slicer.generated;\n\n");
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
}
