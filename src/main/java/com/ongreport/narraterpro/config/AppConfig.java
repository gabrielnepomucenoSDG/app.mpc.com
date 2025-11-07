package com.ongreport.narraterpro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    // Injeta o valor da propriedade 'spring.application.name'
    @Value("${spring.application.name}")
    private String nomeAplicacao;

    // Injeta o valor de uma propriedade customizada 'app.version'
    @Value("${app.version}")
    private String versionAplicacao;

    // Métodos Getters para que outras partes da aplicação possam ler esses valores
    public String getNomeAplicacao() {
        return nomeAplicacao;
    }

    public String getVersionAplicacao() {
        return versionAplicacao;
    }
}