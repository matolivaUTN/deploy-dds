
function validarFormulario() {
    const usuarioInput = document.getElementById("usuario");
    const contrasenaInput = document.getElementById("contrasena");
    const usuarioError = document.getElementById("usuario-error");
    const contrasenaError = document.getElementById("contrasena-error");

    usuarioError.textContent = "";
    contrasenaError.textContent = "";

    let isValid = true;

    if (usuarioInput.value.trim() === "") {
        usuarioError.textContent = "Falta ingresar el usuario";
        usuarioInput.style.borderColor = "red";
        isValid = false;
    } else {
        usuarioInput.style.borderColor = "";
    }

    if (contrasenaInput.value.trim() === "") {
        contrasenaError.textContent = "Falta ingresar la contraseña";
        contrasenaInput.style.borderColor = "red";
        isValid = false;
    } else {
        contrasenaInput.style.borderColor = "";
    }

    if (isValid) {
        // Si el formulario es válido, enviarlo
        document.getElementById("loginForm").submit();
    }
}
