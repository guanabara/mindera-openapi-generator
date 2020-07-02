package com.mindera.openapi.generator.spring;

import com.mindera.openapi.generator.SpringServerGenerator;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.nio.file.Paths;

public class SpringServerGeneratorTest {

    @Test
    public void testPetstoreGeneratedCodeCompilation() throws Exception {
        final File folder = getTemporaryFolder();

        try {
            testGeneratedCodeCompilation("src/test/resources/petstore.yml", folder);
            File petModel = new File(folder, "/src/main/java/org/openapitools/model/Pet.java");
            Assert.assertTrue(petModel.exists());
        } finally {
            FileUtils.deleteDirectory(folder);
        }
    }

    @Test
    public void testPetstore2GeneratedCodeCompilation() throws Exception {
        final File folder = getTemporaryFolder();
        try {
            testGeneratedCodeCompilation("src/test/resources/petstore2.yml", folder);
            File petModel = new File(folder, "/src/main/java/org/openapitools/model/Pet.java");
            Assert.assertTrue(petModel.exists());
            File editablePetModel = new File(folder, "/src/main/java/org/openapitools/model/EditablePet.java");
            Assert.assertTrue(editablePetModel.exists());
        } finally {
            FileUtils.deleteDirectory(folder);
        }
    }

    @Test
    public void testApiGeneratedCodeCompilation() throws Exception {
        final File folder = getTemporaryFolder();
        try {
            testGeneratedCodeCompilation("src/test/resources/api.yml", folder);
            File petModel = new File(folder, "/src/main/java/org/openapitools/model/Company.java");
            Assert.assertTrue(petModel.exists());
            File editablePetModel = new File(folder, "/src/main/java/org/openapitools/model/EditablePet.java");
            Assert.assertTrue(editablePetModel.exists());
        } finally {
            //FileUtils.deleteDirectory(folder);
        }
    }

    private File getTemporaryFolder() throws Exception {
        //final File folder = new File(Paths.get(FileUtils.getTempDirectoryPath(), String.format("%s_%s",
        //        getClass().getSimpleName().toLowerCase(),
        //        RandomStringUtils.randomAlphanumeric(4))).toAbsolutePath().toString());
        final File folder = new File(
                Paths.get("./tmp", String.format("%s_%s", getClass().getSimpleName().toLowerCase(),
                RandomStringUtils.randomAlphanumeric(4))).toAbsolutePath().toString());
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new Exception("Failed to create temporary folder");
            }
        }
        return folder;
    }

    private void testGeneratedCodeCompilation(String apiLocation, final File folder) throws Exception {
        System.setProperty("debugOpenAPI", "true");

        File output = new File(folder.getAbsolutePath());
        SwaggerParseResult parseResult = (new OpenAPIParser()).readLocation(apiLocation, null, null);
        OpenAPI openAPI = parseResult.getOpenAPI();

        SpringServerGenerator codegen = new SpringServerGenerator();
        codegen.setOutputDir(output.getAbsolutePath());

        ClientOptInput clientOptInput = (new ClientOptInput()).openAPI(openAPI).config(codegen);
        (new DefaultGenerator()).opts(clientOptInput).generate();

        File pomFile = new File(folder, "pom.xml");
        URL pomUrl = getClass().getClassLoader().getResources("mindera_spring_server_pom.xml").nextElement();
        FileUtils.copyURLToFile(pomUrl, pomFile);

        Process process = new ProcessBuilder("/usr/local/bin/mvn", "clean", "compile")
                .directory(output)
                .redirectErrorStream(true)
                .redirectOutput(Redirect.INHERIT)
                .start();
        Assert.assertEquals(process.waitFor(), 0);
    }
}