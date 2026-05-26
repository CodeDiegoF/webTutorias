const horarioForm       = document.getElementById("horario-form");
const fechaInput        = document.getElementById("fecha");
const horaInput         = document.getElementById("hora");
const horariosContainer = document.getElementById("horarios-container");
const reservasContainer = document.getElementById("reservas-container");

// ── Variables globales ────────────────────────────────────────────────────────
let todosLosHorarios  = [];
let todasLasReservas  = [];
let todosLasHistorial = [];

// ── Cargar horarios ───────────────────────────────────────────────────────────
/**
 * Obtiene todos los horarios para el panel admin y muestra su estado actual,
 * incluyendo filtrado
 */
async function cargarHorarios() {
    const response = await fetch("/horarios/admin");
    todosLosHorarios = await response.json();
    renderHorarios(todosLosHorarios);
}

function filtrarHorarios(tipo) {
    const hoy   = new Date().toISOString().split('T')[0];
    const ahora = new Date().toTimeString().slice(0, 5);

    let filtrados;
    if (tipo === 'libre')
        filtrados = todosLosHorarios.filter(h => h.disponible &&
            (h.fecha > hoy || (h.fecha === hoy && h.hora.slice(0, 5) >= ahora)));
    else if (tipo === 'ocupado')
        filtrados = todosLosHorarios.filter(h => !h.disponible &&
            (h.fecha > hoy || (h.fecha === hoy && h.hora.slice(0, 5) >= ahora)));
    else if (tipo === 'pasado')
        filtrados = todosLosHorarios.filter(h =>
            h.fecha < hoy || (h.fecha === hoy && h.hora.slice(0, 5) < ahora));
    else
        filtrados = todosLosHorarios;
    renderHorarios(filtrados);
}

function renderHorarios(horarios) {
    horariosContainer.innerHTML = "";

    if (!horarios.length) {
        horariosContainer.innerHTML = "<p style='color:#888'>No hay horarios.</p>";
        return;
    }

    const hoy   = new Date().toISOString().split('T')[0];
    const ahora = new Date().toTimeString().slice(0, 5);

    horarios.forEach(horario => {
        const esPasado = horario.fecha < hoy ||
            (horario.fecha === hoy && horario.hora.slice(0, 5) < ahora);

        const div = document.createElement("div");
        div.classList.add("horario-slot", "col-md-4", "col-6");
        div.innerHTML = `
            <strong>${horario.fecha}</strong> — ${horario.hora}
            <span class="${esPasado ? 'badge-ocupado' : (horario.disponible ? 'badge-libre' : 'badge-ocupado')}">
                ${esPasado ? '🕓 Pasado' : (horario.disponible ? '✅ Libre' : '❌ Ocupado')}
            </span>
            <button class="btn-eliminar" onclick="eliminarHorario(${horario.id})">Eliminar</button>
        `;
        horariosContainer.appendChild(div);
    });
}

// ── Cargar reservas ───────────────────────────────────────────────────────────
/**
 * Carga y pinta las reservas realizadas por los alumnos.
 */
async function cargarReservas() {
    const response = await fetch("/reservas");
    const todas = await response.json();

    const hoy   = new Date().toISOString().split('T')[0];
    const ahora = new Date().toTimeString().slice(0, 5);

    // Solo reservas futuras o de hoy cuya hora aún no ha pasado
    todasLasReservas = todas.filter(r =>
        r.fecha > hoy || (r.fecha === hoy && r.hora.slice(0, 5) > ahora)
    );
    renderReservas(todasLasReservas);
}

function renderReservas(reservas) {
    reservasContainer.innerHTML = "";

    if (!reservas.length) {
        reservasContainer.innerHTML = "<p>No hay reservas todavía.</p>";
        return;
    }

    reservas.forEach(reserva => {
        const div = document.createElement("div");
        div.classList.add("reserva-item", "row-md-4", "row-3");
        div.innerHTML = `
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <strong>${reserva.nombreAlumno}</strong>
                </div>
                <div style="font-size:.85rem; color:#aaa">
                    📅 ${reserva.fecha} &nbsp; 🕐 ${reserva.hora}
                </div>
            </div>
        `;
        reservasContainer.appendChild(div);
    });
}

document.getElementById('buscador-reservas').addEventListener('input', function() {
    const texto = this.value.toLowerCase().trim();
    const filtradas = todasLasReservas.filter(r =>
        (r.nombreAlumno || '').toLowerCase().includes(texto)
    );
    renderReservas(filtradas);
});

// ── Crear horario ─────────────────────────────────────────────────────────────
/**
 * Maneja el envío del formulario para publicar un horario disponible.
 */
horarioForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const horario = {
        fecha: fechaInput.value.trim(),
        hora: horaInput.value.trim(),
        disponible: true
    };

    const result = await Swal.fire({
        title: "¿Confirmar horario?",
        text: `Horario el ${horario.fecha} a las ${horario.hora}`,
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#0d6efd",
        cancelButtonColor: "#dc3545",
        confirmButtonText: "Confirmar",
        cancelButtonText: "Cancelar"
    });

    if (!result.isConfirmed) return;

    const response = await fetch("/horarios", {
        method: "POST",
        headers: {
            "Accept":       "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(horario)
    });

    if (response.ok) {

        Swal.fire({
            title: "¡Horario creado!",
            text: "Se ha creado el horario con éxito.",
            icon: "success",
            confirmButtonColor: "#0d6efd",
            confirmButtonText: "Aceptar"
        });

        horarioForm.reset();
        await cargarHorarios();
    } else {
        const error = await response.text();
        Swal.fire({
            title: "Error",
            text: error,
            icon: "error",
            confirmButtonColor: "#dc3545"
        });
    }
});

// ── Eliminar horario ──────────────────────────────────────────────────────────
/**
 * Elimina un horario por id desde el panel administrativo.
 *
 * @param {number} id identificador del horario a eliminar.
 */
async function eliminarHorario(id) {

    const result = await Swal.fire({
        title: "¿Eliminar horarío?",
        text: "¿Estás seguro de que quieres eliminar este horario?",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#0d6efd",
        cancelButtonColor: "#dc3545",
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Mejor no"
    });

    if (!result.isConfirmed) return;

    const response = await fetch(`/horarios/${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        Swal.fire({
            title: "¡Horario eliminado con éxito!",
            text: "El horario ha sido eliminado correctamente.",
            icon: "success",
            confirmButtonColor: "#0d6efd",
            confirmButtonText: "Aceptar"
        });
        await cargarHorarios();
        await cargarReservas();
    } else {
        const error = await response.text();

        Swal.fire({
            title: "Error",
            text: error,
            icon: "error",
            confirmButtonColor: "#dc3545"
        });
    }
}

// ── Cargar historial ──────────────────────────────────────────────────────────
/**
 * Carga el historial de tutorías pasadas, mostrando solo las reservas que ya han ocurrido.
 * Permite filtrar por nombre del alumno.
 */


async function cargarHistorial() {
    const response = await fetch("/reservas/historial");
    todosLasHistorial = await response.json();
    renderHistorial(todosLasHistorial);
}

function renderHistorial(reservas) {
    const container = document.getElementById("historial-container");
    container.innerHTML = "";

    if (!reservas.length) {
        container.innerHTML = "<p style='color:#888'>No hay tutorías pasadas.</p>";
        return;
    }

    reservas.forEach(reserva => {
        const div = document.createElement("div");
        div.classList.add("reserva-item");
        div.innerHTML = `
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <strong>${reserva.nombreAlumno || 'Alumno'}</strong>
                    <div style="font-size:.82rem;color:#aaa;margin-top:.2rem">
                        📅 ${reserva.fecha} &nbsp; 🕐 ${reserva.hora}
                    </div>
                </div>
                <span class="badge-ocupado">Pasada</span>
            </div>
        `;
        container.appendChild(div);
    });
}

document.getElementById('buscador-reservas-historial').addEventListener('input', function() {
    const texto = this.value.toLowerCase().trim();
    const filtradas = todosLasHistorial.filter(r =>
        (r.nombreAlumno || '').toLowerCase().includes(texto)
    );
    renderHistorial(filtradas);
});

// ── Carga inicial ─────────────────────────────────────────────────────────────
cargarHorarios().then(() => console.log("Horarios cargados"));
cargarReservas().then(() => console.log("Reservas Cargadas"));
cargarHistorial().then(() => console.log("Historial cargado"));