package ru.edu.pr03;
public class SelfCheck {
    public static void main(String[] args) {
        boolean ok = false;
        try { ok = (new ApprovalRouter().route(10_000,true,RiskLevel.LOW,true) == ApprovalRoute.SECURITY_REVIEW && new ApprovalRouter().route(500_000,false,RiskLevel.LOW,false) == ApprovalRoute.FINANCE_REVIEW); } catch (Exception e) { System.out.println("FAIL exception " + e.getMessage()); }
        System.out.println(ok ? "PASS" : "FAIL  complete TODO methods");
    }
}
