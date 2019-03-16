import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成二维码工具类
 */
public class QrCodeUtil {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     *
     * @param url
     * @param path
     * @param name
     * @return
     */
    public static File creatQrCode(String url, String path, String name) {
        File file = new File(path, name);
        Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            if (file.exists() || ((file.getParentFile().exists() || file.getParentFile().mkdirs()) && file.createNewFile())) {
                writeToFile(bitMatrix, "jpg", file);
                System.out.println("测试完成：" + file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 合并图片(按指定初始x、y坐标将附加图片贴到底图之上。此处放中间)
     * @param backPath 背景图片路径
     * @param additionPath 附加图片路径
     * @param toPath 生成图片的路径
     * @return
     * @throws IOException
     */
    public static OutputStream mergeBothImage(String backPath,String additionPath, String toPath) throws IOException{
        InputStream back= null;
        InputStream add= null;
        OutputStream os = null;
        try{
            back = new FileInputStream(backPath);
            add = new FileInputStream(additionPath);
            BufferedImage backImage = ImageIO.read(back);
            BufferedImage addImage = ImageIO.read(add);
            Graphics g = backImage.getGraphics();
            int x = Math.abs(backImage.getWidth()-addImage.getWidth()) >> 1;
            int y = Math.abs(backImage.getHeight()-addImage.getHeight()) >> 1;
            g.drawImage(addImage,x,y,null);
            os = new FileOutputStream(toPath);
            JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(os);
            enc.encode(backImage);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(os != null){
                os.close();
            }
            if(add != null){
                add.close();
            }
            if(back != null){
                back.close();
            }
        }
        return os;
    }

    /**
     *
     * @param matrix
     * @param format
     * @param stream
     * @throws IOException
     */
    private static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    /**
     *
     * @param matrix
     * @param format
     * @param file
     * @throws IOException
     */
    private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    /**
     *
     * @param matrix
     * @return
     */
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
}
