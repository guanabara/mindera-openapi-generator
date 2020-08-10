package com.mindera.openapi.generator;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.SpringCodegen;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.openapitools.codegen.utils.StringUtils.camelize;

public class SpringServerGenerator extends SpringCodegen {
    private static final String CONFIG_OPTION_IS_CORS_ENABLED = "isCorsEnabled";

    public SpringServerGenerator() {
        super();

        embeddedTemplateDir = templateDir = "SpringServer";

        supportedLibraries.clear();
        supportedLibraries.put(DEFAULT_LIBRARY, "Default Spring MVC server stub.");

        modelDocTemplateFiles.clear();
        apiDocTemplateFiles.clear();

        java8 = true;
        interfaceOnly = true;

        cliOptions.add(new CliOption(CONFIG_OPTION_IS_CORS_ENABLED, "should add method to the CORS configuration"));
    }

    @Override
    public String getName() {
        return "mindera-spring";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        if (!additionalProperties.containsKey(CONFIG_OPTION_IS_CORS_ENABLED)) {
            additionalProperties.put(CONFIG_OPTION_IS_CORS_ENABLED, false);
        }

        importMapping.put("InputStreamResource", "org.springframework.core.io.InputStreamResource");

        supportingFiles.clear();
        supportingFiles.add(new SupportingFile("openapiConfig.mustache",
                (sourceFolder + File.separator + configPackage).replace(".", File.separator), "OpenApiConfig.java"));
    }

    @Override
    public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
        super.addOperationToGroup(tag, resourcePath, operation, co, operations);

        if (co.returnType != null && co.returnType.equals("File")) {
            co.returnType = "InputStreamResource";
            co.imports.add("InputStreamResource");
        }

        ObjectNode returnTypeOverride = (ObjectNode) co.vendorExtensions.get("x-returnTypeOverride");
        if (returnTypeOverride != null) {
            String type = returnTypeOverride.get("type").asText();
            String importClass = returnTypeOverride.get("import").asText();
            co.vendorExtensions.replace("x-returnTypeOverride", ImmutableMap.of("type", type, "import", importClass));
            co.imports.add(type);
            importMapping.put(type, importClass);
        }
    }

    @Override
    public CodegenProperty fromProperty(String name, Schema p) {
        CodegenProperty codegenProperty = super.fromProperty(name, p);

        if (name != null) {
            name = camelize(toVarName(name), true);
        }
        codegenProperty.vendorExtensions.put("builder", name);
        return codegenProperty;
    }

    @Override
    public void preprocessOpenAPI(OpenAPI openAPI) {
        super.preprocessOpenAPI(openAPI);
        String dashFormattedTitle = openAPI.getInfo().getTitle().replace(' ', '-').toLowerCase();
        additionalProperties.put("titleDashFormat", dashFormattedTitle);
        additionalProperties.put("titleCamelFormat", CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, dashFormattedTitle));
    }

    @Override
    public CodegenModel fromModel(String name, Schema model) {
        CodegenModel codegenModel = super.fromModel(name, model);
        codegenModel.imports.remove("ApiModel");
        codegenModel.imports.remove("ApiModelProperty");
        return codegenModel;
    }

    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        return objs;
    }
}