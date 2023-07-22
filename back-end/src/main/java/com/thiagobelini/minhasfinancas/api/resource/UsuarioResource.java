package com.thiagobelini.minhasfinancas.api.resource;

import com.thiagobelini.minhasfinancas.api.resource.dto.UsuarioDTO;
import com.thiagobelini.minhasfinancas.model.entity.Usuario;
import com.thiagobelini.minhasfinancas.service.UsuarioService;
import com.thiagobelini.minhasfinancas.exception.ErroAutenticacao;
import com.thiagobelini.minhasfinancas.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private UsuarioService service;

    public UsuarioResource(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
        Usuario usuario = Usuario.builder().nome(dto.getNome()).senha(dto.getSenha()).email(dto.getEmail()).build();
        try {
          Usuario usuarioSalvo = service.salvarUsuario(usuario);
          return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutenticacao e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
