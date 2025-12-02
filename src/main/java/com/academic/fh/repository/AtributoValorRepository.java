package com.academic.fh.repository;

import com.academic.fh.model.AtributoValor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtributoValorRepository extends JpaRepository<AtributoValor, Integer> {

    @Query("SELECT av FROM AtributoValor av WHERE av.atributo.atributoId = :atributoId")
    List<AtributoValor> findByAtributoId(Integer atributoId);
}
