// package com.maoatao.cas.config;
//
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import io.swagger.v3.oas.models.ExternalDocumentation;
// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.info.Contact;
// import io.swagger.v3.oas.models.info.Info;
// import io.swagger.v3.oas.models.info.License;
// import lombok.extern.slf4j.Slf4j;
// import org.springdoc.core.models.GroupedOpenApi;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// /**
//  * <p>
//  * Swagger UI See <a href="http://127.0.0.1:8080/swagger-ui/index.html">http://127.0.0.1:8080/swagger-ui/index.html</a>
//  * </P>
//  * <p>
//  * Knife4j UI See <a href="http://127.0.0.1:8080/doc.html">http://127.0.0.1:8080/doc.html</a>
//  * </P>
//  *
//  * @author MaoAtao
//  * @date 2023-02-04 20:08:50
//  */
// @Slf4j
// @Configuration
// public class SpringDocConfig {
//
//     private Info info;
//     private ExternalDocumentation external;
//     private boolean initialized = true;
//
//     @Value("${app.name:IKAROS}")
//     private String appName;
//
//     @Value("${server.port:8080}")
//     private String port;
//
//     @Bean
//     public OpenAPI api() {
//         if (info == null) {
//             info = buildInfo();
//         }
//         if (external == null) {
//             external = buildExternal();
//         }
//         if (initialized) {
//             initialized = false;
//             log.info("Init SpringDoc...");
//             log.info("API Docs [Swagger-UI http://127.0.0.1:{}/swagger-ui/index.html]", port);
//             log.info("API Docs [Knife4j-UI http://127.0.0.1:{}/doc.html]", port);
//         }
//         return new OpenAPI().info(info).externalDocs(external);
//     }
//
//     @Bean
//     public GroupedOpenApi defultApi() {
//         return GroupedOpenApi.builder()
//                 .group("defult")
//                 .pathsToMatch("/**")
//                 // 方法过滤，该方法的声明类需要有Tag注解
//                 .addOpenApiMethodFilter(method ->
//                         method.isAnnotationPresent(Operation.class) && method.getDeclaringClass().isAnnotationPresent(Tag.class)
//                 )
//                 .build();
//     }
//
//     private Info buildInfo() {
//         return new Info()
//                 .title(appName + " 接口文档")
//                 .contact(new Contact()
//                         .name("MaoAtao")
//                         .url("https://www.maoatao.com")
//                         .email("maoatao@outlook.com")
//                 )
//                 .description("SpringDoc 生成的接口文档(仅开发环境可用)")
//                 .license(new License()
//                         .name("The Apache License, Version 2.0")
//                         .url("https://www.apache.org/licenses/LICENSE-2.0.html")
//                 )
//                 .version("v1.0");
//     }
//
//     private ExternalDocumentation buildExternal() {
//         return new ExternalDocumentation()
//                 .description("Use SpringDoc OpenApi v2.x To Support SpringBoot v3.x")
//                 .url("https://springdoc.org/v2/");
//     }
// }
