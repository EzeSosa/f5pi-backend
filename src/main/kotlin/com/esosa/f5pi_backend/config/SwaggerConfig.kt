package com.esosa.f5pi_backend.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info = Info(
        title = "Specification - f5pi backend",
        description = "Allows users to register their football games, teams, players with their statistics and fields.",
        version = "1.0.0",
    ),
    servers = [
        Server(
            description = "LOCAL ENV",
            url = "http://localhost:8080/"
        )
    ],
    security = [
        SecurityRequirement(
            name = "BearerAuthorization"
        )
    ]
)
@SecurityScheme(
    name = "BearerAuthorization",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    `in` = SecuritySchemeIn.HEADER
)
class SwaggerConfig