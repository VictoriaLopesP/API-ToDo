package org.example;
import java.time.Instant;
import java.util.UUID;

/**
 * classe que representa uma tarefa no sistema
 * contem atributos pro gerenciamento de tarefas
 */

public class Tarefa {
    private String id; // UUID da tarefa
    private String titulo; // titulo da tarefa
    private String descricao; // descriçao da tarefa
    private boolean concluida; // indica se a tarefa foi concluida
    private String dataCriacao; // data e hora da criaçao da tarefa

    public Tarefa() {
        // construtor vazio pro javalin converter JSON pra objeto
    }

    public Tarefa(String titulo, String descricao) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = false;
        this.dataCriacao = Instant.now().toString();
    }

    // getters e setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
