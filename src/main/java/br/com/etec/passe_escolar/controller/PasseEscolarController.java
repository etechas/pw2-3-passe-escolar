package br.com.etec.passe_escolar.controller;

import br.com.etec.passe_escolar.model.PasseEscolarModel;
import br.com.etec.passe_escolar.repository.PasseEscolarRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/passes")
public class PasseEscolarController {

    @Autowired
    private PasseEscolarRepository passeEscolarRepository;

    @GetMapping
    public ResponseEntity<List<PasseEscolarModel>> listar() {
        return ResponseEntity.ok(passeEscolarRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasseEscolarModel> buscarPorId(@PathVariable Long id) {
        var passe = passeEscolarRepository.findById(id);
        if (passe.isPresent()) {
            return ResponseEntity.ok(passe.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PasseEscolarModel> cadastrar(@RequestBody @Valid PasseEscolarModel passe) {
        if (passe.getDataSolicitacao() == null) {
            passe.setDataSolicitacao(LocalDateTime.now());
        }
        if (passe.getSaldoRecarga() == null) {
            passe.setSaldoRecarga(BigDecimal.ZERO);
        }
        if (passe.getAtivo() == null) {
            passe.setAtivo("ATIVO");
        }

        var novoPasse = passeEscolarRepository.save(passe);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPasse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PasseEscolarModel> atualizar(@RequestBody @Valid PasseEscolarModel passe, @PathVariable Long id) {
        var existe = passeEscolarRepository.findById(id);
        if (existe.isPresent()) {
            passe.setId(id);
            if (passe.getDataSolicitacao() == null) {
                passe.setDataSolicitacao(existe.get().getDataSolicitacao());
            }
            var passeAtualizado = passeEscolarRepository.save(passe);
            return ResponseEntity.ok(passeAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        var existe = passeEscolarRepository.findById(id);
        if (existe.isPresent()) {
            passeEscolarRepository.delete(existe.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/{id}/recarregar")
    public ResponseEntity<?> recarregar(@PathVariable Long id, @RequestBody BigDecimal valor) {
        var existe = passeEscolarRepository.findById(id);

        if (existe.isPresent()) {
            var passe = existe.get();

            if (!passe.getAtivo().contains("ATIVO")) {
                return ResponseEntity.badRequest().body("Não é possível recarregar um passe bloqueado ou inativo.");
            }

            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("O valor da recarga deve ser maior que zero.");
            }

            var novoSaldo = passe.getSaldoRecarga().add(valor);
            passe.setSaldoRecarga(novoSaldo);

            var passeAtualizado = passeEscolarRepository.save(passe);
            return ResponseEntity.ok(passeAtualizado);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<PasseEscolarModel> alterarStatus(@PathVariable Long id, @RequestBody String novoStatus) {
        var existe = passeEscolarRepository.findById(id);

        if (existe.isPresent()) {
            var passe = existe.get();
            passe.setAtivo(novoStatus.toUpperCase());

            var passeAtualizado = passeEscolarRepository.save(passe);
            return ResponseEntity.ok(passeAtualizado);
        }

        return ResponseEntity.notFound().build();
    }
}