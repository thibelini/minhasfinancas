package com.thiagobelini.minhasfinancas.repository;

import com.thiagobelini.minhasfinancas.model.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void deveVerificarAExistenciaDeUmEmail() {
        Usuario usuario = getUsuario();
        entityManager.persist(usuario);

        boolean result = repository.existsByEmail("usuario@gmail");
        assertThat(result).isTrue();
    }

    @Test
    void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
        boolean result = repository.existsByEmail("usuario@gmail");
        assertThat(result).isFalse();
    }

    @Test
    void devePersistirUmUsuarioNaBaseDeDados() {
        Usuario usuario = getUsuario();
        Usuario usuarioSalvo = repository.save(usuario);
        assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    void deveBuscarUmUsuarioPorEmail() {
        Usuario usuario = getUsuario();
        entityManager.persist(usuario);
        Optional<Usuario> result = repository.findByEmail("usuario@gmail");
        assertThat(result).isPresent();
    }

    @Test
    void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
        Optional<Usuario> result = repository.findByEmail("usuario@gmail");
        assertThat(result).isNotPresent();
    }

    private static Usuario getUsuario() {
        return Usuario.builder().nome("usuario").email("usuario@gmail").build();
    }

}
