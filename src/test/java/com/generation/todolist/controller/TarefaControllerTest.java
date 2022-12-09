package com.generation.todolist.controller;

import com.generation.todolist.model.Tarefa;
import com.generation.todolist.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class TarefaControllerTest {

        @Autowired
        private TestRestTemplate testRestTemplate;

        @Autowired
        private TarefaRepository tarefaRepository;


    @BeforeAll
    void start() {
        tarefaRepository.deleteAll();

        tarefaRepository.save(new Tarefa(0L,"Davi","Davi","Davi",LocalDate.now(),true));
    }

        @Test
        @DisplayName("Criar nova Tarefa")
        public void deveCriarNovaTarefa() throws Exception {

            Tarefa tarefa = new Tarefa(0L, "Tarefa 01", "Tarefa numero 1", "João", LocalDate.now(), true);

            HttpEntity<Tarefa> corpoRequisicao = new HttpEntity<Tarefa>(tarefa);

            ResponseEntity<Tarefa> resposta = testRestTemplate
                    .exchange("/tarefas", HttpMethod.POST, corpoRequisicao, Tarefa.class);

            assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
            assertEquals(corpoRequisicao.getBody().getNome(), resposta.getBody().getNome());

        }

    @Test
    @DisplayName("Listar uma Tarefa Específica")
    public void deveListarApenasUmaTarefa() {

        Tarefa buscaTarefa = tarefaRepository.save(new Tarefa(0L, "Tarefa 02", "Tarefa numero 2",
                "Maria", LocalDate.now(), true));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/tarefas/" + buscaTarefa.getId(), HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());

    }

    @Test
    @DisplayName("Deletar uma tarefa")
    public void deletarTarefa() {
        // para fins de teste, foi criado um @BeforeAll criando uma tarefa e inicializando ela,
        //no método deletarTarefa foi criado outra tarefa e passado um ID a mais para teste.
         tarefaRepository.save(new Tarefa(0L, "Tarefa 02", "Tarefa numero 2",
                "Maria", LocalDate.now(), true));

        tarefaRepository.save(new Tarefa(0L, "Tarefa 03", "Tarefa numero 3",
                "Juliana", LocalDate.now(), true));


        ResponseEntity<String> resposta = testRestTemplate
                // na rota pode passar qualquer ID para teste. Ele procura o ID no que já foi salvo para testar
                .exchange("/tarefas/2", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());

    }

    @Test
    @DisplayName("Buscar todas as tarefas")
    public void buscarTodasTarefas() {
        tarefaRepository.save(new Tarefa(0L, "Tarefa 02", "Tarefa numero 2",
                "Maria", LocalDate.now(), true));
        tarefaRepository.save(new Tarefa(0L, "Tarefa 03", "Tarefa numero 3",
                "Joana", LocalDate.now(), true));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/tarefas/all", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

}

