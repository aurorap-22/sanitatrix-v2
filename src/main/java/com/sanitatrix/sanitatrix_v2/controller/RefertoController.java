package com.sanitatrix.sanitatrix_v2.controller;

import com.sanitatrix.sanitatrix_v2.model.Referto;
import com.sanitatrix.sanitatrix_v2.service.RefertoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referti")
public class RefertoController {

    @Autowired
    private RefertoService refertoService;

    @GetMapping
    public List<Referto> getAllReferti(){
        return refertoService.getAllReferti();
    }

    @GetMapping("/paziente/{pazienteId}")
    public List<Referto> getByPaziente(@PathVariable Long pazienteId){
        return refertoService.getByPazienteId(pazienteId);
    }

    @PostMapping
    public Referto createReferto(@RequestBody Referto referto){
        return refertoService.saveReferto(referto);
    }
}
