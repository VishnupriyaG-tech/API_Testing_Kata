package com.booking.reporting;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ReportGenerationListener implements TestExecutionListener {

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        File jsonReportDir = new File("target/cucumber-reports");
        File jsonFile = new File(jsonReportDir, "cucumber.json");

        if (!jsonFile.exists()) {
            System.out.println("[ReportGenerationListener] No cucumber.json found, skipping report generation");
            return;
        }

        File reportOutputDirectory = new File("target/cucumber-html-reports");
        List<String> jsonFiles = Collections.singletonList(jsonFile.getAbsolutePath());

        Configuration configuration = new Configuration(reportOutputDirectory, "API_Testing_Kata");
        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();

        System.out.println("[ReportGenerationListener] Cucumber HTML report generated at: "
                + reportOutputDirectory.getAbsolutePath());
    }
}
