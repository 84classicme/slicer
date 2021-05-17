package slicer;

public class SlicerUtils {

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
}
