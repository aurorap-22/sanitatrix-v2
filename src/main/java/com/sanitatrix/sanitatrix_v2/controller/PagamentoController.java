package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Pagamento;
import com.sanitatrix.sanitatrix_v2.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamenti")
@CrossOrigin(origins = "http://localhost:5173")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public List<Pagamento> getAllPagamenti(){
      return pagamentoService.getAllPagamenti();
    }

    @GetMapping("/{id}")
    public Pagamento getPagamentoById(@PathVariable Long id){
        return pagamentoService.getPagamentoById(id);
    }

    @PostMapping
    public Pagamento createPagamento (@RequestBody Pagamento pagamento){
        return pagamentoService.savePagamento(pagamento);
    }

    @PutMapping("/{id}/stato")
    public Pagamento updateStato(@PathVariable Long id,@RequestParam String stato){
        Pagamento p = pagamentoService.getPagamentoById(id);
        p.setStato(stato);
        return pagamentoService.savePagamento(p);
    }
}
