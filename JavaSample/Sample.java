package JavaSample;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Sample {
    public static void main(String[] args) throws IOException {
        // ポート番号8080でHTTPサーバーを作成
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // "/login"にアクセスしたときにlogin.htmlを返す
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("POST".equals(exchange.getRequestMethod())) {
                    // ログイン処理
                    Map<String, String> params = parseFormData(exchange);
                    String username = params.get("username");
                    String password = params.get("password");

                    if ("user".equals(username) && "pass".equals(password)) {
                        // ログイン成功時にホーム画面へリダイレクト
                        exchange.getResponseHeaders().add("Location", "/home");
                        exchange.sendResponseHeaders(302, -1);
                    } else {
                        // ログイン失敗時にエラーメッセージを表示
                        String response = "<h1>ログイン失敗</h1><a href='/'>戻る</a>";
                        exchange.sendResponseHeaders(200, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } else {
                    // ログイン画面を表示
                    sendResponse(exchange, "/Users/hikaru/workspase/java/web-project/src/login.html");
                }
            }
        });

        // "/home"にアクセスしたときにhome.htmlを返す
        server.createContext("/home", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                sendResponse(exchange, "/Users/hikaru/workspase/java/web-project/src/home.html");
            }
        });

        // "/about"にアクセスしたときにabout.htmlを返す
        server.createContext("/about", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                sendResponse(exchange, "/Users/hikaru/workspase/java/web-project/src/about.html");
            }
        });

        // "/newAccount"にアクセスしたときにnewAccount.htmlを返す
        server.createContext("/newAccount", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                sendResponse(exchange, "/Users/hikaru/workspase/java/web-project/src/newAccount.html");
            }
        });

        // "/confirm"にアクセスしたときにconfirm.htmlを返す
        server.createContext("/confirm", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                sendResponse(exchange, "/Users/hikaru/workspase/java/web-project/src/confirm.html");
            }
        });

        // "/success"にアクセスしたときにsuccess.htmlを返す
        server.createContext("/success", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                sendResponse(exchange, "/Users/hikaru/workspase/java/web-project/src/success.html");
            }
        });

        // サーバーを開始
        server.start();
        System.out.println("Server is running at http://localhost:8080");
    }

    // HTMLファイルを読み込んでレスポンスを返すメソッド
    private static void sendResponse(HttpExchange exchange, String filePath) throws IOException {
        byte[] response = Files.readAllBytes(Paths.get(filePath));
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    // フォームデータを解析するメソッド
    private static Map<String, String> parseFormData(HttpExchange exchange) throws IOException {
        Map<String, String> params = new HashMap<>();
        String body = new String(exchange.getRequestBody().readAllBytes());
        for (String pair : body.split("&")) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }
}