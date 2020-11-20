package ru.stqa.pft.github.tests;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

    @Test
    public void testCommits() throws IOException {
        Github github = new RtGithub("512f1e9f9d4dee41ca589b13e214c8d21707efaf");
        RepoCommits commits = github.repos()
                .get(new Coordinates.Simple("rakchaeva", "java_course"))
                .commits();
        for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
            System.out.println(new RepoCommit.Smart(commit).message());
        }
    }
}
