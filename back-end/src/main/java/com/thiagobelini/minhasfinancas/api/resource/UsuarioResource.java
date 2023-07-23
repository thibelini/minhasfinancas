package com.thiagobelini.minhasfinancas.api.resource;

import com.thiagobelini.minhasfinancas.api.resource.dto.UsuarioDTO;
import com.thiagobelini.minhasfinancas.model.entity.Usuario;
import com.thiagobelini.minhasfinancas.service.LancamentosService;
import com.thiagobelini.minhasfinancas.service.UsuarioService;
import com.thiagobelini.minhasfinancas.exception.ErroAutenticacao;
import com.thiagobelini.minhasfinancas.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

    private final UsuarioService service;
    private final LancamentosService lancamentosService;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody UsuarioDTO dto) {
        Usuario usuario = Usuario.builder().nome(dto.getNome()).senha(dto.getSenha()).email(dto.getEmail()).build();
        try {
          Usuario usuarioSalvo = service.salvarUsuario(usuario);
          return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity<Object> autenticar(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutenticacao e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}/saldo")
    public ResponseEntity obterSaldoPorUsuario(@PathVariable("id") Long id) {
        return service.obterPorId(id)
                .map(usuario -> {
                    BigDecimal saldo = lancamentosService.obterSaldoPorUsuario(id);
                    return ResponseEntity.ok(saldo);
                }).orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));
    }
}
