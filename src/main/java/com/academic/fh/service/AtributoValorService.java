package com.academic.fh.service;

import com.academic.fh.model.AtributoValor;
import com.academic.fh.repository.AtributoValorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AtributoValorService {

    private final AtributoValorRepository atributoValorRepository;

    public AtributoValorService(AtributoValorRepository atributoValorRepository) {
        this.atributoValorRepository = atributoValorRepository;
    }

    public List<AtributoValor> findAll() {
        return atributoValorRepository.findAll();
    }

    public Optional<AtributoValor> findById(Integer id) {
        return atributoValorRepository.findById(id);
    }

    public AtributoValor save(AtributoValor atributoValor) {
        return atributoValorRepository.save(atributoValor);
    }

    public void delete(Integer id) {
        atributoValorRepository.deleteById(id);
    }
}
