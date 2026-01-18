package com.example.transactionservice.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class SoapConfig {

    // Définit l'URL de base du service SOAP
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        // Le service sera accessible sur http://localhost:8083/ws/*
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    // Génère le fichier WSDL dynamiquement
    @Bean(name = "transactions")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema transactionsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("TransactionPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://example.com/transactionservice");
        wsdl11Definition.setSchema(transactionsSchema);
        return wsdl11Definition;
    }

    // Pointe vers le fichier XSD
    @Bean
    public XsdSchema transactionsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/transaction.xsd"));
    }
}