# TomcatExtension
JUnit5 Tomcat Extension

A JUnit5 extension to run a servlet with Tomcat

## Usage

In your unit test class, add the extension as a static field with:

```
@RegisterExtension
static TomcatExtension tomcatExtension = TomcatExtension.builder()
    .host("localhost")
    .port(8070)
    .clazz(ReverseProxyServlet.class)
    .build();
```
 
You may also provide values for AppBase, DocBase, Context and Mappings.

## Authors

* **Thorsten J. Lorenz**

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

