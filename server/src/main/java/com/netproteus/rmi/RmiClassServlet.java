// Copyright 2014 William Lewis
package com.netproteus.rmi;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


public class RmiClassServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(RmiClassServlet.class);
    
    private static final HashSet<String> unknownClassesWarnOnly = new HashSet<String>();
    static {
        unknownClassesWarnOnly.add("byte.class");
        unknownClassesWarnOnly.add("short.class");
        unknownClassesWarnOnly.add("int.class");
        unknownClassesWarnOnly.add("long.class");
        unknownClassesWarnOnly.add("float.class");
        unknownClassesWarnOnly.add("double.class");
        unknownClassesWarnOnly.add("char.class");
        unknownClassesWarnOnly.add("boolean.class");
        unknownClassesWarnOnly.add("META-INF/PREFERRED.LIST");
    }    
    
    /**
     * @throws ServletException  
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        try {
            String requested = req.getPathInfo().substring(1);
            // XXX: Forcing the use of the system classloader here is peculiar.
            InputStream resource = ClassLoader.getSystemResourceAsStream(requested);
            ServletOutputStream sos = null;
            if (resource != null) {
                resp.setStatus(SC_OK);
                sos = resp.getOutputStream();
                
                byte[] buf = new byte[8192];
                int bytesRead = resource.read(buf);
                while (bytesRead != -1) {
                    sos.write(buf, 0, bytesRead);
                    bytesRead = resource.read(buf);
                }
                sos.flush();            
                
                return;
            }
            
            /*
             * The class was not found. For certain common cases, log as warning, else log as error.
             * 
             * Primitive class requests occur due to some rather suboptimal coding on Sun's part in
             * sun.rmi.server.MarshalInputStream.resolveClass().
             * 
             * META-INF/PREFERRED.LIST is a Jini feature.
             * 
             * Class names suffixed with "BeanInfo" come from java.beans.Introspector.
             */
            if (unknownClassesWarnOnly.contains(requested) || requested.endsWith("BeanInfo.class")) {
                log.warn("RMI request for unknown class: " + requested);
            }
            else {
                log.error("RMI request for unknown class: " + requested);
            }        
        }
        catch (Throwable t) {
            log.error("RMI request failed for URL: " + req.getPathInfo(), t);
        }
        
        resp.setStatus(SC_NOT_FOUND);
        Writer w = resp.getWriter();
        w.append("Not Found");
        w.flush();
        w.close();
    }
    
}