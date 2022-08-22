package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Map;

public class MockFtpClient extends AbstractFtpClient{

    // emulating server with local json file
    public static final String STUDENTS_FILE_MOCK = "src/main/resources/students.json";

    public MockFtpClient(String user, String password, String server, int port) {
        super(user, password, server, port);
    }

    @Override
    public void open() throws IOException, ParseException {
        try(BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE_MOCK))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String jsonText = sb.toString();
            Map<Long,String> students = jsonTextToStudents(jsonText);
            setStudents(students);
            setMaxId(students.keySet().stream().max(Comparator.<Long>naturalOrder()).orElse(0L));
        }
    }

    @Override
    public void close() throws IOException {
        String jsonText = studentsToJsonText();
        FileWriter myWriter = new FileWriter(STUDENTS_FILE_MOCK);
        myWriter.write(jsonText);
        myWriter.close();
    }
}
