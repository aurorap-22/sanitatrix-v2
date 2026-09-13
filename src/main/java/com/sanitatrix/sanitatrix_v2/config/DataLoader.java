package com.sanitatrix.sanitatrix_v2.config;
import com.sanitatrix.sanitatrix_v2.model.*;
import com.sanitatrix.sanitatrix_v2.repository.PrestazioneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner init(PrestazioneRepository preRepo) {
        return args -> {
            if(preRepo.count() == 0) {
                for(TipoVisita t : TipoVisita.values()){
                    Prestazione p = new Prestazione();
                    p.setNome("Visita " + t.name().substring(0,1) + t.name().substring(1).toLowerCase());
                    p.setTipoVisita(t);
                    p.setDescrizione("Visita specialistica in " + t.name());
                    p.setPrezzo(new BigDecimal("50.00"));
                    preRepo.save(p);
                }
            }
        };
    }
}
