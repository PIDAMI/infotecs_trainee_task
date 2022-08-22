//package client;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.text.ParseException;
//import java.util.stream.Collectors;
//import org.mockftpserver.fake.FakeFtpServer;
//import org.mockftpserver.fake.UserAccount;
//import org.mockftpserver.fake.filesystem.DirectoryEntry;
//import org.mockftpserver.fake.filesystem.FileEntry;
//import org.mockftpserver.fake.filesystem.FileSystem;
//import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Ignore;
//
//
//public class FtpClientTest extends AbstractFtpClientTest{
//
//    private FakeFtpServer fakeFtpServer;
//
//    private static final String FAKE_FTP_HOME_DIRECTORY = "/data";
//
//    @BeforeMethod()
//    public void setup() throws IOException, ParseException {
//        fakeFtpServer = new FakeFtpServer();
//        fakeFtpServer.addUserAccount(new UserAccount("user", "password", FAKE_FTP_HOME_DIRECTORY));
//
//        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(AbstractFtpClient.STUDENT_FILE);
//
//        String jsonText = new BufferedReader(new InputStreamReader(resourceStream))
//            .lines().collect(Collectors.joining("\n"));
//
//        FileSystem fileSystem = new UnixFakeFileSystem();
//        fileSystem.add(new DirectoryEntry(FAKE_FTP_HOME_DIRECTORY));
//        fileSystem.add(new FileEntry(FAKE_FTP_HOME_DIRECTORY + "/" + AbstractFtpClient.STUDENT_FILE, jsonText));
//        fakeFtpServer.setFileSystem(fileSystem);
//        fakeFtpServer.setServerControlPort(0);
//
//        fakeFtpServer.start();
//
//        ftpClient = new FtpClient("user", "password", "localhost", fakeFtpServer.getServerControlPort());
//        ftpClient.open();
//    }
//
//    @AfterMethod()
//    public void teardown() throws IOException, ParseException {
//        ftpClient.close();
//        fakeFtpServer.stop();
//    }
//}