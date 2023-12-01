$("#tipo-servicio").on("change", function() {
    var selectedValue = $(this).val();

    // Realizar una solicitud AJAX al servidor para obtener opciones para el campo de incidentes
    $.get("/mostrarCamposAdicionales?valor=" + selectedValue, function(data) {

        // Actualizar el segundo select con la respuesta HTML del servidor
        $("#informacion-adicional").html(data);
    });
});

