package com.checkme.CheckMe.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "CheckMe",
                        email = "jolmer@gmail.com",
                        url = "https://jolmer.me"
                ),
                title = "CheckMe API",
                version = "1.0.0",
                description = "CheckMe API Documentation",
                license = @License(name = "Apache 2.0", url = "http://foo.bar")
        ),
        servers = {
                @Server(
                        description = "Local Server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production Server",
                        url = "https://api.jolmer.me"
                )
        }
)
public class OpenAPIConfig {
}
