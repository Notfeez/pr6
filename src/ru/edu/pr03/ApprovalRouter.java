package ru.edu.pr03;

public class ApprovalRouter {
    public static final long FINANCE_LIMIT = 500_000L;
    private static final long BASE_FAST_TRACK_LIMIT = 500_000L;
    private static final int FAST_TRACK_DISCOUNT_PERCENT = 35;
    public static final long FAST_TRACK_LIMIT = BASE_FAST_TRACK_LIMIT - BASE_FAST_TRACK_LIMIT * FAST_TRACK_DISCOUNT_PERCENT / 100;

    public ApprovalRoute route(long amount, boolean urgent, RiskLevel risk, boolean containsPersonalData) {
        checkInput(amount, risk);
        if (containsPersonalData) {
            return ApprovalRoute.SECURITY_REVIEW;
        }
        if (isHighRisk(risk)) {
            return ApprovalRoute.DIRECTOR_REVIEW;
        }
        if (needsFinance(amount)) {
            return ApprovalRoute.FINANCE_REVIEW;
        }
        if (isFastTrack(amount, urgent)) {
            return ApprovalRoute.FAST_TRACK;
        }
        return ApprovalRoute.STANDARD;
    }

    private void checkInput(long amount, RiskLevel risk) {
        if (amount < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + amount);
        }
        if (risk == null) {
            throw new IllegalArgumentException("Уровень риска не задан");
        }
    }

    private boolean isHighRisk(RiskLevel risk) {
        return risk == RiskLevel.HIGH;
    }

    private boolean needsFinance(long amount) {
        return amount >= FINANCE_LIMIT;
    }

    private boolean isFastTrack(long amount, boolean urgent) {
        return urgent && amount < FAST_TRACK_LIMIT;
    }
}
