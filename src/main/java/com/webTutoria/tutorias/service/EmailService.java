package com.webTutoria.tutorias.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${app.url}")
    private String appUrl;

    private final JavaMailSender mailSender;

    @Value("${profesor.email}")
    private String emailProfesor;

    @Value("${spring.mail.username}")
    private String emailRemitente;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Notifica al profesor cuando un alumno reserva una tutoría.
     */
    @Async
    public void notificarProfesorReserva(String nombreAlumno, String emailAlumno,
                                         String fecha, String hora) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailRemitente);
        msg.setTo(emailProfesor);
        msg.setSubject("Nueva reserva de tutoría");
        msg.setText(
                "El alumno " + nombreAlumno + " (" + emailAlumno + ") " +
                        "ha reservado una tutoría el " + fecha + " a las " + hora + "."
        );
        mailSender.send(msg);
    }

    /**
     * Notifica al profesor cuando un alumno cancela una reserva.
     */
    @Async
    public void notificarProfesorCancelacion(String nombreAlumno, String emailAlumno,
                                             String fecha, String hora) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailRemitente);
        msg.setTo(emailProfesor);
        msg.setSubject("Cancelación de tutoría");
        msg.setText(
                "El alumno " + nombreAlumno + " (" + emailAlumno + ") " +
                        "ha cancelado su tutoría del " + fecha + " a las " + hora + "."
        );
        mailSender.send(msg);
    }

    /**
     * Notifica al alumno cuando el profesor elimina un horario con reserva asociada.
     */
    @Async
    public void notificarAlumnoHorarioEliminado(String emailAlumno, String nombreAlumno,
                                                String fecha, String hora) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailRemitente);
        msg.setTo(emailAlumno);
        msg.setSubject("Tu tutoría ha sido cancelada");
        msg.setText(
                "Hola " + nombreAlumno + ",\n\n" +
                        "Tu tutoría del " + fecha + " a las " + hora +
                        " ha sido cancelada por el profesor.\n\n" +
                        "Por favor, reserva un nuevo horario disponible."
        );
        mailSender.send(msg);
    }

    /**
     * Envia el email de recuperación de contraseña con el enlace.
     */
    @Async
    public void enviarRecuperacion(String emailDestino, String token) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailRemitente);
        msg.setTo(emailDestino);
        msg.setSubject("Recuperación de contraseña - TutorApp");
        msg.setText(
                "Has solicitado restablecer tu contraseña.\n\n" +
                        "Haz click en el siguiente enlace para crear una nueva contraseña:\n\n" +
                        appUrl + "/resetPassword.html?token=" + token + "\n\n" +
                        "Este enlace expirará en 1 hora.\n\n" +
                        "Si no has solicitado este cambio, ignora este mensaje."
        );
        mailSender.send(msg);
    }
}