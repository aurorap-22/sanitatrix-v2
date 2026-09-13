package com.sanitatrix.sanitatrix_v2.service;

import com.sanitatrix.sanitatrix_v2.model.Pagamento;
import com.sanitatrix.sanitatrix_v2.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    public List<Pagamento> getAllPagamenti(){
        return pagamentoRepository.findAll();
    }

    public Pagamento getPagamentoById(Long id){
        return pagamentoRepository.findById(id).orElse(null);
    }

    public Pagamento savePagamento(Pagamento pagamento){
        return pagamentoRepository.save(pagamento);
    }

}
