package com.fiap.mercadoexpresssec.repository;

import com.fiap.mercadoexpresssec.model.Mercado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MercadoRepository extends JpaRepository<Mercado, Long> {

    List<Mercado> findAllByOrderByIdAsc();

    List<Mercado> findByNomeContainingIgnoreCaseOrderByIdAsc(String nome);

}
