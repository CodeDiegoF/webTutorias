// Obtener el token de la URL
const urlParams = new URLSearchParams(window.location.search);
const token = urlParams.get('token');

// Si no hay token redirigir al login
if (!token) {
    window.location.href = '/login.html';
}

function mostrarError(mensaje) {
    let err = document.querySelector('.error-msg');
    if (!err) {
        err = document.createElement('p');
        err.classList.add('error-msg');
        err.style.cssText = 'color:#e55;font-size:13px;margin-top:8px;text-align:center';
        document.getElementById('reset-form').appendChild(err);
    }
    err.textContent = mensaje;
}

document.getElementById('reset-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const nuevaPassword    = document.getElementById('nueva-password').value;
    const confirmarPassword = document.getElementById('confirmar-password').value;

    if (nuevaPassword !== confirmarPassword) {
        mostrarError('Las contraseñas no coinciden.');
        return;
    }

    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;

    if (!passwordRegex.test(nuevaPassword)) {
        mostrarError('La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.');
        return;
    }

    try {
        const res = await fetch('/auth/reset', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ token, password: nuevaPassword }),
            credentials: "include"
        });

        if (res.ok) {
            alert('Contraseña actualizada correctamente. Ahora puedes iniciar sesión.');
            window.location.href = '/login.html';
        } else {
            const msg = await res.text();
            mostrarError(msg || 'El enlace ha expirado o es inválido.');
        }
    } catch {
        mostrarError('Error de conexión.');
    }
});