
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

    function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " active";
}

document.getElementById("listado").style.display = "block"; // Para mostrar la pestaña "Listado de Incidentes" por defecto