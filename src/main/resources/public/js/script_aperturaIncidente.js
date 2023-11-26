$("#entidad").on("change", function() {
    var selectedValue = $(this).val();

    // Reset #prestacion-afectada options
    $("#prestacion-afectada").empty();
    $("#prestacion-afectada").append('<option value="" disabled selected>Seleccione una prestación</option>');

    // Realizar una solicitud AJAX al servidor para obtener opciones para el campo de incidentes
    $.get("/obtenerEstablecimientosIncidente?valor=" + selectedValue, function(data) {
        // Actualizar el segundo select con la respuesta HTML del servidor
        $("#establecimiento").html(data);
    });
});

// Event handler for #establecimiento change
$("#establecimiento").on("change", function() {
    var selectedEstablecimiento = $(this).val();

    // Reset #prestacion-afectada options before making the AJAX request
    $("#prestacion-afectada").empty();
    $("#prestacion-afectada").append('<option value="" disabled selected>Seleccione una prestación</option>');

    // Realizar la segunda solicitud AJAX para las prestaciones de ese establecimiento que estén disponibles
    $.get("/obtenerPrestacionesEstablecimiento?valor=" + selectedEstablecimiento, function(prestacionData) {
        // Actualizar el tercer select con la respuesta HTML del servidor
        $("#prestacion-afectada").html(prestacionData);
    });
});
