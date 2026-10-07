package com.fiap.mercadoexpresssec.config;

import com.fiap.mercadoexpresssec.model.Mercado;
import com.fiap.mercadoexpresssec.model.Role;
import com.fiap.mercadoexpresssec.repository.MercadoRepository;
import com.fiap.mercadoexpresssec.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Carga inicial: garante um usuario ADMIN (o Sign Up publico so cria CLIENTE)
 * e alguns produtos de exemplo quando a tabela estiver vazia.
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final MercadoRepository mercadoRepository;

    @Value("${app.admin.nome}")
    private String adminNome;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.senha}")
    private String adminSenha;

    @Value("${app.seed-produtos:true}")
    private boolean seedProdutos;

    public DataInitializer(UsuarioService usuarioService, MercadoRepository mercadoRepository) {
        this.usuarioService = usuarioService;
        this.mercadoRepository = mercadoRepository;
    }

    @Override
    public void run(String... args) {
        if (!usuarioService.emailCadastrado(adminEmail)) {
            usuarioService.criar(adminNome, adminEmail, adminSenha, Role.ADMIN);
            log.info("Usuario ADMIN inicial criado: {}", adminEmail);
        }

        if (seedProdutos && mercadoRepository.count() == 0) {
            mercadoRepository.saveAll(List.of(
                    Mercado.builder().nome("Meia cano alto algodão").tipo("Vestuário").setor("Meias").tamanho(39.0).preco(19.90).estoque(120).build(),
                    Mercado.builder().nome("Meia esportiva kit 3 pares").tipo("Vestuário").setor("Meias").tamanho(42.0).preco(34.90).estoque(60).build(),
                    Mercado.builder().nome("Detergente neutro 500ml").tipo("Limpeza").setor("Produtos de limpeza").tamanho(0.5).preco(2.99).estoque(300).build(),
                    Mercado.builder().nome("Água sanitária 2L").tipo("Limpeza").setor("Produtos de limpeza").tamanho(2.0).preco(7.49).estoque(150).build(),
                    Mercado.builder().nome("Banana prata (kg)").tipo("Fruta").setor("Hortifruti").tamanho(1.0).preco(6.99).estoque(80).build(),
                    Mercado.builder().nome("Maçã gala (kg)").tipo("Fruta").setor("Hortifruti").tamanho(1.0).preco(9.98).estoque(70).build()
            ));
            log.info("Produtos de exemplo cadastrados na tabela TDS_Sec_MVC_TB_Mercado");
        }
    }

}
