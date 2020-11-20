package ru.stqa.pft.rest.tests;

import com.jayway.restassured.RestAssured;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.stqa.pft.rest.model.Issue;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;

import static org.testng.Assert.*;

public class RestAssuredTests extends TestBase {

    @BeforeClass
    public void init() {
        RestAssured.authentication = RestAssured.basic(app.getProperty("bugify.login"),
                app.getProperty("bugify.password"));
    }

    @Test
    public void testCreateIssue() {
        try {
            skipIfNotFixed(429);
        } catch (SkipException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        Set<Issue> oldIssues = app.restAssured().getIssues();
        Issue newIssue = new Issue()
                .withSubject("Test Issue KR RA")
                .withDescription("Test Issue KR RA Description");
        int issueId = app.restAssured().createIssue(newIssue);
        Set<Issue> newIssues = app.restAssured().getIssues();
        oldIssues.add(newIssue.withId(issueId));
        assertEquals(newIssues, oldIssues);
    }

    @Test(enabled = false)
    public void testOpenIssue() {
        Issue issue = app.restAssured().getIssue(429);
        assertEquals(issue.getStatus(), "Open");
        assertTrue(isIssueOpen(issue.getId()));
    }

    @Test(enabled = false)
    public void testClosedIssue() {
        Issue issue = app.restAssured().getIssue(428);
        assertEquals(issue.getStatus(), "Closed");
        assertFalse(isIssueOpen(issue.getId()));
    }

}
