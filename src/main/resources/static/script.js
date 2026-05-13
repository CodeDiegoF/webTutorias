// Referencias a elementos del DOM usados por la vista de alumno.
const horariosContainer = document.getElementById("horarios-container");
const reservaForm       = document.getElementById("reserva-form");
const fechaInput        = document.getElementById("fecha");
const horaInput         = document.getElementById("hora");

/**
 * Carga los horarios disponibles y permite seleccionar uno para precargar
 * los campos de fecha y hora del formulario de reserva.
 */
async function cargarHorarios() {
    const response = await fetch("http://localhost:8080/horarios");
    const horarios = await response.json();

    horariosContainer.innerHTML = "";

    horarios.forEach(horario => {
        const div = document.createElement("div");
        div.classList.add("horario-slot", "col-md-3", "col-6");
        div.innerHTML = `
            <strong>${horario.fecha}</strong><br>
            ${horario.hora}
        `;
        div.addEventListener("click", () => {
            fechaInput.value = horario.fecha;
            horaInput.value  = horario.hora;
        });
        horariosContainer.appendChild(div);
    });
}

/**
 * Consulta todas las reservas y pinta solo las del email indicado.
 *
 * @param {string} emailAlumno correo del alumno autenticado en el formulario.
 */
async function cargarMisReservas(emailAlumno) {
    if (!emailAlumno) return;

    const response = await fetch("http://localhost:8080/reservas");
    const reservas = await response.json();

    const misReservas = reservas.filter(r => r.emailAlumno === emailAlumno);
    const container   = document.getElementById("mis-reservas-container");
    container.innerHTML = "";

    if (!misReservas.length) {
        container.innerHTML = "<p>No tienes tutorías reservadas.</p>";
        return;
    }

    misReservas.forEach(reserva => {
        const div = document.createElement("div");
        div.classList.add("reserva-item", "mb-2");
        div.innerHTML = `
            <div class="d-flex justify-content-between align-items-center gap-3">
                <div>
                    <strong>${reserva.nombreAlumno}</strong>
                </div>
                <div style="font-size:.85rem; color:#aaa">
                    📅 ${reserva.fecha} &nbsp; 🕐 ${reserva.hora}
                </div>
                <button class="btn btn-sm btn-danger flex-shrink-0"
                    onclick="cancelarReserva(${reserva.id})">Cancelar</button>
            </div>
        `;
        container.appendChild(div);
    });
}

// Envio de reserva: valida en backend que el horario exista y este disponible.
reservaForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("emailAlumno").value.trim();

    const reserva = {
        nombreAlumno: document.getElementById("nombreAlumno").value.trim(),
        emailAlumno:  email,
        fecha: fechaInput.value.trim(),
        hora:  horaInput.value.trim()
    };

    const response = await fetch("http://localhost:8080/reservas", {
        method: "POST",
        headers: {
            "Accept":       "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reserva)
    });

    if (response.ok) {
        sessionStorage.setItem("emailAlumno", email);
        alert("Reserva realizada");
        reservaForm.reset();
        await cargarHorarios()
        await cargarMisReservas(email);
    } else {
        const error = await response.text();
        alert(error);
    }
});

async function cancelarReserva(id) {
    const response = await fetch(`http://localhost:8080/reservas/${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        await cargarHorarios();
        await cargarMisReservas(sessionStorage.getItem('emailAlumno'));
    } else {
        alert("Error al cancelar la reserva.");
    }
}

// Carga inicial de horarios al abrir la vista.
cargarHorarios().then(() => console.log("Horarios cargados"));
cargarMisReservas(emailGuardado).then(() => console.log("Reservas Cargadas"));
const emailGuardado = sessionStorage.getItem('emailAlumno');
if (emailGuardado) {
    cargarMisReservas(emailGuardado).then(() => console.log("Reservas Cargadas"));
}