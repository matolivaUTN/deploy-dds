$("#comunidad-incidente").on("change", function() {
    var selectedValue = $(this).val();

    // Realizar una solicitud AJAX al servidor para obtener opciones para el campo de incidentes
    $.get("/obtenerIncidentesComunidad?valor=" + selectedValue, function(data) {
        // Actualizar el segundo select con la respuesta HTML del servidor
        $("#incidente").html(data);
    });
});

