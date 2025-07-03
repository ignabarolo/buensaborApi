package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Factura;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final PdfService pdfService;

    @Value("${mail.from.email}")
    private String fromEmail;

    @Value("${mail.from.name}")
    private String fromName;

    @Value("${mail.base.url}")
    private String baseUrl;

    public void enviarPasswordResetEmail(String email, String nombre, String token) {
        try {
            String resetUrl = baseUrl + "/reset-password?token=" + token;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setFrom(fromEmail, fromName);
            helper.setSubject("Recuperación de contraseña");

            String htmlContent = crearPasswordResetEmailTemplate(nombre, resetUrl);
            helper.setText(htmlContent, true);
            adjuntarLogo(helper);

            mailSender.send(message);
            log.info("Email de recuperación enviado a: {}", email);

        } catch (MessagingException | IOException e) {
            log.error("Error al enviar email: {}", e.getMessage());
            throw new RuntimeException("Error al enviar email", e);
        }
    }

    public void enviarEmailBienvenida(String email, String nombre) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setFrom(fromEmail, fromName);
            helper.setSubject("¡Bienvenido!");

            String htmlContent = crearEmailBienvenidaTemplate(nombre);
            helper.setText(htmlContent, true);
            adjuntarLogo(helper);

            mailSender.send(message);
            log.info("Email de bienvenida enviado a: {}", email);

        } catch (MessagingException | IOException e) {
            log.error("Error al enviar email de bienvenida: {}", e.getMessage());
        }
    }

    public void enviarFacturaEmail(Factura factura) {
        try {
            // Obtener el email y nombre del cliente asociado a la factura
            String email = factura.getCliente().getEmail();
            String nombre = factura.getCliente().getNombre();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setFrom(fromEmail, fromName);
            helper.setSubject("Tu factura de compra #" + factura.getNroComprobante());

            String htmlContent = crearFacturaEmailTemplate(nombre, factura);
            helper.setText(htmlContent, true);

            // Adjuntar PDF de la factura
            byte[] pdfBytes = pdfService.generarFacturaPdf(factura);
            helper.addAttachment("factura_" + factura.getNroComprobante() + ".pdf",
                    new ByteArrayDataSource(pdfBytes, "application/pdf"));

            adjuntarLogo(helper);

            mailSender.send(message);
            log.info("Email con factura enviado a: {}", email);

        } catch (MessagingException | IOException e) {
            log.error("Error al enviar email con factura: {}", e.getMessage());
            throw new RuntimeException("Error al enviar email con factura", e);
        }
    }

    public void enviarNotaCreditoEmail(Factura notaCredito) {
        try {
            // Obtener el email y nombre del cliente asociado a la factura
            String email = notaCredito.getCliente().getEmail();
            String nombre = notaCredito.getCliente().getNombre();


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setFrom(fromEmail, fromName);
            helper.setSubject("Nota de Crédito - Pedido Cancelado");

            String htmlContent = crearNotaCreditoEmailTemplate(nombre, notaCredito);
            helper.setText(htmlContent, true);

            // Adjuntar PDF de la nota de crédito
            byte[] pdfBytes = pdfService.generarNotaCreditoPdf(notaCredito);
            helper.addAttachment("nota_credito_" + Math.abs(notaCredito.getNroComprobante()) + ".pdf",
                    new ByteArrayDataSource(pdfBytes, "application/pdf"));

            adjuntarLogo(helper);

            mailSender.send(message);
            log.info("Email con nota de crédito enviado a: {}", email);

        } catch (MessagingException | IOException e) {
            log.error("Error al enviar email con nota de crédito: {}", e.getMessage());
            throw new RuntimeException("Error al enviar email con nota de crédito", e);
        }
    }

    private void adjuntarLogo(MimeMessageHelper helper) throws MessagingException, IOException {
        // El logo debe estar en la carpeta resources/static/images
        ClassPathResource resource = new ClassPathResource("static/images/logo.png");
        helper.addInline("logoImage", resource);
    }

    private String crearPasswordResetEmailTemplate(String nombre, String resetUrl) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #000; }
                .container { max-width: 600px; margin: 0 auto; }
                .header { text-align: center; margin-bottom: 20px; }
                .btn { background-color: #4CAF50; color: white; padding: 10px 20px; 
                       text-decoration: none; border-radius: 5px; display: inline-block; }
                .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #777; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <img src="cid:logoImage" alt="El Buen Sabor Logo" style="max-width: 150px;">
                </div>
                <h1>Hola %s,</h1>
                <p>Hemos recibido una solicitud para restablecer tu contraseña.</p>
                <p>Haz clic en el siguiente enlace para crear una nueva contraseña:</p>
                <p style="text-align: center;">
                    <a href="%s" class="btn">Recuperar contraseña</a>
                </p>
                <p>Si no solicitaste este cambio, puedes ignorar este correo.</p>
                <div class="footer">
                    <p>&copy; 2025 El Buen Sabor. Todos los derechos reservados.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(nombre, resetUrl);
    }

    private String crearEmailBienvenidaTemplate(String nombre) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #000; }
                .container { max-width: 600px; margin: 0 auto; }
                .header { text-align: center; margin-bottom: 20px; }
                .content { margin: 20px 0; }
                .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #777; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <img src="cid:logoImage" alt="El Buen Sabor Logo" style="max-width: 150px;">
                </div>
                <div class="content">
                    <h1>¡Bienvenido a El Buen Sabor, %s!</h1>
                    <p>Gracias por registrarte en nuestra plataforma. Estamos encantados de tenerte como cliente.</p>
                    <p>Ahora podrás disfrutar de nuestras deliciosas comidas y promociones especiales.</p>
                </div>
                <div class="footer">
                    <p>&copy; 2025 El Buen Sabor. Todos los derechos reservados.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(nombre);
    }

    private String crearFacturaEmailTemplate(String nombre, Factura factura) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #000;}
                .container { max-width: 600px; margin: 0 auto; }
                .header { text-align: center; margin-bottom: 20px; }
                .content { margin: 20px 0; }
                .details { background-color: #f9f9f9; padding: 15px; border-radius: 5px; }
                .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #777; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <img src="cid:logoImage" alt="El Buen Sabor Logo" style="max-width: 150px;">
                </div>
                <div class="content">
                    <h1>¡Gracias por tu pedido, %s!</h1>
                    <p>Adjunto encontrarás la factura de tu compra reciente en El Buen Sabor.</p>
                    <div class="details">
                        <p><strong>Número de Factura:</strong> %d</p>
                        <p><strong>Importe Total:</strong> $%.2f</p>
                        <p><strong>Fecha:</strong> %s</p>
                    </div>
                    <p>Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos.</p>
                </div>
                <div class="footer">
                    <p>&copy; 2025 El Buen Sabor. Todos los derechos reservados.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                nombre,
                factura.getNroComprobante(),
                factura.getTotalVenta(),
                factura.getFechaFacturacion().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }

    private String crearNotaCreditoEmailTemplate(String nombre, Factura notaCredito) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #000;}
                .container { max-width: 600px; margin: 0 auto; }
                .header { text-align: center; margin-bottom: 20px; }
                .content { margin: 20px 0; }
                .details { background-color: #f9f9f9; padding: 15px; border-radius: 5px; }
                .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #777; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <img src="cid:logoImage" alt="El Buen Sabor Logo" style="max-width: 150px;">
                </div>
                <div class="content">
                    <h1>Información sobre tu pedido cancelado</h1>
                    <p>Estimado/a %s,</p>
                    <p>Lamentamos informarte que tu pedido ha sido cancelado. Adjunto encontrarás la nota de crédito correspondiente.</p>
                    <div class="details">
                        <p><strong>Número de Nota de Crédito:</strong> NC%d</p>
                        <p><strong>Importe Reintegrado:</strong> $%.2f</p>
                        <p><strong>Fecha:</strong> %s</p>
                    </div>
                    <p>El importe será reintegrado según el método de pago original. Si tienes alguna duda, por favor contáctanos.</p>
                </div>
                <div class="footer">
                    <p>&copy; 2025 El Buen Sabor. Todos los derechos reservados.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                nombre,
                Math.abs(notaCredito.getNroComprobante()),
                Math.abs(notaCredito.getTotalVenta()),
                notaCredito.getFechaFacturacion().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }
}

