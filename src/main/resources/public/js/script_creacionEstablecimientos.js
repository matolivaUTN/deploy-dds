$("#entidad").on("change", function() {
    var entidad = $("#entidad").val();

    // Realizar la primera solicitud AJAX para obtener departamentos
    $.get("/obtener-provincia-entidad?valor=" + entidad, function(data) {
        // Actualizar el primer select con la respuesta HTML del servidor
        $("#provincia").html(data);


        const provincia = document.getElementById("provincia").value;


        // Realizar la primera solicitud AJAX para obtener departamentos
        $.get("/obtener-departamentos-provincia?valor=" + provincia, function(data) {
            // Actualizar el primer select con la respuesta HTML del servidor
            $("#departamento").html(data);

            // Realizar la segunda solicitud AJAX para obtener municipios
            $.get("/obtener-municipios-provincia?valor=" + provincia, function(municipiosData) {
                // Actualizar el segundo select con la respuesta HTML del servidor
                $("#municipio").html(municipiosData);
            });
        });
    });

    // Realizar la segunda solicitud AJAX para obtener municipios
    $.get("/obtener-localizacion?valor=" + entidad, function(localizacionData) {
        // Actualizar el segundo select con la respuesta HTML del servidor
        $("#localizacion-container").html(localizacionData);




    });
});


