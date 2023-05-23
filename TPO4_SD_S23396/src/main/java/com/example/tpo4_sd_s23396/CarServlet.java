package com.example.tpo4_sd_s23396;

import java.io.*;
import java.util.Date;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "carServlet", value = "/car-servlet")
public class CarServlet extends HttpServlet {
    private String message;
    private PrintWriter out;

    public void init() throws ServletException {
        message = "Hello World!";
        ServletConfig config = getServletConfig();

        if(config == null) {
            message += " This servlet doesn't work.";
        }

        // uzyskanie kontekstu i dostęp do niego
        ServletContext context = config.getServletContext();

    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("");

        out = response.getWriter();
        out.println(message);
        out.println("obsluga zlecenia przez metode serviceRequest " + new Date());
        super.service(request, response);
        out.close();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        out = response.getWriter();
        out.write("Hi");
        out.println("Wywolana metoda doGet " + new Date());


        out.write("</body>\n</html>");
    }

    public void destroy() {
    }
}