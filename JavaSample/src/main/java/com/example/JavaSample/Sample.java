package com.example.JavaSample;

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

    // プロジェクトのルートディレクトリを取得
    private static final String BASE_PATH = Paths.get("").toAbsolutePath().toString();
    private static final String HTTP_LOGIN = BASE_PATH + "/src/main/resources/web-project/login.html";
    private static final String HTTP_HOME = BASE_PATH + "/src/main/resources/web-project/home.html";
    private static final String HTTP_ABOUT = BASE_PATH + "/src/main/resources/web-project/about.html";
    private static final String HTTP_NEW_ACCOUNT = BASE_PATH + "/src/main/resources/web-project/newAccount.html";
    private static final String HTTP_CONFIRM = BASE_PATH + "/src/main/resources/web-project/confirm.html";
    private static final String HTTP_SUCCESS = BASE_PATH + "/src/main/resources/web-project/success.html";

    public static void main(String[] args) throws IOException {
        // デバッグ: プロジェクトのルートディレクトリを出力
        System.out.println("Base path: " + BASE_PATH);

        // ポート番号8080でHTTPサーバーを作成
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // HTMLファイルの相対パスを管理するマップ
        Map<String, String> pathMap = new HashMap<>();
        pathMap.put("/", HTTP_LOGIN);
        pathMap.put("/home", HTTP_HOME);
        pathMap.put("/about", HTTP_ABOUT);
        pathMap.put("/newAccount", HTTP_NEW_ACCOUNT);
        pathMap.put("/confirm", HTTP_CONFIRM);
        pathMap.put("/success", HTTP_SUCCESS);

        // 各パスに対応するコンテキストを設定
        for (Map.Entry<String, String> entry : pathMap.entrySet()) {
            server.createContext(entry.getKey(), new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    sendResponse(exchange, entry.getValue());
                }
            });
        }

        // サーバーを開始
        server.start();
        System.out.println("Server is running at http://localhost:8080");
    }

    // HTMLファイルを読み込んでレスポンスを返すメソッド
    private static void sendResponse(HttpExchange exchange, String filePath) throws IOException {
        try {
            System.out.println("Attempting to read file: " + filePath); // デバッグログ
            byte[] response = Files.readAllBytes(Paths.get(filePath));
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        } catch (IOException e) {
            System.out.println("File not found: " + filePath); // デバッグログ
            String errorResponse = "<html><body><h1>404 Not Found</h1></body></html>";
            exchange.sendResponseHeaders(404, errorResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(errorResponse.getBytes());
            os.close();
        }
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