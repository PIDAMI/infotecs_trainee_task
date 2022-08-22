package client;

import domain.Student;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import json.JSON;

public abstract class AbstractFtpClient {

    public final static String STUDENT_FILE = "students.json";
    private Map<Long, String> students;
    private Long maxId;

    public AbstractFtpClient(String user, String password, String server,int port) {}

    public abstract void open() throws IOException, ParseException;

    public abstract void close() throws IOException, ParseException;

    protected String studentsToJsonText() {
        JSON[] studentsJson = getStudents().entrySet()
                                           .stream()
                                           .map(entry -> new Student(entry.getKey(), entry.getValue()).toJSON())
                                           .toArray(JSON[]::new);
        return new JSON().set("students", studentsJson).toString();
    }

    protected Map<Long,String> jsonTextToStudents(String jsonText) throws IOException, ParseException {
        Object[] jsonArray = new JSON(jsonText).getArray("students");
        return Arrays.stream(jsonArray)
                          .map(x -> Student.fromJSON((JSON)x))
                          .collect(Collectors.toMap(Student::getId, Student::getName));
    }

    protected void setStudents(Map<Long, String> students) {
        this.students = students;
    }

    protected Map<Long, String> getStudents() {
        return students;
    }

    public void setMaxId(java.lang.Long maxId) {
        this.maxId = maxId;
    }


    // returns null if student w/ given id doesnt exist
    // which is generally a bad practice but ...
    public String getById(Long id) {
        return students.get(id);
    }

    public void addStudent(String name) {
        maxId += 1;
        students.put(maxId, name);
    }

    public String removeStudentById(Long id) {
        return students.remove(id);
    }

    public List<Student> getAll() {
        return students.entrySet()
                       .stream()
                       .sorted(Entry.comparingByValue())
                       .map(entry -> new Student(entry.getKey(), entry.getValue()))
                       .collect(Collectors.toList());
    }
}
