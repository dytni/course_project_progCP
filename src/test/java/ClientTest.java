import org.junit.jupiter.api.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {

    private static ServerSocket mockServerSocket;

    @BeforeAll
    static void setupMockServer() throws IOException {
        mockServerSocket = new ServerSocket(8080);

        // Запускаем мок-сервер для имитации ответов
        new Thread(() -> {
            try {
                while (!mockServerSocket.isClosed()) {
                    Socket socket = mockServerSocket.accept();
                    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        String input = in.readLine();
                        if ("PING".equals(input)) {
                            out.println("PONG");
                        } else {
                            out.println("UNKNOWN_COMMAND");
                        }
                    }
                }
            } catch (IOException e) {
                // Игнорируем при закрытии
            }
        }).start();
    }

    @Test
    void testClientRequest() throws IOException {
        try (Socket clientSocket = new Socket("localhost", 8080);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            // Отправляем запрос от имени клиента
            out.println("PING");
            String response = in.readLine();

            // Проверяем ответ
            assertEquals("PONG", response);
        }
    }

    @AfterAll
    static void shutdownMockServer() throws IOException {
        if (!mockServerSocket.isClosed()) {
            mockServerSocket.close();
        }
    }
}
