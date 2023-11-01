
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

document.addEventListener("DOMContentLoaded", function () {
    const establecimientoSelect = document.getElementById("establecimiento");
    const prestacionSelect = document.getElementById("prestacion");
    const observacionesTextarea = document.getElementById("observaciones");
    const verificacion = document.getElementById("verificacion"); // Elemento de verificación

    establecimientoSelect.addEventListener("change", function () {
      // Habilitar el select de prestación cuando se selecciona un establecimiento
      prestacionSelect.disabled = false;
    });

    const incidenteForm = document.getElementById("incidenteForm");
    incidenteForm.addEventListener("submit", function (event) {
      event.preventDefault();

      const establecimientoValue = establecimientoSelect.value;
      const prestacionValue = prestacionSelect.value;
      const observacionesValue = observacionesTextarea.value;

      // Validación de campos (puedes personalizar según tus necesidades)
      if (establecimientoValue && prestacionValue && observacionesValue) {
        // Mostrar el mensaje de verificación solo una vez
        verificacion.innerText = "¡Formulario enviado correctamente!";
        verificacion.classList.remove("hidden");

        // Puedes realizar aquí cualquier otra acción que desees con los datos del formulario
      }
    });
  });