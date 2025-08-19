package com.bodaeli.spring.eshop.springboot_app_bodaeli.controller;

import com.bodaeli.spring.eshop.springboot_app_bodaeli.entity.Regalo;
import com.bodaeli.spring.eshop.springboot_app_bodaeli.repository.RegaloRepository;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regalos")
public class RegaloController {

    private final RegaloRepository regaloRepository;

    public RegaloController(RegaloRepository regaloRepository) {
        this.regaloRepository = regaloRepository;
    }

    @GetMapping
    public ResponseEntity<List<Regalo>> listarRegalos() {
        List<Regalo> regalos = regaloRepository.findAll();
        return ResponseEntity.ok(regalos);
    }

    @PostMapping
    public ResponseEntity<Regalo> crearRegalo(@Valid @RequestBody Regalo regalo) {
        return ResponseEntity.ok(regaloRepository.save(regalo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Regalo> actualizarRegalo(@PathVariable Long id, @RequestBody Regalo actualizado) {
        return regaloRepository.findById(id)
                .map(regalo -> {
                    regalo.setNombre(actualizado.getNombre());
                    regalo.setDescripcion(actualizado.getDescripcion());
                    regalo.setImagenUrl(actualizado.getImagenUrl());
                    regalo.setPrecio(actualizado.getPrecio());
                    regalo.setStock(actualizado.getStock());
                    return ResponseEntity.ok(regaloRepository.save(regalo));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegalo(@PathVariable Long id) {
        if (regaloRepository.existsById(id)) {
            regaloRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}