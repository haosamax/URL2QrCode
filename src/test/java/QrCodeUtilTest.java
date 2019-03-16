import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

class QrCodeUtilTest {

    @org.junit.jupiter.api.Test
    void creatQrCode() {
        String url = "https://www.baidu.com";
        String path = FileSystemView.getFileSystemView().getHomeDirectory()
                + File.separator + "testQrcode";
        QrCodeUtil.creatQrCode(url, path, "qrcode.jpg");
    }

    @org.junit.jupiter.api.Test
    void mergeBothImage() {
        String backPath = FileSystemView.getFileSystemView().getHomeDirectory()
                + File.separator + "testQrcode" + File.separator + "box.jpg";
        String addPath = FileSystemView.getFileSystemView().getHomeDirectory()
                + File.separator + "testQrcode" + File.separator + "qrcode.jpg";
        String toPath = FileSystemView.getFileSystemView().getHomeDirectory()
                + File.separator + "testQrcode" + File.separator + "combined.jpg";
        try {
            QrCodeUtil.mergeBothImage(backPath, addPath, toPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}