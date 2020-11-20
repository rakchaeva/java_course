package ru.stqa.pft.rest.tests;

import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.rest.appmanager.ApplicationManager;

import java.io.IOException;

public class TestBase {

    protected static final ApplicationManager app = new ApplicationManager();

    @BeforeSuite
    public void setUp() throws IOException {
        app.init();
    }

    // should be used for tests with REST Assured only
    public void skipIfNotFixed(int issueId) {
        if (isIssueOpen(issueId)) {
            throw new SkipException("Ignored because of issue " + issueId);
        }
    }

    // should be used for tests with REST Assured only
    public boolean isIssueOpen(int issueId) {
        return !app.restAssured().getIssue(issueId).getStatus().equals("Closed");
    }
}
