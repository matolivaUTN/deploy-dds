$("#provincia").on("change", function() {
    var selectedValue = $(this).val();

    // Realizar la primera solicitud AJAX para obtener departamentos
    $.get("/obtener-departamentos-provincia?valor=" + selectedValue, function(data) {
        // Actualizar el primer select con la respuesta HTML del servidor
        $("#departamento").html(data);

        // Realizar la segunda solicitud AJAX para obtener municipios
        $.get("/obtener-municipios-provincia?valor=" + selectedValue, function(municipiosData) {
            // Actualizar el segundo select con la respuesta HTML del servidor
            $("#municipio").html(municipiosData);
        });
    });
});
