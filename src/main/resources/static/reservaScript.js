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
    const response = await fetch("/horarios");
    const horarios = await response.json();

    horariosContainer.innerHTML = "";

    horarios.forEach(horario => {
        const div = document.createElement("div");
        div.classList.add("horario-slot", "col-md-3", "col-6");
        div.innerHTML = `
            <strong>${horario.fecha}</strong><br>
            <strong>${horario.hora}</strong>
        `;
        div.addEventListener("click", () => {
            // Quitar selección anterior
            document.querySelectorAll(".horario-slot").forEach(s =>
                s.classList.remove("horario-slot-seleccionado")
            );

            // Marcar este como seleccionado
            fechaInput.value = horario.fecha;
            horaInput.value  = horario.hora;
            div.classList.add("horario-slot-seleccionado");
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

    const response = await fetch("/reservas");
    const reservas = await response.json();

    const misReservas = reservas.filter(r => r.emailAlumno === emailAlumno);
    const container   = document.getElementById("reservas-container");
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

    const usuarioLogueado = JSON.parse(sessionStorage.getItem('usuario') || '{}');
    const email = usuarioLogueado.email;

    const reserva = {
        nombreAlumno: document.getElementById("nombreAlumno").value.trim(),
        emailAlumno:  email,
        fecha: fechaInput.value.trim(),
        hora:  horaInput.value.trim()
    };

    const result = await Swal.fire({
        title: "¿Confirmar reserva?",
        text: `Tutoría el ${reserva.fecha} a las ${reserva.hora}`,
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#0d6efd",
        cancelButtonColor: "#dc3545",
        confirmButtonText: "Sí, reservar",
        cancelButtonText: "Cancelar"
    });

    if (!result.isConfirmed) return;

    const response = await fetch("/reservas", {
        method: "POST",
        headers: {
            "Accept":       "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reserva),
        credentials: "include"
    });

    if (response.ok) {

        Swal.fire({
            title: "¡Reserva realizada!",
            text: "Tu tutoría ha sido reservada correctamente.",
            icon: "success",
            confirmButtonColor: "#0d6efd",
            confirmButtonText: "Aceptar"
        });

            reservaForm.reset();

            await cargarHorarios();
            await cargarMisReservas(email)

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

async function cancelarReserva(id) {

    const result = await Swal.fire({
        title: "¿Cancelar reserva?",
        text: "¿Estás seguro de que quieres cancelar esta reserva?",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#0d6efd",
        cancelButtonColor: "#dc3545",
        confirmButtonText: "Sí, cancelar",
        cancelButtonText: "Mejor no"
    });

    if (!result.isConfirmed) return;

    const response = await fetch(`/reservas/${id}`, {
        method: "DELETE",
        credentials: "include"
    });

    if (response.ok) {
        Swal.fire({
            title: "¡Reserva cancelada con éxito!",
            text: "Tu tutoría ha sido cancelada correctamente.",
            icon: "success",
            confirmButtonColor: "#0d6efd",
            confirmButtonText: "Aceptar"
        });
        await cargarHorarios();
        const usuarioLogueado = JSON.parse(sessionStorage.getItem('usuario') || '{}');
        await cargarMisReservas(usuarioLogueado.email);
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

// Carga inicial de horarios al abrir la vista.
cargarHorarios().then(() => console.log("Horarios cargados"));

const usuarioLogueado = JSON.parse(sessionStorage.getItem('usuario') || '{}');
if (usuarioLogueado.email) {
    cargarMisReservas(usuarioLogueado.email);
}