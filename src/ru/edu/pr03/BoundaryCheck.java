package ru.edu.pr03;

public class BoundaryCheck {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        ApprovalRouter router = new ApprovalRouter();
        long limit = ApprovalRouter.FINANCE_LIMIT;

        expect("граница -1", router.route(limit - 1, false, RiskLevel.LOW, false), ApprovalRoute.STANDARD);
        expect("граница =", router.route(limit, false, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);
        expect("граница +1", router.route(limit + 1, false, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);
        expect("499999", router.route(499_999, false, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);
        expect("500001", router.route(500_001, false, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);
        expect("500000 по условию", router.route(500_000, false, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);
        expect("срочная ниже порога", router.route(limit - 1, true, RiskLevel.LOW, false), ApprovalRoute.FAST_TRACK);
        expect("срочная на пороге", router.route(limit, true, RiskLevel.LOW, false), ApprovalRoute.FINANCE_REVIEW);
        expect("нулевая сумма", router.route(0, false, RiskLevel.LOW, false), ApprovalRoute.STANDARD);
        expect("ПДн и срочность", router.route(10_000, true, RiskLevel.LOW, true), ApprovalRoute.SECURITY_REVIEW);
        expect("ПДн и большая сумма", router.route(900_000, false, RiskLevel.HIGH, true), ApprovalRoute.SECURITY_REVIEW);
        expect("HIGH и срочность", router.route(10_000, true, RiskLevel.HIGH, false), ApprovalRoute.DIRECTOR_REVIEW);
        expect("HIGH и большая сумма", router.route(900_000, false, RiskLevel.HIGH, false), ApprovalRoute.DIRECTOR_REVIEW);
        expect("MEDIUM как LOW", router.route(10_000, false, RiskLevel.MEDIUM, false), ApprovalRoute.STANDARD);

        expectError("отрицательная сумма", () -> router.route(-1, false, RiskLevel.LOW, false));
        expectError("риск null", () -> router.route(1000, false, null, false));

        RequestParser parser = new RequestParser();
        expect("строка файла", parser.routeFromLine("120000;true;LOW;false", router), ApprovalRoute.FAST_TRACK);
        expect("строка с пробелами", parser.routeFromLine(" 500000 ; false ; low ; false ", router), ApprovalRoute.FINANCE_REVIEW);
        expectError("пустая строка", () -> parser.routeFromLine("", router));
        expectError("null строка", () -> parser.routeFromLine(null, router));
        expectError("мало полей", () -> parser.routeFromLine("1000;true;LOW", router));
        expectError("пустая сумма", () -> parser.routeFromLine(";true;LOW;false", router));
        expectError("сумма не число", () -> parser.routeFromLine("abc;true;LOW;false", router));
        expectError("дробная сумма", () -> parser.routeFromLine("1000.5;true;LOW;false", router));
        expectError("неверный риск", () -> parser.routeFromLine("1000;true;EXTREME;false", router));
        expectError("пустой риск", () -> parser.routeFromLine("1000;true;;false", router));
        expectError("неверный boolean", () -> parser.routeFromLine("1000;yes;LOW;false", router));

        ApprovalRoute first = router.route(250_000, true, RiskLevel.MEDIUM, false);
        ApprovalRoute second = router.route(250_000, true, RiskLevel.MEDIUM, false);
        expect("повторный запуск", second, first);

        System.out.println("Прошло: " + passed + ", не прошло: " + failed);
    }

    private static void expect(String name, ApprovalRoute actual, ApprovalRoute expected) {
        if (actual == expected) {
            passed++;
            System.out.println("OK   " + name + " -> " + actual);
        } else {
            failed++;
            System.out.println("FAIL " + name + ": ожидалось " + expected + ", получено " + actual);
        }
    }

    private static void expectError(String name, Runnable action) {
        try {
            action.run();
            failed++;
            System.out.println("FAIL " + name + ": ошибка не возникла");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("OK   " + name + " -> " + e.getMessage());
        }
    }
}
