package models.entities.roles;

import io.javalin.security.RouteRole;

public enum TipoRol implements RouteRole {
    ADMINISTRADOR,
    MIEMBRO,
    EMPRESA,
}
