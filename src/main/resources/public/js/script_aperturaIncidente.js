$("#entidad").on("change", function() {
    var selectedValue = $(this).val();

    // Realizar una solicitud AJAX al servidor para obtener opciones para el campo de incidentes
    $.get("/obtenerEstablecimientosIncidente?valor=" + selectedValue, function(data) {
        // Actualizar el segundo select con la respuesta HTML del servidor
        $("#establecimiento").html(data);
    });
});

