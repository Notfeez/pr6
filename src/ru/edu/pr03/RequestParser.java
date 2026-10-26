package ru.edu.pr03;

public class RequestParser {
    public ApprovalRoute routeFromLine(String line, ApprovalRouter router) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Пустая строка");
        }
        String[] parts = line.split(";", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Ожидалось 4 поля, получено " + parts.length);
        }
        long amount = parseAmount(parts[0].trim());
        boolean urgent = parseBoolean(parts[1].trim(), "urgent");
        RiskLevel risk = parseRisk(parts[2].trim());
        boolean personal = parseBoolean(parts[3].trim(), "personal");
        return router.route(amount, urgent, risk, personal);
    }

    private long parseAmount(String text) {
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Сумма не задана");
        }
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Сумма не является числом: " + text);
        }
    }

    private boolean parseBoolean(String text, String name) {
        if (text.equalsIgnoreCase("true")) {
            return true;
        }
        if (text.equalsIgnoreCase("false")) {
            return false;
        }
        throw new IllegalArgumentException("Поле " + name + " должно быть true или false: " + text);
    }

    private RiskLevel parseRisk(String text) {
        if (text.isEmpty()) {
            throw new IllegalArgumentException("Риск не задан");
        }
        try {
            return RiskLevel.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестный уровень риска: " + text);
        }
    }
}
