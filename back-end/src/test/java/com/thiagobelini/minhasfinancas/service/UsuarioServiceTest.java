package com.thiagobelini.minhasfinancas.service;

import com.thiagobelini.minhasfinancas.repository.UsuarioRepository;
import com.thiagobelini.minhasfinancas.exception.RegraNegocioException;
import com.thiagobelini.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UsuarioServiceTest {

    UsuarioService service;
    @MockBean
    UsuarioRepository repository;

    @BeforeEach
    void setUp() {
        service = new UsuarioServiceImpl(repository);
    }

    @Test
    void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {

        when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        RegraNegocioException thrown = assertThrows(
                RegraNegocioException.class,
                () -> service.validarEmail("email@email.com")
        );

        assertTrue(thrown.getMessage().contains("Já existe um usuário cadastrado com este email."));

    }

}
