package com.projeto.emprestimo.controller;

import com.projeto.emprestimo.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EquipamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Test
    void deveCadastrarEquipamento() throws Exception {

        String patrimonio = "TESTE-MOCKMVC-001";

        String json = """
                {
                  "patrimonio": "%s",
                  "nome": "Notebook de teste",
                  "tipo": "NOTEBOOK"
                }
                """.formatted(patrimonio);

        String token = tokenService.gerarToken("admin", "ADMIN");

        mockMvc.perform(post("/equipamentos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornar404QuandoEquipamentoNaoExiste() throws Exception {

        String token = tokenService.gerarToken("admin", "ADMIN");

        mockMvc.perform(get("/equipamentos/999999999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Equipamento não encontrado"));
    }
}