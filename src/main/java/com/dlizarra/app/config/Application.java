package com.dlizarra.app.config;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import javax.xml.ws.Endpoint;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.dlizarra.app.ws.AppImpl;

@SpringBootApplication
public class Application {
	
	public static final String SERVLET_MAPPING_URL_PATH = "/soap/*";
	public static final String SERVICE_NAME_URL_PATH = "/app";
	
	@Autowired
	private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public ServletRegistrationBean dispatcherServlet() {
    	return new ServletRegistrationBean(new CXFServlet(), SERVLET_MAPPING_URL_PATH);
    }
    
    
    @Bean(name=Bus.DEFAULT_BUS_ID)
    // <bean id="cxf" class="org.apache.cxf.bus.spring.SpringBus">
    public SpringBus springBus() {    	
    	return new SpringBus();
    }
    
    @Bean
    // <jaxws:endpoint id="app" implementor="com.dlizarra.app.ws.AppImpl" address="/app">
    public Endpoint app() {
        Bus bus = (Bus) applicationContext.getBean(Bus.DEFAULT_BUS_ID);
        Object implementor = new AppImpl();
        EndpointImpl endpoint = new EndpointImpl(bus, implementor);        
        endpoint.publish(SERVICE_NAME_URL_PATH);
        return endpoint;
    }    
    
}
