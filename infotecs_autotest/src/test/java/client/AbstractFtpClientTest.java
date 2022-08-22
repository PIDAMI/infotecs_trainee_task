package client;

import domain.Student;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public abstract class AbstractFtpClientTest {

    protected AbstractFtpClient ftpClient;

    @DataProvider(name = "dataGetAll")
    public Object[][] getDataForGetAll() {
        return new Object[][]{
            {Stream.of(new Student(5L, "Astudent"),
                new Student(1L, "Student1"),
                new Student(2L, "Student2"),
                new Student(3L, "Student3"),
                new Student(4L, "Student3")
                   )
                   .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList())
            },
        };
    }

    // checks that method works as intended and students are sorted by name
    @Test(dataProvider = "dataGetAll")
    public void checkGetAll(List<Student> expected) throws IOException {
        List<Student> actual = ftpClient.getAll();
        Assert.assertEquals(actual, expected);
    }

    @DataProvider(name = "dataGetById")
    public Object[][] getDataForGetById() {
        return new Object[][]{
            { 1L, "Student1" },
            { 4L, "Student3" },
            { -1L, null },
        };
    }

    // try getting 2 existing students and one non-existing, former must return null
    @Test(dataProvider = "dataGetById")
    public void checkGetById(Long id, String expectedName) throws IOException {
        String actualName = ftpClient.getById(id);
        Assert.assertEquals(actualName, expectedName);
    }

    // try adding student w/ new name, student whos name already exists in students list
    // and student w/ empty name
    @DataProvider(name = "dataAddStudent")
    public Object[][] getDataForAddStudent() {
        return new Object[][]{
            { "totallyNewStudent" },
            { "Student1" },
            { "" },
        };
    }

    @Test(dataProvider = "dataAddStudent")
    public void checkAddStudent(String student) throws IOException, ParseException {
        List<Student> oldMatchStudents = ftpClient.getAll();
        ftpClient.addStudent(student);
        List<Student> newMatchStudents = ftpClient.getAll();
        Assert.assertEquals(newMatchStudents.size(), oldMatchStudents.size() + 1);
    }


    // try removing 1 existing student and 1 non-existing
    @DataProvider(name = "dataRemoveStudent")
    public Object[][] getDataForRemoveStudent() {
        return new Object[][]{
            { 1L, "Student1" },
            { 4L, "Student3" },
            { -1L, null },
        };
    }

    @Test(dataProvider = "dataRemoveStudent")
    public void checkRemoveStudent(Long id, String expectedName) {
        String actualRemovedStudent = ftpClient.removeStudentById(id);
        Assert.assertEquals(actualRemovedStudent, expectedName);
        String alreadyRemovedStudent = ftpClient.getById(id);
        Assert.assertNull(alreadyRemovedStudent);
    }

    // try removing 1 existing student and 1 non-existing
    @DataProvider(name = "dataStateSaved")
    public Object[][] getDataForStateSaved() {
        return new Object[][]{
            { "Student1" },
            { "newStudent" },
        };
    }

    // check that progress's been saved after exiting current session
    @Test(dataProvider = "dataStateSaved")
    public void checkActualStateSaved(String student) throws IOException, ParseException {
        ftpClient.addStudent(student);

        List<Student> oldStudents = ftpClient.getAll();
        ftpClient.removeStudentById(oldStudents.get(0).getId());


        ftpClient.close();
        ftpClient.open();
        List<Student> matchStudentsAfterSaving = ftpClient.getAll();
        Assert.assertEquals(matchStudentsAfterSaving.size(), oldStudents.size() - 1);
    }

}