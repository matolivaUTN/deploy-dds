$(document).ready(function() {
    $("#boton").click(function() {

        // Gather values from the form
        var opcionValue = $("#opcion").val();
        var comunidadIncidenteValue = $("#comunidad-incidente").val();
        var tipoIncidenteValue = $("input[name='tipo-incidente']:checked").val();

        // Perform AJAX request with gathered values
        $.ajax({
            url: "/incidentes-de-comunidad",
            method: "GET",
            data: {
                opcion: opcionValue,
                comunidadIncidente: comunidadIncidenteValue,
                tipoIncidente: tipoIncidenteValue
            },
            success: function(response) {
                // Handle the successful response here
                $(".table-container").html(response);
            },
            error: function(error) {
                // Handle the error here
                console.error("Error:", error);
            }
        });
    });
});

