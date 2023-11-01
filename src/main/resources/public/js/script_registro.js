$("#provincia").on("change", function() {
    var selectedValue = $(this).val();

    // Realizar la primera solicitud AJAX
    $.get("/obtener-departamentos-provincia?valor=" + selectedValue, function(data) {
        // Actualizar el primer select con la respuesta HTML del servidor
        $("#departamento").html(data);

    });
});

$("#provincia").on("change", function() {
    var selectedValue = $(this).val();

    // Realizar la primera solicitud AJAX
    $.get("/obtener-municipios-provincia?valor=" + selectedValue, function(data) {
        // Actualizar el primer select con la respuesta HTML del servidor
        $("#municipio").html(data);
    });
});

