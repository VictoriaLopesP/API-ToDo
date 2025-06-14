# API-ToDo
Projeto de exemplo desenvolvido em Java utilizando o framework Javalin para criação de uma API REST simples para gerenciamento de tarefas.

  Funcionalidades da API:

    GET /hello -> Retorna uma saudação simples

    GET /status -> Retorna o status do servidor com timestamp

    POST /echo -> Retorna o corpo da requisição recebido

    GET /saudacao/{nome} -> Retorna uma saudação personalizada

    POST /tarefas -> Cria uma nova tarefa (JSON com título e descrição)

    GET /tarefas -> Lista todas as tarefas

    GET /tarefas/{id} -> Retorna os detalhes de uma tarefa específica por ID

  Como rodar o projeto:

    Inicie a aplicação executando a classe Main.java
    
    A API ficará disponível em: http://localhost:7000
  
    Os testes estão na classe: src/test/java/org/example/TarefaApiHttpTest.java

Projeto desenvolvido para fins acadêmicos
