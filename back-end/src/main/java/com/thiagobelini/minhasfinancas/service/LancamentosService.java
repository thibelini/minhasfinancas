package com.thiagobelini.minhasfinancas.service;

import com.thiagobelini.minhasfinancas.model.entity.Lancamento;
import com.thiagobelini.minhasfinancas.model.enumarator.StatusLancamento;

import java.util.List;

public interface LancamentosService {

    Lancamento salvar(Lancamento lancamento);
    Lancamento atualizar(Lancamento lancamento);
    void deletar(Lancamento lancamento);
    List<Lancamento> buscar(Lancamento lancamentoFiltro);
    void atualizarStatus(Lancamento lancamento, StatusLancamento status);

    void validar(Lancamento lancamento);


}
