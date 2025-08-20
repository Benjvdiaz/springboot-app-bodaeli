package com.bodaeli.spring.eshop.springboot_app_bodaeli.service;

import java.io.File;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.lang.NonNull;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  private static final Logger log = LoggerFactory.getLogger(EmailService.class);

  private final JavaMailSender mailSender;
  private final String fromAddress;

  public EmailService(
      JavaMailSender mailSender,
      @Value("${app.mail.from:${spring.mail.username}}") String fromAddress) {
    this.mailSender = mailSender;
    this.fromAddress = fromAddress;
  }

  public void enviarCorreoCompra(@NonNull String destinatario,
      @NonNull String nombreComprador,
      @NonNull String nombreRegalo,
      @NonNull BigDecimal monto,
      @NonNull String nombreArchivoImagen) {  // <-- ahora recibe solo el nombre del archivo
    doSend(destinatario, nombreComprador, nombreRegalo, monto, nombreArchivoImagen);
  }

  @Async
  public void enviarCorreoCompraAsync(@NonNull String destinatario,
      @NonNull String nombreComprador,
      @NonNull String nombreRegalo,
      @NonNull BigDecimal monto,
      @NonNull String nombreArchivoImagen) {
    doSend(destinatario, nombreComprador, nombreRegalo, monto, nombreArchivoImagen);
  }

  private void doSend(String destinatario, String nombreComprador, String nombreRegalo,
      BigDecimal monto, String nombreArchivoImagen) {
    try {
      MimeMessage mensaje = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

      helper.setFrom(fromAddress);
      helper.setTo(destinatario);
      helper.setSubject("🎁 Pago confirmado – Tienda Online Eli & Seba");

      String cid = "imagenRegalo"; // ID para la imagen inline

      String contenidoHtml = String.format(
          """
          <div style="font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px;">
            <div style="max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
              <h2 style="color: #6c63ff; text-align: center;">¡Pago confirmado! 🎉</h2>
              <p style="font-size: 16px; color: #333;">
                Hola <strong>%s</strong>,
              </p>
              <p style="font-size: 16px; color: #333;">
                Hemos <strong>confirmado tu pago</strong> en <strong>Tienda Online Eli & Seba</strong>.
              </p>
              <div style="background-color: #f1f1f1; padding: 15px; border-radius: 8px; margin: 20px 0; text-align:center;">
                <p style="margin: 0; font-size: 16px; color: #333;">
                  🎁 <strong>Regalo:</strong> %s
                </p>
                <p style="margin: 5px 0; font-size: 16px; color: #333;">
                  💰 <strong>Monto:</strong> $%.2f
                </p>
                <img src="cid:%s" alt="Imagen del regalo" style="max-width: 200px; margin-top: 10px; border-radius: 8px;"/>
              </div>
              <p style="font-size: 16px; color: #333;">
                ¡Muchas gracias por tu aporte! 💜
              </p>
              <p style="font-size: 12px; color: #777; text-align: center; margin-top: 24px;">
                Este es un mensaje automático, por favor no respondas a este correo.
              </p>
              <div style="text-align: center; margin-top: 20px;">
                <a href="https://regalos-matrimonio.onrender.com/" style="background-color: #6c63ff; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px;">
                  Volver a la tienda
                </a>
              </div>
            </div>
          </div>
          """,
          nombreComprador, nombreRegalo, monto, cid);

      helper.setText(contenidoHtml, true);

      // Ruta del proyecto a la carpeta de imágenes
      String rutaImagen = "src/main/resources/public/img/" + nombreArchivoImagen;
      FileSystemResource resource = new FileSystemResource(new File(rutaImagen));

      if (!resource.exists()) {
        log.warn("No se encontró la imagen: {}", rutaImagen);
      } else {
        helper.addInline(cid, resource);
      }

      mailSender.send(mensaje);
      log.info("Correo de confirmación enviado a {}", destinatario);

    } catch (MessagingException | MailException e) {
      log.error("Error al enviar correo a {}: {}", destinatario, e.getMessage(), e);
    }
  }
}