const modoOscuroButton = document.getElementById('modo-oscuro');
const body = document.body;
const logoImg = document.querySelector('.navbar-logo img');

// Check the user's last mode preference from local storage
const savedMode = localStorage.getItem('modoOscuro');
if (savedMode === 'true') {
    body.classList.add('modo-oscuro');
    modoOscuroButton.classList.add('active');
    logoImg.src = '/imagenes/logo_negro.png'; // Set dark mode logo
} else {
    body.classList.remove('modo-oscuro');
    modoOscuroButton.classList.remove('active');
    logoImg.src = '/imagenes/logo_blanco.png'; // Set light mode logo
}

modoOscuroButton.addEventListener('click', () => {
    body.classList.toggle('modo-oscuro');
    modoOscuroButton.classList.toggle('active');

    // Save the current mode preference to local storage
    const isModoOscuro = body.classList.contains('modo-oscuro');
    localStorage.setItem('modoOscuro', isModoOscuro);

    // Change the logo based on the selected mode
    if (isModoOscuro) {
        logoImg.src = '/imagenes/logo_negro.png'; // Set dark mode logo
    } else {
        logoImg.src = '/imagenes/logo_blanco.png'; // Set light mode logo
    }
});

