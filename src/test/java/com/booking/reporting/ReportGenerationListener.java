package com.booking.reporting;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class ReportGenerationListener implements TestExecutionListener {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        File jsonReportDir = new File("target/cucumber-reports");
        File jsonFile = new File(jsonReportDir, "cucumber.json");

        if (!jsonFile.exists()) {
            System.out.println("[ReportGenerationListener] No cucumber.json found, skipping report generation");
            return;
        }
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        File reportOutputDirectory = new File("target/cucumber-html-reports/report-" + timestamp);
        List<String> jsonFiles = Collections.singletonList(jsonFile.getAbsolutePath());

        Configuration configuration = new Configuration(reportOutputDirectory, "API_Testing_Kata");
        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();

        System.out.println("[ReportGenerationListener] Cucumber HTML report generated at: "
                + reportOutputDirectory.getAbsolutePath());
    }
}
