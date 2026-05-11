const horarioForm       = document.getElementById("horario-form");
const fechaInput        = document.getElementById("fecha");
const horaInput         = document.getElementById("hora");
const horariosContainer = document.getElementById("horarios-container");
const reservasContainer = document.getElementById("reservas-container");

// ── Cargar horarios ───────────────────────────────────────────────────────────
/**
 * Obtiene todos los horarios para el panel admin y muestra su estado actual.
 */
async function cargarHorarios() {
    const response = await fetch("http://localhost:8080/horarios/admin");
    const horarios = await response.json();

    horariosContainer.innerHTML = "";

    if (!horarios.length) {
        horariosContainer.innerHTML = "<p>No hay horarios creados.</p>";
        return;
    }

    horarios.forEach(horario => {
        const div = document.createElement("div");
        div.classList.add("horario");
        div.innerHTML = `
            <strong>${horario.fecha}</strong> — ${horario.hora}
            <span>${horario.disponible ? "✅ Libre" : "❌ Ocupado"}</span>
            <button onclick="eliminarHorario(${horario.id})">Eliminar</button>
        `;
        horariosContainer.appendChild(div);
    });
}

// ── Cargar reservas ───────────────────────────────────────────────────────────
/**
 * Carga y pinta las reservas realizadas por los alumnos.
 */
async function cargarReservas() {
    const response = await fetch("http://localhost:8080/reservas");
    const reservas = await response.json();

    reservasContainer.innerHTML = "";

    if (!reservas.length) {
        reservasContainer.innerHTML = "<p>No hay reservas todavía.</p>";
        return;
    }

    reservas.forEach(reserva => {
        const div = document.createElement("div");
        div.classList.add("horario");
        div.innerHTML = `
            <strong>${reserva.nombreAlumno}</strong>
            <br>
            ${reserva.fecha} — ${reserva.hora}
        `;
        reservasContainer.appendChild(div);
    });
}

// ── Crear horario ─────────────────────────────────────────────────────────────
/**
 * Maneja el envio del formulario para publicar un horario disponible.
 */
horarioForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const horario = {
        fecha: fechaInput.value.trim(),
        hora: horaInput.value.trim(),
        disponible: true
    };

    const response = await fetch("http://localhost:8080/horarios", {
        method: "POST",
        headers: {
            "Accept":       "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(horario)
    });

    if (response.ok) {
        alert("Horario creado");
        horarioForm.reset();
        await cargarHorarios();
    } else {
        const error = await response.text();
        alert("Error: " + error);
    }
});

// ── Eliminar horario ──────────────────────────────────────────────────────────
/**
 * Elimina un horario por id desde el panel administrativo.
 *
 * @param {number} id identificador del horario a eliminar.
 */
async function eliminarHorario(id) {
    const response = await fetch(`http://localhost:8080/horarios/${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        await cargarHorarios();
        await cargarReservas();
    } else {
        alert("Error al eliminar el horario.");
    }
}

// ── Carga inicial ─────────────────────────────────────────────────────────────
cargarHorarios().then(console.log("Horarios cargados"));
cargarReservas().then(console.log("Reservas Cargadas") );