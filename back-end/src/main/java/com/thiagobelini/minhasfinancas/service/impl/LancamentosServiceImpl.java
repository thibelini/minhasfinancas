package com.thiagobelini.minhasfinancas.service.impl;

import com.thiagobelini.minhasfinancas.exception.RegraNegocioException;
import com.thiagobelini.minhasfinancas.model.entity.Lancamento;
import com.thiagobelini.minhasfinancas.model.enumarator.StatusLancamento;
import com.thiagobelini.minhasfinancas.repository.LancamentoRepository;
import com.thiagobelini.minhasfinancas.service.LancamentosService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class LancamentosServiceImpl implements LancamentosService {

    private LancamentoRepository repository;

    public LancamentosServiceImpl(LancamentoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    @Transactional
    @Override
    public Lancamento atualizar(Lancamento lancamento) {
        validar(lancamento);
        Objects.requireNonNull(lancamento.getId());
        return repository.save(lancamento);
    }

    @Transactional
    @Override
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        repository.delete(lancamento);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        Example example = Example.of(lancamentoFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
       return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {
        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Informe uma descrição válida.");
        }

        if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
            throw new RegraNegocioException("Informe um mês válido.");
        }

        if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
            throw new RegraNegocioException("Informe um ano válido.");
        }

        if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um usuário.");
        }

        if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um valor válido.");
        }

        if(lancamento.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo de lançamento.");
        }
    }
}
