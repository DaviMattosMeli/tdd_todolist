package com.generation.todolist.controller;

import com.generation.todolist.model.Tarefa;
import com.generation.todolist.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @PostMapping
    public ResponseEntity<Tarefa> post(@Valid @RequestBody Tarefa tarefa){
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaRepository.save(tarefa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getById(@PathVariable Long id) {
        return tarefaRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
       return tarefaRepository.findById(id)
               .map(res -> {
                    tarefaRepository.deleteById(id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                })
               .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tarefa>> findAll() {
        if (tarefaRepository.findAll().isEmpty())

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(tarefaRepository.findAll());
    }


    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Tarefa>> getByNome(@PathVariable String nome) {
        List<Tarefa> tarefas = tarefaRepository.findAllByNomeContainingIgnoreCase(nome);
        if (tarefas.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(tarefaRepository.findAllByNomeContainingIgnoreCase(nome));
    }


    @PutMapping
    public ResponseEntity<Tarefa> putTarefa(@Valid @RequestBody Tarefa tarefa) {

        return tarefaRepository.findById(tarefa.getId()).map(resposta ->
                        ResponseEntity.ok().body(tarefaRepository.save(tarefa)))
                .orElse(ResponseEntity.notFound().build());

    }


}