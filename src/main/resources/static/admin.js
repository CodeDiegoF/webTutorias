const horarioForm = document.getElementById("horario-form");

const horariosContainer = document.getElementById("horarios-container");
const reservasContainer = document.getElementById("reservas-container");

async function cargarHorarios() {

    const response = await fetch("http://localhost:8080/horarios");
    const horarios = await response.json();

    horariosContainer.innerHTML = "";

    horarios.forEach(horario => {

        const div = document.createElement("div");
        div.classList.add("horario");

        div.innerHTML = `
            <strong>${horario.fecha}</strong>
            <br>
            ${horario.hora}
        `;

        horariosContainer.appendChild(div);
    });
}

async function cargarReservas() {

    const response = await fetch("http://localhost:8080/reservas");
    const reservas = await response.json();

    reservasContainer.innerHTML = "";

    reservas.forEach(reserva => {

        const div = document.createElement("div");
        div.classList.add("horario");

        div.innerHTML = `
            <strong>${reserva.nombreAlumno}</strong>
            <br>
            ${reserva.fecha} - ${reserva.hora}
        `;

        reservasContainer.appendChild(div);
    });
}

horarioForm.addEventListener("submit", async (e) => {

    e.preventDefault();

    const horario = {
        fecha: document.getElementById("fecha").value,
        hora: document.getElementById("hora").value
    };

    const response = await fetch("http://localhost:8080/horarios", {
        method: "POST",
        headers: {
            "Accept" : "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(horario)
    });

    if (response.ok) {

        alert("Horario creado");

        horarioForm.reset();

        await cargarHorarios();

    } else {

        alert("Error creando horario");
    }
});

cargarHorarios().then(() => console.log("Horarios cargados"));
cargarReservas().then(() => console.log("Reservas cargadas"));