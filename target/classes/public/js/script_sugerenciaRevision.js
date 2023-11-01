
    const modoOscuroButton = document.getElementById('modo-oscuro');
    const body = document.body;
    const logoImg = document.querySelector('.navbar-logo img');

    modoOscuroButton.addEventListener('click', () => {
        body.classList.toggle('modo-oscuro');
        modoOscuroButton.classList.toggle('active');

        // Cambiamos la imagen del logo en modo oscuro
        if (body.classList.contains('modo-oscuro')) {
            logoImg.src = '/imagenes/logo_negro.png'; // Ruta de la imagen del logo en modo oscuro
        } else {
            logoImg.src = '/imagenes/logo_blanco.png'; // Ruta de la imagen del logo en modo claro
        }

    });

    function cambiarEstadoIncidente() {
    // Supongamos que el sistema proporciona automáticamente el ID del incidente
    var incidenteId = "Incidente123"; // Reemplaza esto con la lógica real para obtener el ID
    
    // Supongamos que el sistema también proporciona automáticamente la descripción del incidente
    var descripcionIncidente = "Descripción del incidente"; // Reemplaza esto con la lógica real para obtener la descripción
    
    // Aquí puedes agregar la lógica para cambiar el estado del incidente en tu sistema.
    // Por ejemplo, puedes hacer una solicitud a un servidor o actualizar una base de datos.

    // Actualiza la información del incidente en la página
    document.getElementById("incidenteInfo").textContent = incidenteId;
    document.getElementById("descripcionIncidente").textContent = descripcionIncidente;

    // Actualiza el estado actual en la página (esto es solo un ejemplo)
    document.getElementById("estadoActual").textContent = "Estado Actual: Resuelto";
}
