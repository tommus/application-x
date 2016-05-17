package com.todev.appx.controllers;

import com.todev.appx.Application;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public abstract class JsonControllerTest {
    protected MockMvc mockMvc;
    protected HttpMessageConverter httpMessageConverter;

    protected MediaType unsupportedContent = new MediaType(
        MediaType.APPLICATION_XHTML_XML.getType(),
        MediaType.APPLICATION_XHTML_XML.getSubtype()
    );

    protected MediaType jsonContent = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype()
    );

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    public void setConverters(HttpMessageConverter<?>[] converters) {
        httpMessageConverter = Arrays.asList(converters).stream().filter(
            c -> c instanceof MappingJackson2HttpMessageConverter).findAny().get();
    }

    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(context).build();
    }

    /**
     * Serializes given object as JSON-formatted string.
     *
     * @param o an object that should be serialized.
     * @return serialized JSON string.
     * @throws IOException an error that will be raised while trying to serialize malformed object.
     */
    protected String serialize(Object o) throws IOException {
        MockHttpOutputMessage mockMessage = new MockHttpOutputMessage();
        httpMessageConverter.write(o, jsonContent, mockMessage);
        return mockMessage.getBodyAsString();
    }
}
