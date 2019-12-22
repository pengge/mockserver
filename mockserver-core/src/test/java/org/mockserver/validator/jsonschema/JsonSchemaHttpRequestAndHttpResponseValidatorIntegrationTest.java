package org.mockserver.validator.jsonschema;

import org.junit.Test;
import org.mockserver.logging.MockServerLogger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockserver.character.Character.NEW_LINE;
import static org.mockserver.validator.jsonschema.JsonSchemaHttpRequestAndHttpResponseValidator.jsonSchemaHttpRequestAndHttpResponseValidator;

/**
 * @author jamesdbloom
 */
public class JsonSchemaHttpRequestAndHttpResponseValidatorIntegrationTest {

    private JsonSchemaValidator jsonSchemaValidator = jsonSchemaHttpRequestAndHttpResponseValidator(new MockServerLogger());

    private final String completeSerialisedHttpRequestAndHttpResponse = "{" + NEW_LINE +
        "  \"httpRequest\" : {" + NEW_LINE +
        "    \"method\" : \"GET\"," + NEW_LINE +
        "    \"path\" : \"somepath\"," + NEW_LINE +
        "    \"queryStringParameters\" : {" + NEW_LINE +
        "      \"queryStringParameterNameOne\" : [ \"queryStringParameterValueOne_One\", \"queryStringParameterValueOne_Two\" ]," + NEW_LINE +
        "      \"queryStringParameterNameTwo\" : [ \"queryStringParameterValueTwo_One\" ]" + NEW_LINE +
        "    }," + NEW_LINE +
        "    \"headers\" : {" + NEW_LINE +
        "      \"headerName\" : [ \"headerValue\" ]" + NEW_LINE +
        "    }," + NEW_LINE +
        "    \"cookies\" : {" + NEW_LINE +
        "      \"cookieName\" : \"cookieValue\"" + NEW_LINE +
        "    }," + NEW_LINE +
        "    \"keepAlive\" : true," + NEW_LINE +
        "    \"secure\" : true," + NEW_LINE +
        "    \"socketAddress\" : {" + NEW_LINE +
        "      \"host\" : \"someHost\"," + NEW_LINE +
        "      \"port\" : 1234," + NEW_LINE +
        "      \"scheme\" : \"HTTPS\"" + NEW_LINE +
        "    }," + NEW_LINE +
        "    \"body\" : \"someBody\"" + NEW_LINE +
        "  }," + NEW_LINE +
        "  \"httpResponse\" : {" + NEW_LINE +
        "    \"statusCode\" : 123," + NEW_LINE +
        "    \"reasonPhrase\" : \"randomPhrase\"," + NEW_LINE +
        "    \"headers\" : {" + NEW_LINE +
        "      \"headerName\" : [ \"headerValue\" ]" + NEW_LINE +
        "    }," + NEW_LINE +
        "    \"cookies\" : {" + NEW_LINE +
        "      \"cookieName\" : \"cookieValue\"" + NEW_LINE +
        "    }," + NEW_LINE +
        "    \"body\" : \"somebody\"," + NEW_LINE +
        "    \"delay\" : {" + NEW_LINE +
        "      \"timeUnit\" : \"MICROSECONDS\"," + NEW_LINE +
        "      \"value\" : 3" + NEW_LINE +
        "    }" + NEW_LINE +
        "  }" + NEW_LINE +
        "}";

    @Test
    public void shouldValidateValidCompleteRequestFromRawJson() {
        // when
        assertThat(jsonSchemaValidator.isValid(completeSerialisedHttpRequestAndHttpResponse), is(""));
    }

    @Test
    public void shouldValidateInvalidBodyType() {
        // when
        assertThat(jsonSchemaValidator.isValid("{" + NEW_LINE +
                "  \"httpRequest\" : {" + NEW_LINE +
                "    \"body\" : 1" + NEW_LINE +
                "  }" + NEW_LINE +
                "}"),
            is(
                "1 error:" + NEW_LINE +
                    " - for field \"/httpRequest/body\" a plain string, JSON object or one of the following example bodies must be specified " + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"BINARY\"," + NEW_LINE +
                    "     \"base64Bytes\": \"\"," + NEW_LINE +
                    "     \"contentType\": \"\"" + NEW_LINE +
                    "   }, " + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"JSON\"," + NEW_LINE +
                    "     \"json\": \"\"," + NEW_LINE +
                    "     \"contentType\": \"\"," + NEW_LINE +
                    "     \"matchType\": \"ONLY_MATCHING_FIELDS\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"JSON_SCHEMA\"," + NEW_LINE +
                    "     \"jsonSchema\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"JSON_PATH\"," + NEW_LINE +
                    "     \"jsonPath\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"PARAMETERS\"," + NEW_LINE +
                    "     \"parameters\": {\"name\": \"value\"}" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"REGEX\"," + NEW_LINE +
                    "     \"regex\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"STRING\"," + NEW_LINE +
                    "     \"string\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"XML\"," + NEW_LINE +
                    "     \"xml\": \"\"," + NEW_LINE +
                    "     \"contentType\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"XML_SCHEMA\"," + NEW_LINE +
                    "     \"xmlSchema\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"XPATH\"," + NEW_LINE +
                    "     \"xpath\": \"\"" + NEW_LINE +
                    "   }"
            ));
    }

    @Test
    public void shouldValidateInvalidExtraField() {
        // when
        assertThat(jsonSchemaValidator.isValid("{" + NEW_LINE +
                "  \"invalidField\" : {" + NEW_LINE +
                "    \"type\" : \"STRING\"," + NEW_LINE +
                "    \"value\" : \"someBody\"" + NEW_LINE +
                "  }" + NEW_LINE +
                "  }"),
            is(
                "1 error:" + NEW_LINE +
                    " - object instance has properties which are not allowed by the schema: [\"invalidField\"]"
            ));
    }

    @Test
    public void shouldValidateMultipleInvalidFieldTypes() {
        // when
        assertThat(jsonSchemaValidator.isValid("{" + NEW_LINE +
                "  \"httpRequest\" : {" + NEW_LINE +
                "    \"body\" : 1" + NEW_LINE +
                "  }," + NEW_LINE +
                "  \"method\" : 100," + NEW_LINE +
                "  \"path\" : false" + NEW_LINE +
                "  }"),
            is(
                "2 errors:" + NEW_LINE +
                    " - object instance has properties which are not allowed by the schema: [\"method\",\"path\"]" + NEW_LINE +
                    " - for field \"/httpRequest/body\" a plain string, JSON object or one of the following example bodies must be specified " + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"BINARY\"," + NEW_LINE +
                    "     \"base64Bytes\": \"\"," + NEW_LINE +
                    "     \"contentType\": \"\"" + NEW_LINE +
                    "   }, " + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"JSON\"," + NEW_LINE +
                    "     \"json\": \"\"," + NEW_LINE +
                    "     \"contentType\": \"\"," + NEW_LINE +
                    "     \"matchType\": \"ONLY_MATCHING_FIELDS\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"JSON_SCHEMA\"," + NEW_LINE +
                    "     \"jsonSchema\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"JSON_PATH\"," + NEW_LINE +
                    "     \"jsonPath\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"PARAMETERS\"," + NEW_LINE +
                    "     \"parameters\": {\"name\": \"value\"}" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"REGEX\"," + NEW_LINE +
                    "     \"regex\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"STRING\"," + NEW_LINE +
                    "     \"string\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"XML\"," + NEW_LINE +
                    "     \"xml\": \"\"," + NEW_LINE +
                    "     \"contentType\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"XML_SCHEMA\"," + NEW_LINE +
                    "     \"xmlSchema\": \"\"" + NEW_LINE +
                    "   }," + NEW_LINE +
                    "   {" + NEW_LINE +
                    "     \"not\": false," + NEW_LINE +
                    "     \"type\": \"XPATH\"," + NEW_LINE +
                    "     \"xpath\": \"\"" + NEW_LINE +
                    "   }"
            ));
    }

}
