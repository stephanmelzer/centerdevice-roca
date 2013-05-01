package de.centerdevice.roca.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.mockito.BDDMockito.*;

@RunWith(JUnit4.class)
public class CorsFilterTest {

    private CorsFilter corsFilter;

    @Before
    public void setUp() {
        corsFilter = new CorsFilter();
    }

    @Test
    public void corsFilterTest() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Origin")).thenReturn("http://spa.local:8000");
        corsFilter.doFilter(request, response, filterChain);

        verify(response).addHeader("Access-Control-Allow-Origin", "http://spa.local:8000");
        verify(response).addHeader("Access-Control-Allow-Credentials", "true");
    }
}
