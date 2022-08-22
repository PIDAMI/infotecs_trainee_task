package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;


public class FtpClient extends AbstractFtpClient{

    private final String ftpUrl;

    public FtpClient(String user, String password, String server,int port) {
        super(user, password, server, port);
        ftpUrl =
            "ftp://" + user + ":" + password + "@" + server + ":" + port + "/" + STUDENT_FILE;
    }

    @Override
    public void open() throws IOException, ParseException {
        Map<Long, String> students = downloadStudents();
        setStudents(students);
        setMaxId(students.keySet().stream().max(Comparator.<Long>naturalOrder()).orElse(0L));
    }

    @Override
    public void close() throws IOException, ParseException {
        URLConnection urlConnection = new URL(ftpUrl).openConnection();
        String jsonText = studentsToJsonText();
        OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write(jsonText.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    private Map<Long,String> downloadStudents() throws IOException, ParseException {
        URLConnection urlConnection = new URL(ftpUrl).openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        String jsonText = new BufferedReader(new InputStreamReader(inputStream))
            .lines().collect(Collectors.joining("\n"));
        inputStream.close();

        return jsonTextToStudents(jsonText);
    }
}

