package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.CenterDeviceService;
import de.centerdevice.roca.centerdevice.CenterDeviceServiceStub;
import de.centerdevice.roca.centerdevice.HttpMessage;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import javax.servlet.ServletContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class DocumentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private CenterDeviceService centerDeviceService;
    private CenterDeviceServiceStub centerDeviceServiceStub;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        centerDeviceServiceStub = (CenterDeviceServiceStub) centerDeviceService;
        centerDeviceServiceStub.setHttpMessage(null);
    }

    @Test
    public void downloadDocumentTest() throws Exception {
        String documentId = "123";
        mockMvc.perform(get("/document/" + documentId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/plain; charset=utf-8"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"documentFileStub.txt\""))
                .andExpect(header().string("Content-Length", "20"))
                .andDo(print());
    }

    @Test
    public void downloadDocumentNotLoggedInTest() throws Exception {
        String documentId = "123";
        HttpMessage responseStub = new HttpMessage();
        responseStub.setStatusCode(401);
        centerDeviceServiceStub.setHttpMessage(responseStub);

        mockMvc.perform(get("/document/" + documentId))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Configuration
    public static class TestConfiguration {

        @Bean
        public DocumentController documentController() {
            return new DocumentController();
        }

        @Bean
        public CenterDeviceService createCenterDeviceServiceStub() {
            return new CenterDeviceServiceStub();
        }

        @Bean
        @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
        public OAuthAccessToken oauthAccessToken() {
            OAuthAccessToken accessToken = new OAuthAccessToken();

            return accessToken;
        }
    }
}
