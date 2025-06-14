package org.example;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClienteApi {

    private static final String BASE_URL = "http://localhost:7000";

    public static void main(String[] args) throws Exception {
        criarTarefa();
        listarTarefas();
        buscarTarefaPorId();
        verificarStatus();
    }

    // POST - criar tarefa
    private static void criarTarefa() throws Exception {
        System.out.println("\n== Criando Tarefa ==");

        URL url = new URL(BASE_URL + "/tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String jsonTarefa = """
        {
            "titulo": "Tarefa criada pelo ClienteApi",
            "descricao": "Descrição da tarefa"
        }
        """;

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonTarefa.getBytes());
        }

        int status = conn.getResponseCode();
        System.out.println("Status POST: " + status);

        String response = lerResposta(conn);
        System.out.println("Resposta: " + response);
    }

    // GET - listar tarefas
    private static void listarTarefas() throws Exception {
        System.out.println("\n== Listando Tarefas ==");

        URL url = new URL(BASE_URL + "/tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        System.out.println("Status GET /tarefas: " + status);

        String response = lerResposta(conn);
        System.out.println("Resposta: " + response);
    }

    // GET - buscar por ID
    private static void buscarTarefaPorId() throws Exception {
        System.out.println("\n== Buscando Tarefa por ID ==");

        URL urlPost = new URL(BASE_URL + "/tarefas");
        HttpURLConnection connPost = (HttpURLConnection) urlPost.openConnection();
        connPost.setRequestMethod("POST");
        connPost.setDoOutput(true);
        connPost.setRequestProperty("Content-Type", "application/json");

        String jsonTarefa = """
        {
            "titulo": "Tarefa para busca por ID",
            "descricao": "Criada para teste de GET por ID"
        }
        """;

        try (OutputStream os = connPost.getOutputStream()) {
            os.write(jsonTarefa.getBytes());
        }

        String responsePost = lerResposta(connPost);

        // extraindo o ID
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responsePost);
        String id = jsonNode.get("id").asText();

        System.out.println("ID criado: " + id);

        // buscando pelo ID
        URL urlGet = new URL(BASE_URL + "/tarefas/" + id);
        HttpURLConnection connGet = (HttpURLConnection) urlGet.openConnection();
        connGet.setRequestMethod("GET");

        int statusGet = connGet.getResponseCode();
        System.out.println("Status GET /tarefas/{id}: " + statusGet);

        String responseGet = lerResposta(connGet);
        System.out.println("Resposta: " + responseGet);
    }

    // GET - status
    private static void verificarStatus() throws Exception {
        System.out.println("\n== Verificando Status ==");

        URL url = new URL(BASE_URL + "/status");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        System.out.println("Status GET /status: " + status);

        String response = lerResposta(conn);
        System.out.println("Resposta: " + response);
    }

    // ler resposta da conexão
    private static String lerResposta(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String linha;
        while ((linha = in.readLine()) != null) {
            response.append(linha);
        }
        in.close();
        return response.toString();
    }
}
