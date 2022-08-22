package client;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.stream.Collectors;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class MockFtpClientTest extends AbstractFtpClientTest{

    @BeforeMethod()
    public void setup() throws IOException, ParseException {
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(AbstractFtpClient.STUDENT_FILE);
        String jsonText = new BufferedReader(new InputStreamReader(resourceStream))
            .lines().collect(Collectors.joining("\n"));

        FileWriter myWriter = new FileWriter(MockFtpClient.STUDENTS_FILE_MOCK);
        myWriter.write(jsonText);
        myWriter.close();

        ftpClient = new MockFtpClient("zxc", "zxc", "zxc", 1488);
        ftpClient.open();
    }

    @AfterMethod()
    public void teardown() throws IOException, ParseException {
        ftpClient.close();
    }
}
