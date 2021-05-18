package org.slicer;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // provides @ToString, @EqualsAndHashCode, @Getter, @Setter, and @RequiredArgsConstructor
@NoArgsConstructor
public class Endpoint implements Writeable {
    @JacksonXmlProperty(isAttribute = true)
    private String method;

    @JacksonXmlProperty(isAttribute = true)
    private String uri;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(localName = "RequestParams")
    private List<RequestParam> requestParams;

    @JacksonXmlProperty(localName = "PathVariables")
    private List<PathVariable> pathVariables;

    @JacksonXmlProperty(localName = "RequestBody")
    private RequestBody requestBody;

    @JacksonXmlProperty(localName = "ResponseCode")
    private String responseCode;

    @JacksonXmlProperty(localName = "ResponseType")
    private String responseType;

    public String toImpl(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.buildMapping());
        if (this.responseCode != null) {
            sb.append(this.buildResponseCode());
            sb.append(this.buildApiResponse());
        }
        sb.append(this.buildApiOperation());
        sb.append(this.buildMethodSignature());
        return sb.toString();
    }

    private String buildMapping(){
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        switch (this.method){
            case("GET"):
                sb.append("@GetMapping");
                break;
            case("POST"):
                sb.append("@PostMapping");
                break;
            case("PUT"):
                sb.append("@PutMapping");
                break;
            case("DELETE"):
                sb.append("@DeleteMapping");
                break;
        }
        sb.append("(\"");
        sb.append(this.getUri());
        sb.append("\")\n");
        return sb.toString();
    }

    private String buildResponseCode(){
        StringBuilder sb = new StringBuilder();
        sb.append("    @ResponseStatus(");
        switch (this.responseCode) {
            case ("200"):
                sb.append("HttpStatus.OK");
                break;
            case ("202"):
                sb.append("HttpStatus.CREATED");
                break;
        }
        sb.append(")\n");
        return sb.toString();
    }

    private String buildApiResponse(){
        StringBuilder sb = new StringBuilder();
        sb.append("    @ApiResponse(code = ");
        switch (this.responseCode) {
            case ("200"):
                sb.append("200, message = \"OK.\")\n");
                break;
            case ("202"):
                sb.append("202, message = \"Entity created.\")\n");
                break;
        }
        return sb.toString();
    }

    private String buildMethodSignature(){
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        sb.append("public ResponseEntity<");
        sb.append(this.responseType);
        sb.append("> ");
        sb.append(this.name);
        sb.append("(");
        if(this.getRequestBody() !=null) sb.append(this.buildRequestBody());
        sb.append(this.buildRequestParams());
        sb.append(this.buildPathVariables());
        sb.replace(sb.lastIndexOf(", "), sb.length()-1, ") {\n");
        sb.append("        return null;\n");
        sb.append("    }");
        return sb.toString();
    }

    private  String buildRequestBody(){
        StringBuilder sb = new StringBuilder();
        sb.append("@RequestBody ");
        sb.append(this.requestBody.getType());
        sb.append(" ");
        sb.append(this.requestBody.getName());
        sb.append(", ");
        return sb.toString();
    }

    private String buildApiOperation(){
        StringBuilder sb = new StringBuilder();
        sb.append("    @ApiOperation(value = ");
        sb.append("\"Rest Method\",\n");
        sb.append("      response = ");
        sb.append(this.responseType);
        sb.append(".class)\n");
        return sb.toString();
    }

    private  String buildRequestParams(){
        StringBuilder sb = new StringBuilder();
        if(requestParams != null) {
            this.requestParams.forEach(p -> {
                sb.append("@RequestParam(value=\"");
                sb.append(p.getName());
                sb.append("\") ");
                sb.append(p.getType());
                sb.append(" ");
                sb.append(p.getName());
                sb.append(", ");
            });
        }
        return sb.toString();
    }

    private String buildPathVariables(){
        StringBuilder sb = new StringBuilder();
        if(pathVariables != null) {
            this.pathVariables.forEach(v -> {
                sb.append("@PathVariable ");
                sb.append(v.getType());
                sb.append(" ");
                sb.append(v.getName());
                sb.append(", ");
            });
        }
        return sb.toString();
    }
}
