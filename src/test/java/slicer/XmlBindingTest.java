package slicer;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import help.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class XmlBindingTest {

    @Test
    public void testXmlBinding() throws IOException {
        URL url = this.getClass().getResource("/slice.xml");
        File file = new File(url.getFile());
        XmlMapper xmlMapper = new XmlMapper();
        String xml = TestHelper.inputStreamToString(new FileInputStream(file));

        Slice slice = xmlMapper.readValue(xml, Slice.class);
        Assertions.assertThat(slice.getName().equals("SliceName"));

        List<Controller> controllers = slice.getControllers();
        Assertions.assertThat(controllers.size() == 1);

        Controller controller = controllers.get(0);
        Assertions.assertThat(controller.getName().equals("ControllerName"));

        List<Service> services = controller.getServices();
        Assertions.assertThat(services.size() == 2);

        Flux<Service> serviceFlux = Flux.fromIterable(services);

        Long count = serviceFlux.filter(s -> s.getName().equals("ServiceNameOne")).count().block();
        Assertions.assertThat(count.intValue() == 1);

        List<Repository> repositories = serviceFlux.filter(s -> s.getName().equals("ServiceNameOne")).blockFirst().getRepositories();
        Assertions.assertThat(count.intValue() == 2);

        Flux<Repository> repositoryFlux = Flux.fromIterable(repositories);

        count = repositoryFlux.filter(s -> s.getName().equals("RepositoryOne")).count().block();
        Assertions.assertThat(count.intValue() == 1);

        count = repositoryFlux.filter(s -> s.getName().equals("RepositoryTwo")).count().block();
        Assertions.assertThat(count.intValue() == 1);

        count = serviceFlux.filter(s -> s.getName().equals("ServiceNameTwo")).count().block();
        Assertions.assertThat(count.intValue() == 1);

        repositories = serviceFlux.filter(s -> s.getName().equals("ServiceNameTwo")).blockFirst().getRepositories();
        Assertions.assertThat(count.intValue() == 2);

        repositoryFlux = Flux.fromIterable(repositories);

        count = repositoryFlux.filter(s -> s.getName().equals("RepositoryThree")).count().block();
        Assertions.assertThat(count.intValue() == 1);

        count = repositoryFlux.filter(s -> s.getName().equals("RepositoryFour")).count().block();
        Assertions.assertThat(count.intValue() == 1);

        List<Endpoint> endpoints = controller.getEndpoints();
        Assertions.assertThat(endpoints.size() == 2);

        Flux<Endpoint> endpointFlux = Flux.fromIterable(endpoints);

        count = endpointFlux.filter(e -> e.getName().equals("EndpointOneName")).count().block();
        Assertions.assertThat(count.intValue() == 1);

        Endpoint endpoint = endpointFlux.filter(e -> e.getName().equals("EndpointOneName")).blockFirst();
        Assertions.assertThat(endpoint.getUri().equals("EndpointOneUri"));
        Assertions.assertThat(endpoint.getResponseCode().equals("EndpointOneResponseCode"));
        Assertions.assertThat(endpoint.getResponseType().equals("EndpointOneResponseType"));

        List<RequestParam> requestParams = endpoint.getRequestParams();
        Assertions.assertThat(requestParams.size() == 2);
        Flux<RequestParam> requestParamFlux = Flux.fromIterable(requestParams);

        List<PathVariable> pathVariables = endpoint.getPathVariables();
        Assertions.assertThat(pathVariables.size() == 2);
        Flux<PathVariable> pathVariableFlux = Flux.fromIterable(pathVariables);

        count = endpointFlux.filter(e -> e.getName().equals("EndpointTwoName")).count().block();
        Assertions.assertThat(count.intValue() == 1);

        endpoint = endpointFlux.filter(e -> e.getName().equals("EndpointTwoName")).blockFirst();
        Assertions.assertThat(endpoint.getUri().equals("EndpointTwoUri"));
        Assertions.assertThat(endpoint.getResponseCode().equals("EndpointTwoResponseCode"));
        Assertions.assertThat(endpoint.getResponseType().equals("EndpointTwoResponseType"));

        RequestBody requestBody = endpoint.getRequestBody();
        Assertions.assertThat(requestBody.getName().equals("EndpointTwoRequestBodyName"));
        Assertions.assertThat(requestBody.getType().equals("EndpointTwoRequestBodyType"));
    }

}
