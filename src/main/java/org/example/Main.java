package org.example;
import io.javalin.Javalin;
import java.time.Instant;
import java.util.*;

/**
 * classe principal que configura e inicia o servidor javalin
 * aqui define os endpoints da API REST
 */

public class Main {

    public static Javalin criarApp() {
        Javalin app = Javalin.create();

        // lista que armazena as tarefas criadas
        List<Tarefa> tarefas = new ArrayList<>();

        // endpoint /hello retornando uma mensagem de saudaçao
        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        // endpoint /status retornando o status da API
        app.get("/status", ctx -> {
            ctx.json(Map.of(
                    "status", "ok",
                    "timestamp", Instant.now().toString()
            ));
        });

        // endpoint /echo que recebe um JSON e devolve o mesmo conteudo como resposta
        app.post("/echo", ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            ctx.json(body);
        });

        // endpoint /saudacao/{nome} retornando uma saudaçao com nome fornecido
        app.get("/saudacao/{nome}", ctx -> {
            String nome = ctx.pathParam("nome");
            ctx.json(Map.of("mensagem", "Olá, " + nome + "!"));
        });

        // endpoint /tarefas que cria uma nova tarefa
        app.post("/tarefas", ctx -> {
            Tarefa novaTarefa = ctx.bodyAsClass(Tarefa.class);

            // validaçao to titulo que é obrigatorio
            if (novaTarefa.getTitulo() == null || novaTarefa.getTitulo().isBlank()) {
                ctx.status(400).result("O título é obrigatório");
                return;
            }

            // preenche os campos antes de enviar
            novaTarefa.setId(UUID.randomUUID().toString());
            novaTarefa.setConcluida(false);
            novaTarefa.setDataCriacao(Instant.now().toString());

            tarefas.add(novaTarefa);

            // retorna status 201 com a tarefa criada
            ctx.status(201).json(novaTarefa);
        });

        // endpoint /tarefas que lista todas as tarefas cadastradas
        app.get("/tarefas", ctx -> {
            ctx.json(tarefas);
        });

        // endpoint /tarefas/{id} que busca uma tarefa especifica pelo ID
        app.get("/tarefas/{id}", ctx -> {
            String id = ctx.pathParam("id");

            Optional<Tarefa> tarefa = tarefas.stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst();

            if (tarefa.isPresent()) {
                ctx.json(tarefa.get());
            } else {
                ctx.status(404).result("Tarefa não encontrada");
            }
        });

        return app;
    }

    // inicia o servidor javalin na porta 7000
    public static void main(String[] args) {
        criarApp().start(7000);
    }
}
