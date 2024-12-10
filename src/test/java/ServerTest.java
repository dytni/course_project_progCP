import com.hatsukha.nikolai.repository.DatabaseManager;
import com.hatsukha.nikolai.server.ClientHandler;
import com.hatsukha.nikolai.server.ServerLogger;
import com.hatsukha.nikolai.service.LoginService;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    private static ServerSocket serverSocket;
    private static ExecutorService executorService;

    @BeforeAll
    static void setupServer() throws IOException {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        LoginService loginService = new LoginService(dbManager);
        ServerLogger logger = new ServerLogger();

        serverSocket = new ServerSocket(8080);
        executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket, loginService, dbManager, logger);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) {
                logger.log("Сервер остановлен.");
            }
        });
    }

    @Test
    void testServerResponse() throws IOException {
        try (Socket clientSocket = new Socket("localhost", 8080);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            // Отправляем запрос на сервер
            out.println("PING");
            String response = in.readLine();

            // Проверяем ответ
            assertEquals("PONG", response);
        }
    }

    @AfterAll
    static void shutdownServer() throws IOException {
        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }
        executorService.shutdown();
    }
}
