package com.academic.fh.service;

import com.academic.fh.model.PQRS;
import com.academic.fh.repository.PqrsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PqrsService {

    private final PqrsRepository pqrsRepository;

    public PqrsService(PqrsRepository pqrsRepository) {
        this.pqrsRepository = pqrsRepository;
    }

    public List<PQRS> findAll() {
        return pqrsRepository.findAll();
    }

    public Optional<PQRS> findById(Long id) {
        return pqrsRepository.findById(id);
    }

    public PQRS save(PQRS pqrs) {
        return pqrsRepository.save(pqrs);
    }

    public void delete(Long id) {
        pqrsRepository.deleteById(id);
    }
}
