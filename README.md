# Mindera OpenAPI Codegen 

## Overview
This project purpose if to aggregate all Mindera OpenAPI custom OpenAPI code generators.

## What's OpenAPI?
The goal of OpenAPI is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection. When properly defined via OpenAPI, a consumer can understand and interact with the remote service with a minimal amount of implementation logic. Similar to what interfaces have done for lower-level programming, OpenAPI removes the guesswork in calling the service.


Check out [OpenAPI-Spec](https://github.com/OAI/OpenAPI-Specification) for additional information about the OpenAPI project, including additional libraries with support for other languages and more. 

## How do I use this?

```
.
|- README.md    // this file
|- pom.xml      // build script
|-- src
|--- main
|---- java
|----- com.mindera.openapi.generator.SpringServerGenerator.java // generator file
|----- com.mindera.openapi.generator.Swift4Generator.java // generator file
|---- resources
|----- MinderaSpringServer // template files
|----- MinderaSwift4 // template files
|----- META-INF
|------ services
|------- io.swagger.codegen.CodegenConfig
```

You can run this to package the generators:

```
mvn package
```
