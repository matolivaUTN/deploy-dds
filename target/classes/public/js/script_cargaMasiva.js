
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
const fileInput = document.getElementById("fileInput");
const fileNameLabel = document.getElementById("fileNameLabel");
const mensaje = document.getElementById("mensaje");
const cargarButton = document.getElementById("cargarButton");

fileInput.addEventListener("change", function () {
if (fileInput.files.length > 0) {
fileNameLabel.textContent = `Archivo seleccionado: ${fileInput.files[0].name}`;
} else {
fileNameLabel.textContent = "Seleccione un archivo CSV";
}
});

cargarButton.addEventListener("click", function () {
const file = fileInput.files[0];

if (file) {
if (file.name.endsWith(".csv")) {
  mensaje.textContent = "Archivo cargado exitosamente.";
  mensaje.className = "success";
} else {
  mensaje.textContent = "Por favor, seleccione un archivo CSV.";
  mensaje.className = "error";
}
} else {
mensaje.textContent = "Por favor, seleccione un archivo.";
mensaje.className = "error";
}
});
});

