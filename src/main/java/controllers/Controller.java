package controllers;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import models.entities.comunidad.Miembro;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.Objects;

public class Controller implements WithSimplePersistenceUnit {
    protected Miembro miembroLogueado(Context ctx, EntityManager entityManager) {
        if(ctx.cookie("id_miembro") == null)
            return null;
        return entityManager
                .find(Miembro.class, Long.parseLong(ctx.cookie("id_miembro")));
    }

    public void cargarRolesAModel(Context context, Map<String, Object> model) {
        model.put("rol", context.cookie("tipo_rol"));
    }
}