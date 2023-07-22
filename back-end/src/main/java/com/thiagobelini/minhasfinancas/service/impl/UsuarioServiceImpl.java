package com.thiagobelini.minhasfinancas.service.impl;

import com.thiagobelini.minhasfinancas.model.entity.Usuario;
import com.thiagobelini.minhasfinancas.repository.UsuarioRepository;
import com.thiagobelini.minhasfinancas.service.UsuarioService;
import com.thiagobelini.minhasfinancas.exception.ErroAutenticacao;
import com.thiagobelini.minhasfinancas.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if(!usuario.isPresent()) {
            throw new ErroAutenticacao("Usuário não encontrado para email informado.");
        }

        if(!usuario.get().getSenha().equalsIgnoreCase(senha)) {
            throw new ErroAutenticacao("Senha inválida.");
        }

        return usuario.get();

    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existEmail = repository.existsByEmail(email);
        if (existEmail) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }
}
