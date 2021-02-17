package se.niyo;

import fileutils.FileReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {



    public static void main(String[] args) {

        ExecutorService exs = Executors.newCachedThreadPool();

        try {
            ServerSocket serverSocket = new ServerSocket(5050);
            System.out.println(Thread.currentThread());

            while (true) {

                Socket soc = serverSocket.accept();
                exs.execute(() -> handleConnection(soc));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleConnection(Socket soc) {

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(soc.getInputStream()));

            String url = readHeaders(input);

            var output = new PrintWriter(soc.getOutputStream());

            File file = new File("Web " + File.separator + url); //"src//rec//index.html"
            byte[] page = FileReader.readFromFile(file);

            String contentType = Files.probeContentType(file.toPath());

            output.println("HTTP/1.1 200 OK");
            output.println("Content-Length:" + page.length);
            output.println("Content-Type: " + contentType); //text/html
            output.println("");


            output.flush();
            var dataOut = new BufferedOutputStream(soc.getOutputStream());
            dataOut.write(page);
            dataOut.flush();
            soc.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readHeaders(BufferedReader input) throws IOException {
        String requestedUrl = "";
        while(true) {
            String headerLine = input.readLine();
            if (headerLine.startsWith("GET")) {
                requestedUrl = headerLine.split(" ")[1];
            }
            System.out.println(headerLine);
            if (headerLine.isEmpty()) {
                break;
            }
        }
        return requestedUrl;
    }
}
