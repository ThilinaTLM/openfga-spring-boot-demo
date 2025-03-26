package com.openfgademo.api.net.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
@RequestMapping("/docs")
public class DocsController {

    @RequestMapping(value = {"/scalar"}, method = {RequestMethod.GET})
    public void renderHtml(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = "Scalar API Reference";
        String scheme = request.getScheme(); // http or https
        String serverName = request.getServerName(); // hostname or IP
        String serverPort = (request.getServerPort() != 80 && request.getServerPort() != 443) ? ":" + request.getServerPort() : "";
        String specUrl = scheme + "://" + serverName + serverPort + "/docs/api";
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>" + title + "</title>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <script id=\"api-reference\" data-url=\"" + specUrl + "\"></script>\n" +
                "    <script>\n" +
                "      var configuration = {\n" +
                "        theme: \"purple\",\n" +
                "      };\n" +
                "      document.getElementById(\"api-reference\").dataset.configuration =\n" +
                "        JSON.stringify(configuration);\n" +
                "    </script>\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/@scalar/api-reference\"></script>\n" +
                "  </body>\n" +
                "</html>";
        response.setContentType("text/html");
        response.getWriter().write(html);
    }
}
