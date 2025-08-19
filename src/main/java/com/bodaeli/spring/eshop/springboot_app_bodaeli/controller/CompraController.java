package com.bodaeli.spring.eshop.springboot_app_bodaeli.controller;

import com.bodaeli.spring.eshop.springboot_app_bodaeli.dto.CompraDTO;
import com.bodaeli.spring.eshop.springboot_app_bodaeli.entity.Compra;
import com.bodaeli.spring.eshop.springboot_app_bodaeli.entity.Regalo;
import com.bodaeli.spring.eshop.springboot_app_bodaeli.repository.CompraRepository;
import com.bodaeli.spring.eshop.springboot_app_bodaeli.repository.RegaloRepository;
import com.bodaeli.spring.eshop.springboot_app_bodaeli.service.EmailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraRepository compraRepository;
    private final RegaloRepository regaloRepository;
    private final EmailService emailService;

    public CompraController(CompraRepository compraRepository,
            RegaloRepository regaloRepository,
            EmailService emailService) {
        this.compraRepository = compraRepository;
        this.regaloRepository = regaloRepository;
        this.emailService = emailService;
    }

    // ✅ Crear una compra (sin afectar stock todavía)
    @PostMapping
    public ResponseEntity<?> crearCompra(@Valid @RequestBody CompraDTO compraDto) {
        Map<String, String> response = new HashMap<>();
        try {
            Regalo regalo = regaloRepository.findById(compraDto.getRegaloId())
                    .orElseThrow(() -> new RuntimeException("Regalo no encontrado"));

            Compra compra = new Compra();
            compra.setRegalo(regalo);
            compra.setNombreInvitado(compraDto.getNombreInvitado());
            compra.setEmailInvitado(compraDto.getEmailInvitado());
            compra.setMontoPagado(regalo.getPrecio());
            compra.setMetodoPago("Transferencia bancaria");
            compra.setReferenciaTransferencia(compraDto.getReferenciaTransferencia());
            compra.setEstadoPago("Pendiente de transferencia");

            compraRepository.save(compra);

            response.put("mensaje", "Compra registrada correctamente. Realiza la transferencia bancaria.");
            response.put("banco", "BancoEstado");
            response.put("tipoCuenta", "Cuenta Vista");
            response.put("numeroCuenta", "38470118340");
            response.put("rut", "18778478-6");
            response.put("nombre", "SEBASTIAN HUGO ANDRES FARIAS TAPIA");
            response.put("correo", "seba.prev15@gmail.com");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Ocurrió un error al procesar la compra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ Listar todas las compras (para AdminPanel)
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            var compras = compraRepository.findAll();
            return ResponseEntity.ok(compras);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al obtener las compras: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ Listar compras pendientes de confirmación
    @GetMapping("/pendientes")
    public ResponseEntity<?> listarPendientes() {
        try {
            var pendientes = compraRepository.findAll().stream()
                    .filter(c -> "Pendiente de transferencia".equals(c.getEstadoPago()))
                    .toList();
            return ResponseEntity.ok(pendientes);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Error al obtener compras pendientes: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ Confirmar pago y enviar correo
    @PutMapping("/{id}/confirmar-pago")
    public ResponseEntity<?> confirmarPago(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            Compra compra = compraRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            if ("Pagado".equals(compra.getEstadoPago())) {
                response.put("mensaje", "El pago ya fue confirmado anteriormente.");
                return ResponseEntity.badRequest().body(response);
            }

            // Cambiar estado a Pagado
            compra.setEstadoPago("Pagado");

            // Reducir stock
            Regalo regalo = compra.getRegalo();
            if (regalo.getStock() > 0) {
                regalo.setStock(regalo.getStock() - 1);
                regaloRepository.save(regalo);
            } else {
                response.put("mensaje", "El regalo ya no tiene stock disponible para confirmar el pago.");
                return ResponseEntity.badRequest().body(response);
            }

            compraRepository.save(compra);

            // 📧 Enviar correo de confirmación
            try {
                emailService.enviarCorreoCompra(
                        compra.getEmailInvitado(),
                        compra.getNombreInvitado(),
                        regalo.getNombre(),
                        regalo.getPrecio(),
                        regalo.getImagenUrl() // asumiendo que tu entidad Regalo tiene un campo imagenUrl
                );
            } catch (Exception e) {
                response.put("mensaje",
                        "Pago confirmado y stock actualizado, pero ocurrió un error al enviar el correo: "
                                + e.getMessage());
                return ResponseEntity.ok(response); // confirmamos pago igual aunque falle el mail
            }

            response.put("mensaje", "Pago confirmado, stock actualizado y correo enviado.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al confirmar el pago: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ Eliminar compra por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCompra(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            if (!compraRepository.existsById(id)) {
                response.put("mensaje", "La compra no existe.");
                return ResponseEntity.badRequest().body(response);
            }

            compraRepository.deleteById(id);
            response.put("mensaje", "Compra eliminada correctamente.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("mensaje", "Error al eliminar la compra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}