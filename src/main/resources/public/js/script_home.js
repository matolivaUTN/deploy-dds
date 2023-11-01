
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
