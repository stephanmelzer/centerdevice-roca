package de.centerdevice.roca.config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.web.filter.OncePerRequestFilter;

public class ResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponse responseWrapper = loggingResponseWrapper(response);
        filterChain.doFilter(request, responseWrapper);
    }

    private HttpServletResponse loggingResponseWrapper(HttpServletResponse response) {
        return new HttpServletResponseWrapper(response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return new DelegatingServletOutputStream(
                        new TeeOutputStream(super.getOutputStream(), loggingOutputStream()));
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return new CopyPrintWriter(super.getWriter());
            }
        };
    }

    private OutputStream loggingOutputStream() {
        return System.err;
    }
}

class CopyPrintWriter extends PrintWriter {

    private StringBuilder copy = new StringBuilder();

    public CopyPrintWriter(Writer writer) {
        super(writer);
    }

    @Override
    public void write(int c) {
        copy.append((char) c); // It is actually a char, not an int.
        System.out.append((char) c);
        super.write(c);
    }

    @Override
    public void write(char[] chars, int offset, int length) {
        copy.append(chars, offset, length);
        System.out.println(chars);
        super.write(chars, offset, length);
    }

    @Override
    public void write(String string, int offset, int length) {
        copy.append(string, offset, length);
        System.out.print(string);
        super.write(string, offset, length);
    }

    public String getCopy() {
        return copy.toString();
    }
}