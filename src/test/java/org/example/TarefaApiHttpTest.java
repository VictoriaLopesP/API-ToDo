package org.example;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * classe de testes para a API usando HttpURLConnection
 * cada teste realiza chamadas na API rodando na porta 7000
 */


public class TarefaApiHttpTest {

    /**
     * testa o endpoint /hello e verifica se o status é 200 e a resposta é "Hello, Javalin!"
     */

    @Test
    public void testHelloEndpoint() throws Exception {
        URL url = new URL("http://localhost:7000/hello");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = in.readLine();
        in.close();

        assertEquals("Hello, Javalin!", response);
    }

    /**
     * testa o endpoint /tarefas criando uma nova tarefa e ai verifica se o status de criação 201 é retornado
     */

    @Test
    public void testCriarTarefa() throws Exception {
        URL url = new URL("http://localhost:7000/tarefas");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        String jsonTarefa = """
        {
            "titulo": "Teste via HttpURLConnection",
            "descricao": "Tarefa criada no teste"
        }
        """;

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonTarefa.getBytes());
        }

        int responseCode = connection.getResponseCode();
        assertEquals(201, responseCode, "O código de status deve ser 201");
    }

    /**
     * testa o endpoint POST /tarefas criando uma nova tarefa e verifica se o status de criação 201 é retornado
     */

    @Test
    public void testListarTarefas() throws Exception {
        URL url = new URL("http://localhost:7000/tarefas");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String linha;
        while ((linha = in.readLine()) != null) {
            response.append(linha);
        }
        in.close();

        String respostaString = response.toString();

        // verifica se a resposta é um array JSON
        assertTrue(respostaString.startsWith("[") && respostaString.endsWith("]"), "resposta deve ser um array JSON");

        // criei uma resposta pra debug
        System.out.println("Resposta /tarefas: " + respostaString);
    }


    /**
     * testa o fluxo completo e busca por ID
     */
    @Test
    public void testBuscarTarefaPorId() throws Exception {
        // Primeiro, cria uma nova tarefa (POST)
        URL urlPost = new URL("http://localhost:7000/tarefas");
        HttpURLConnection connectionPost = (HttpURLConnection) urlPost.openConnection();
        connectionPost.setRequestMethod("POST");
        connectionPost.setDoOutput(true);
        connectionPost.setRequestProperty("Content-Type", "application/json");

        String jsonTarefa = """
        {
            "titulo": "Tarefa de teste",
            "descricao": "Teste buscar por ID"
        }
    """;

        try (OutputStream os = connectionPost.getOutputStream()) {
            os.write(jsonTarefa.getBytes());
        }

        int responseCodePost = connectionPost.getResponseCode();
        assertEquals(201, responseCodePost);

        BufferedReader readerPost = new BufferedReader(new InputStreamReader(connectionPost.getInputStream()));
        StringBuilder responsePost = new StringBuilder();
        String linePost;
        while ((linePost = readerPost.readLine()) != null) {
            responsePost.append(linePost);
        }
        readerPost.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responsePost.toString());
        String id = jsonNode.get("id").asText();

        URL urlGet = new URL("http://localhost:7000/tarefas/" + id);
        HttpURLConnection connectionGet = (HttpURLConnection) urlGet.openConnection();
        connectionGet.setRequestMethod("GET");

        int responseCodeGet = connectionGet.getResponseCode();
        assertEquals(200, responseCodeGet);

        BufferedReader readerGet = new BufferedReader(new InputStreamReader(connectionGet.getInputStream()));
        StringBuilder responseGet = new StringBuilder();
        String lineGet;
        while ((lineGet = readerGet.readLine()) != null) {
            responseGet.append(lineGet);
        }
        readerGet.close();

        // verifica se a resposta contém o titulo
        String responseGetString = responseGet.toString();
        assertTrue(responseGetString.contains("Tarefa de teste"));
    }

}
