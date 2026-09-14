package com.booking.reporting;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

        generateMasterthoughtReport(jsonFile, timestamp);
        archiveSingleFileHtmlReport(timestamp);
    }

    private void generateMasterthoughtReport(File jsonFile, String timestamp) {
        File reportOutputDirectory = new File("target/cucumber-html-reports/report-" + timestamp);
        List<String> jsonFiles = Collections.singletonList(jsonFile.getAbsolutePath());

        Configuration configuration = new Configuration(reportOutputDirectory, "API_Testing_Kata");
        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();

        System.out.println("[ReportGenerationListener] Cucumber HTML report generated at: "
                + reportOutputDirectory.getAbsolutePath());
    }

    private void archiveSingleFileHtmlReport(String timestamp) {
        File singleFileReport = new File("target/cucumber-html-report.html");
        if (!singleFileReport.exists()) {
            return;
        }

        File archivedCopy = new File("target/cucumber-html-report-" + timestamp + ".html");
        try {
            Files.copy(singleFileReport.toPath(), archivedCopy.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[ReportGenerationListener] Single-file HTML report archived at: "
                    + archivedCopy.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[ReportGenerationListener] Failed to archive single-file HTML report: " + e.getMessage());
        }
    }
}