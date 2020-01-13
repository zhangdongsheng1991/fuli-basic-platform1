package com.fuli.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.web.Swagger2Controller;

import java.util.Optional;


/**
 * 获取注册的服务
 * @author WFZ
 * @description: swagger-resource
 * @date 2019-06-0310:47
 */
@RestController
public class SwaggerHandler {

    @Autowired(required = false)
    private SecurityConfiguration securityConfiguration;

    @Autowired(required = false)
    private UiConfiguration uiConfiguration;

    private Swagger2Controller swagger2Controller;
    private final SwaggerResourcesProvider swaggerResources;

    @Autowired
    public SwaggerHandler(SwaggerResourcesProvider swaggerResources) {
        this.swaggerResources = swaggerResources;
    }


    @GetMapping("/swagger-resources/configuration/security")
    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
        return Mono.just(new ResponseEntity<>(
                Optional.ofNullable(securityConfiguration).orElse(SecurityConfigurationBuilder.builder().build()), HttpStatus.OK));
    }

    @GetMapping("/swagger-resources/configuration/ui")
    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
        return Mono.just(new ResponseEntity<>(
                Optional.ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build()), HttpStatus.OK));
    }

    @GetMapping("/swagger-resources")
    public Mono<ResponseEntity> swaggerResources() {
        return Mono.just((new ResponseEntity<>(swaggerResources.get(), HttpStatus.OK)));
    }

//    @RequestMapping(value = "/v2/api-docs", method = RequestMethod.GET, produces = {"application/json", "application/hal+json"})
//    @ResponseBody
//    public ResponseEntity<Json> getDocumentation(
//            @RequestParam(value = "group", required = false) String swaggerGroup,
//            HttpServletRequest servletRequest) {
//        return swagger2Controller.getDocumentation(swaggerGroup, servletRequest);
//    }
}
