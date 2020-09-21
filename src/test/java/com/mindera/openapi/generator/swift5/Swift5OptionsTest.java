/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mindera.openapi.generator.swift5;

import org.openapitools.codegen.AbstractOptionsTest;
import org.openapitools.codegen.CodegenConfig;
import com.mindera.openapi.generator.Swift5Generator;
import com.mindera.openapi.generator.swift5.options.MinderaSwift5OptionsProvider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Swift5OptionsTest extends AbstractOptionsTest {
    private Swift5Generator clientCodegen = mock(Swift5Generator.class, mockSettings);

    public Swift5OptionsTest() {
        super(new MinderaSwift5OptionsProvider());
    }

    @Override
    protected CodegenConfig getCodegenConfig() {
        return clientCodegen;
    }

    @SuppressWarnings("unused")
    @Override
    protected void verifyOptions() {
        verify(clientCodegen).setSortParamsByRequiredFlag(Boolean.valueOf(MinderaSwift5OptionsProvider.SORT_PARAMS_VALUE));
        verify(clientCodegen).setProjectName(MinderaSwift5OptionsProvider.PROJECT_NAME_VALUE);
        verify(clientCodegen).setResponseAs(MinderaSwift5OptionsProvider.RESPONSE_AS_VALUE.split(","));
        verify(clientCodegen).setNonPublicApi(Boolean.parseBoolean(MinderaSwift5OptionsProvider.NON_PUBLIC_API_REQUIRED_VALUE));
        verify(clientCodegen).setObjcCompatible(Boolean.parseBoolean(MinderaSwift5OptionsProvider.OBJC_COMPATIBLE_VALUE));
        verify(clientCodegen).setLenientTypeCast(Boolean.parseBoolean(MinderaSwift5OptionsProvider.LENIENT_TYPE_CAST_VALUE));
        verify(clientCodegen).setPrependFormOrBodyParameters(Boolean.valueOf(MinderaSwift5OptionsProvider.PREPEND_FORM_OR_BODY_PARAMETERS_VALUE));
    }
}
