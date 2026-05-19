const container   = document.querySelector('.container');
const registerBtn = document.getElementById('sign-up-btn');
const loginBtn    = document.getElementById('sign-in-btn');

// ── Animación del panel ──────────────────────────────────────────────────────
registerBtn.addEventListener('click', () => container.classList.add("toggle"));
loginBtn.addEventListener('click',    () => container.classList.remove("toggle"));

// ── Utilidad: mostrar error bajo el formulario ───────────────────────────────
function mostrarError(form, mensaje) {
    let err = form.querySelector('.error-msg');
    if (!err) {
        err = document.createElement('p');
        err.classList.add('error-msg');
        err.style.cssText = 'color:#e55;font-size:13px;margin-top:8px;text-align:center';
        form.appendChild(err);
    }
    err.textContent = mensaje;
}

function limpiarError(form) {
    const err = form.querySelector('.error-msg');
    if (err) err.textContent = '';
}

// ── REGISTRO ──────────────────────────────────────────────────────────────────
const signUpForm = document.querySelector('.sign-up');

signUpForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    limpiarError(signUpForm);

    const nombre   = document.getElementById('registro-nombre').value.trim();
    const email    = document.getElementById('registro-email').value.trim();
    const password = document.getElementById('registro-password').value;

    if (!nombre || !email || !password) {
        mostrarError(signUpForm, 'Rellena todos los campos.');
        return;
    }

    try {
        const res = await fetch('/auth/registro', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombre, email, password })
        });

        if (res.ok) {
            alert('¡Cuenta creada! Ahora inicia sesión.');
            container.classList.remove('toggle');
            document.getElementById('login-email').value = email;
        } else {
            const msg = await res.text();
            mostrarError(signUpForm, msg || 'Error al registrarse.');
        }
    } catch {
        mostrarError(signUpForm, 'Error de conexión.');
    }
});

// ── LOGIN ─────────────────────────────────────────────────────────────────────
const signInForm = document.querySelector('.sign-in');

signInForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    limpiarError(signInForm);

    const email    = document.getElementById('login-email').value.trim();
    const password = document.getElementById('login-password').value;

    if (!email || !password) {
        mostrarError(signInForm, 'Rellena todos los campos.');
        return;
    }

    try {
        const res = await fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (res.ok) {
            const usuario = await res.json();
            sessionStorage.setItem('usuario', JSON.stringify(usuario));

            // Redirigir según el rol
            if (usuario.rol === 'PROFESOR') {
                window.location.href = '/admin.html';
            } else {
                window.location.href = '/reservaTutorías.html';
            }
        } else {
            const msg = await res.text();
            mostrarError(signInForm, msg || 'Correo o contraseña incorrectos.');
        }
    } catch {
        mostrarError(signInForm, 'Error de conexión.');
    }
});