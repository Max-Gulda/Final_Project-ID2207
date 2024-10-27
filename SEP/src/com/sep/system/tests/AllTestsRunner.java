package com.sep.system.tests;

public class AllTestsRunner {

    public static void main(String[] args) {
        // Run all test classes
        runEventRequestTest();
        runEventWorkflowTest();
        runFinancialRequestTest();
        runFinancialRequestWorkflowTest();
        runRecruitmentWorkflowTest();
        runStaffRecruitmentRequestTest();
        runTaskWorkflowTest();
    }

    private static void runEventRequestTest() {
        System.out.println("Running EventRequestTest...");
        EventRequestTest.main(new String[0]);
    }

    private static void runEventWorkflowTest() {
        System.out.println("Running EventWorkflowTest...");
        EventWorkflowTest.main(new String[0]);
    }

    private static void runFinancialRequestTest() {
        System.out.println("Running FinancialRequestTest...");
        FinancialRequestTest.main(new String[0]);
    }

    private static void runFinancialRequestWorkflowTest() {
        System.out.println("Running FinancialRequestWorkflowTest...");
        FinancialRequestWorkflowTest.main(new String[0]);
    }

    private static void runRecruitmentWorkflowTest() {
        System.out.println("Running RecruitmentWorkflowTest...");
        RecruitmentWorkflowTest.main(new String[0]);
    }

    private static void runStaffRecruitmentRequestTest() {
        System.out.println("Running StaffRecruitmentRequestTest...");
        StaffRecruitmentRequestTest.main(new String[0]);
    }

    private static void runTaskWorkflowTest() {
        System.out.println("Running TaskWorkflowTest...");
        TaskWorkflowTest.main(new String[0]);
    }
}

