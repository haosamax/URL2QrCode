import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TestQRCode {

    public static void main(String[] args) throws IOException {
        String url = "https://www.baidu.com";
        String path = FileSystemView.getFileSystemView().getHomeDirectory()
                + File.separator + "testQrcode";
        System.out.println(path);
        String qrCodeName = "qrcode.jpg";
        createQrCode(url, path, qrCodeName);



//        // load source images
//        BufferedImage image = ImageIO.read(new File(path, "qrcode.jpg"));
//        BufferedImage overlay = ImageIO.read(new File(path, "box.jpg"));
//
//        // create the new image, canvas size is the max. of both image sizes
//        int w = Math.max(image.getWidth(), overlay.getWidth());
//        int h = Math.max(image.getHeight(), overlay.getHeight());
//        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//
//        // paint both images, preserving the alpha channels
//        Graphics g = combined.getGraphics();
//        g.drawImage(image, 0, 0, null);
//        g.drawImage(overlay, 0, 0, null);
//
//        // Save as new image
//        ImageIO.write(combined, "PNG", new File(path, "combined.png"));

        mergeBothImage(path+File.separator+"box.jpg",
                path + File.separator+"qrcode.jpg", 20,20,
                path + File.separator+"combined.jpg");
        System.out.println("合并成功！");
    }

    public static String createQrCode(String url, String path, String fileName) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            File file = new File(path, fileName);
            if (file.exists() || ((file.getParentFile().exists() || file.getParentFile().mkdirs()) && file.createNewFile())) {
                writeToFile(bitMatrix, "jpg", file);
                System.out.println("测试完成：" + file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 合并图片(按指定初始x、y坐标将附加图片贴到底图之上)
     * @param negativeImagePath 背景图片路径
     * @param additionImagePath 附加图片路径
     * @param x 附加图片的起始点x坐标
     * @param y  附加图片的起始点y坐标
     * @param toPath 图片写入路径
     * @throws IOException
     */
    public static void mergeBothImage(String negativeImagePath,String additionImagePath,int x,int y,String toPath ) throws IOException{
        InputStream is= null;
        InputStream is2= null;
        OutputStream os = null;
        try{
            is=new FileInputStream(negativeImagePath);
            is2=new FileInputStream(additionImagePath);
            BufferedImage image=ImageIO.read(is);
            BufferedImage image2=ImageIO.read(is2);
            Graphics g=image.getGraphics();
            g.drawImage(image2,x,y,null);
            os = new FileOutputStream(toPath);
            JPEGImageEncoder enc= JPEGCodec.createJPEGEncoder(os);
            enc.encode(image);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(os != null){
                os.close();
            }
            if(is2 != null){
                is2.close();
            }
            if(is != null){
                is.close();
            }
        }
    }

}
