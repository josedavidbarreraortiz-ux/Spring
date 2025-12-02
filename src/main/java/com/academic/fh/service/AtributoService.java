package com.academic.fh.service;

import com.academic.fh.model.Atributo;
import com.academic.fh.repository.AtributoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AtributoService {

    private final AtributoRepository atributoRepository;

    public AtributoService(AtributoRepository atributoRepository) {
        this.atributoRepository = atributoRepository;
    }

    public List<Atributo> findAll() {
        return atributoRepository.findAll();
    }

    public Optional<Atributo> findById(Integer id) {
        return atributoRepository.findById(id);
    }

    public Atributo save(Atributo atributo) {
        return atributoRepository.save(atributo);
    }

    public void delete(Integer id) {
        atributoRepository.deleteById(id);
    }
    
}
