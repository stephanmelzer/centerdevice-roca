package de.centerdevice.roca.config;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * taken from https://gist.github.com/kdonald/2232095 adjusted to fit:
 * http://www.html5rocks.com/static/images/cors_server_flowchart.png
 *
 */
public class CorsFilter extends OncePerRequestFilter {

    private String[] allowedHosts = {"http://spa.local:8000",
        "http://localhost:8000",
        "http://centerdevice-spa.herokuapp.com"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean isPreFlightRequest = false;
//        String[] allowedHeaders = {"Accept", "Origin", "X-Requested-With"};
//        String[] allowedMethods = {"GET", "POST", "PUT", "DELETE"};

        //TODO: make sure that every new entry in these arries are lowercase
        String[] allowedHeaders = {"accept", "origin", "x-requested-with"};
        String[] allowedMethods = {"get", "post", "put", "delete"};


        String method = request.getHeader("Access-Control-Request-Method");
        String options = request.getMethod();
        String originHeader = request.getHeader("Origin");

        if (originHeader != null && isAllowedHost(originHeader)) {
            if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
                // the request is a CORS "pre-flight" request
                //isPreFlightRequest = true;

                if (hasValidRequestHeaders(request, allowedHeaders)) {
                    isPreFlightRequest = true;

                    // TODO: generate string from array allowedHeaders
                    response.addHeader("Access-Control-Allow-Headers", "Accept, origin, X-Requested-With");
                }

                if (hasValidRequestMethods(request, allowedMethods)) {
                    isPreFlightRequest = true;

                    // TODO: generate string from array allowedMethods
                    response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                }

                // pre-flight header should be saved for 30 min
                response.addHeader("Access-Control-Max-Age", "1800");
            }

            // always necessary
            response.addHeader("Access-Control-Allow-Origin", originHeader);
            // Cookies are allowed
            response.addHeader("Access-Control-Allow-Credentials", "true");

            if (isPreFlightRequest) {
                return;
            }
        }

        // if preflight return immediately a response ?!
        filterChain.doFilter(request, response);
    }

    private boolean isAllowedHost(String host) {
        for (String allowedHost : allowedHosts) {
            if (allowedHost.toLowerCase().equals(host.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    //TODO: refactor to commonn method, because hasValidRequestMethods does the same
    private boolean hasValidRequestHeaders(HttpServletRequest request, String[] allowedHeaders) {
        String requestHeader = request.getHeader("Access-Control-Request-Headers");

        if (requestHeader != null) {
            String[] requestHeadersParts = requestHeader.trim().toLowerCase().split(", ");
            for (String headerPart : requestHeadersParts) {
                boolean containsHeader = Arrays.asList(allowedHeaders).contains(headerPart);
                if (containsHeader) {

                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasValidRequestMethods(HttpServletRequest request, String[] allowedMethods) {
        if (request.getHeader("Access-Control-Request-Method") != null) {
            String[] requestMethods = request.getHeader("Access-Control-Request-Method").trim().toLowerCase().split(", ");
            for (String method : requestMethods) {
                boolean containsMethod = Arrays.asList(allowedMethods).contains(method);
                if (containsMethod) {

                    return true;
                }
            }
        }
        return false;
    }
}
