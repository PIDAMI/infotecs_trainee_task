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
//import org.apache.commons.net.PrintCommandListener;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPFile;
//import org.apache.commons.net.ftp.FTPReply;

public class FtpClient {

    public final static String STUDENT_FILE = "students.json";
    private final String ftpUrl;
    private final String newFtpUrl;

    private Map<Long, String> students;
    private Long maxId;

    public FtpClient(String user, String password, String server,int port) {
        ftpUrl =
            "ftp://" + user + ":" + password + "@" + server + ":" + port + "/" + STUDENT_FILE;
        newFtpUrl = "ftp://" + user + ":" + password + "@" + server + ":" + port + "/" + "sstudents.json";

    }


    public void open() throws IOException, ParseException {
        students = downloadStudents();
        maxId = students.keySet().stream().max(Comparator.<Long>naturalOrder()).orElse(0L);
    }

    public void close() throws IOException, ParseException {
        URLConnection urlConnection = new URL(ftpUrl).openConnection();
        JSON[] studentsJson = students.entrySet()
                                      .stream()
                                      .map(entry -> new Student(entry.getKey(), entry.getValue()).toJSON())
                                      .toArray(JSON[]::new);
        String jsonText = new JSON().set("students", studentsJson).toString();
        OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write(jsonText.getBytes(StandardCharsets.UTF_8));
        outputStream.close();

        downloadStudents().forEach((key, value) -> System.out.println(value + key));
    }

    private Map<Long,String> downloadStudents() throws IOException, ParseException {
        URLConnection urlConnection = new URL(ftpUrl).openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        String result = new BufferedReader(new InputStreamReader(inputStream))
            .lines().collect(Collectors.joining("\n"));
        inputStream.close();

        Object[] jsonArray = new JSON(result).getArray("students");
        return Arrays.stream(jsonArray)
                     .map(x -> Student.fromJSON((JSON)x))
                     .collect(Collectors.toMap(Student::getId, Student::getName));
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

