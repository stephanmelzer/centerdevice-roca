package de.centerdevice.roca.controller;

import de.centerdevice.roca.centerdevice.CenterDeviceService;
import de.centerdevice.roca.centerdevice.CenterDeviceServiceStub;
import de.centerdevice.roca.centerdevice.HttpResponse;
import de.centerdevice.roca.oauth.OAuthAccessToken;
import java.io.InputStream;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.MediaType;
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
public class GroupControllerTest {

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
        centerDeviceServiceStub.setHttpResponse(null);
    }

    @Test
    public void getAllGroupsAsJsonTest() throws Exception {
        InputStream jsonStreamStub = servletContext.getResourceAsStream("/WEB-INF/stubs/groups.json");
        String groupsJson = IOUtils.toString(jsonStreamStub, "UTF-8");

        mockMvc.perform(get("/groups").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(groupsJson))
                .andDo(print());
    }

    @Test
    public void getGroupTest() throws Exception {
        String groupId = "abc-123";
        mockMvc.perform(post("/group/" + groupId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void groupNotFoundTest() throws Exception {
        String groupId = "123";
        HttpResponse responseStub = new HttpResponse();
        responseStub.setStatusCode(404);
        centerDeviceServiceStub.setHttpResponse(responseStub);

        mockMvc.perform(post("/group/" + groupId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Configuration
    public static class TestConfiguration {

        @Bean
        public GroupController groupController() {
            return new GroupController();
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
