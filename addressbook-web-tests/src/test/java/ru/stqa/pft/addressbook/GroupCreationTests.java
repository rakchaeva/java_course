package ru.stqa.pft.addressbook;

import org.testng.annotations.*;

public class GroupCreationTests extends TestBase {

  @Test
  public void testGroupCreation() throws Exception {
    goToGroupPage();
    initGroupCreation();
    fillGroupForm(new GroupData("TestGroup1", "TestGroup2", "TestGroup3"));
    submitGroupCreation();
    returnToGroupPage();
  }

}
