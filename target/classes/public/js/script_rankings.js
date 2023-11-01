
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
    const btnIncidentes = document.getElementById("btnIncidentes");
    const btnTiempo = document.getElementById("btnTiempo");
    const btnImpacto = document.getElementById("btnImpacto");
    const ranking = document.getElementById("ranking");

    // Función para mostrar el ranking correspondiente al botón presionado
    function mostrarRanking(criterio) {
        // Aquí puedes cargar y mostrar el ranking según el criterio seleccionado
        // Por ejemplo, podrías utilizar una solicitud AJAX para obtener los datos del servidor
        // y luego mostrarlos en la tabla 'ranking-table'.
        // Aquí, se muestra un ejemplo simple con entidades diferentes:
        const rankingTable = ranking.querySelector('.ranking-table tbody');
        rankingTable.innerHTML = ''; // Limpia el contenido anterior

        const entidades = {
            "Mayor cantidad de incidentes reportados en la semana": [
                "Entidad A",
                "Entidad B",
                "Entidad C"
            ],
            "Mayor promedio de tiempo de cierre de incidentes": [
                "Entidad X",
                "Entidad Y",
                "Entidad Z"
            ],
            "Mayor grado de impacto de las problemáticas": [
                "Entidad P",
                "Entidad Q",
                "Entidad R"
            ]
        };

        const rankingData = entidades[criterio];
        rankingData.forEach((entidad, index) => {
            const row = document.createElement("tr");
            const positionCell = document.createElement("td");
            positionCell.textContent = index + 1;
            const entidadCell = document.createElement("td");
            entidadCell.textContent = entidad;
            row.appendChild(positionCell);
            row.appendChild(entidadCell);
            rankingTable.appendChild(row);
        });

        ranking.style.display = "block";
    }

    btnIncidentes.addEventListener("click", () => {
        mostrarRanking("Mayor cantidad de incidentes reportados en la semana");
        resetButtons();
        btnIncidentes.classList.add("selected");
    });

    btnTiempo.addEventListener("click", () => {
        mostrarRanking("Mayor promedio de tiempo de cierre de incidentes");
        resetButtons();
        btnTiempo.classList.add("selected");
    });

    btnImpacto.addEventListener("click", () => {
        mostrarRanking("Mayor grado de impacto de las problemáticas");
        resetButtons();
        btnImpacto.classList.add("selected");
    });

    function resetButtons() {
        btnIncidentes.classList.remove("selected");
        btnTiempo.classList.remove("selected");
        btnImpacto.classList.remove("selected");
    }
});
