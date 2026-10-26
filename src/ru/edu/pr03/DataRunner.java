package ru.edu.pr03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataRunner {
    public static void main(String[] args) throws IOException {
        String path = args.length > 0 ? args[0] : "data/sample.csv";
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        ApprovalRouter router = new ApprovalRouter();
        RequestParser parser = new RequestParser();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            try {
                System.out.println(line + " -> " + parser.routeFromLine(line, router));
            } catch (IllegalArgumentException e) {
                System.out.println(line + " -> ОШИБКА: " + e.getMessage());
            }
        }
    }
}
