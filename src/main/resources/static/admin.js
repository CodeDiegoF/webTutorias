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
        div.classList.add("horario-slot", "col-md-3", "col-6");
        div.innerHTML = `
            <strong>${horario.fecha}</strong> — ${horario.hora}
            <span>${horario.disponible ? "✅ Libre" : "❌ Ocupado"}</span>
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
    const response = await fetch("http://localhost:8080/reservas");
    const reservas = await response.json();

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

    const response = await fetch("http://localhost:8080/horarios", {
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

    const response = await fetch(`http://localhost:8080/horarios/${id}`, {
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

// ── Carga inicial ─────────────────────────────────────────────────────────────
cargarHorarios().then(() => console.log("Horarios cargados"));
cargarReservas().then(() => console.log("Reservas Cargadas"));