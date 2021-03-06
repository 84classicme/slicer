# slicer
Stubbed Lightweight Implementations of Controllers for Enterprise REST

## Features
 
- Accepts XML-based descriptions of RESTful application controllers, auto-wired child services, and datasources.
- Generates basic configuration, including application.xml and pom.mxl and dependencies, for Spring REST and WebFlux frameworks, including Spring R2DBC.
- Controller services can auto-wire their own child services and even repositories.

## Configuration

The below example provides a typical use case. [See the dtd][1] for syntax. 

```
<?xml version = "1.0" encoding = "UTF-8" ?>
<!DOCTYPE Slice SYSTEM "slicer.dtd">
<Slice name="SlicerTestOne" type="spring-webflux" groupId="io.slicer" artifactId="slicer-core" description="Example slice implementing a few endpoints of various design.">
    <Controllers>
        <Controller name="AccountController">
            <Services>
                <Service name="AccountService">
                    <Services>
                        <Service name="ValidatorService"/>
                        <Service name="EventTrackerService"/>
                    </Services>
                    <Repositories>
                        <Repository name="AccountRepository"/>
                    </Repositories>
                </Service>
                <Service name="OnboardingService">
                </Service>
            </Services>
            <Endpoints>
                <Endpoint name="createAccount" method="POST" uri="/accounts">
                    <RequestBody name="slice" type="Slice"/>
                    <ResponseCode>202</ResponseCode>
                    <ResponseType>Slice</ResponseType>
                </Endpoint>
                 <Endpoint name="getAccount" method="GET" uri="/accounts/{region}/{department}">
                    <PathVariables>
                        <PathVariable name="department" type="String"/>
                        <PathVariable name="region" type="String"/>
                    </PathVariables>
                    <ResponseCode>200</ResponseCode>
                    <ResponseType>Slice</ResponseType>
                </Endpoint>
            </Endpoints>
        </Controller>
    </Controllers>
    <DataSource type="mariadb">
        <Port>1090</Port>
        <Host>localhost</Host>
        <Database>my_schema</Database>
        <Url>localhost:1090</Url>
        <Username>scott</Username>
        <Password>tiger</Password>
    </DataSource>
</Slice>
```

[1]: src/test/resources/slicer.dtd 

