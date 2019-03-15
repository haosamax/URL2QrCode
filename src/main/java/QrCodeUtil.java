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
     * @return
     */
    public static OutputStream creatQrCode(String url) {
        OutputStream out =new ByteArrayOutputStream();
        Map<EncodeHintType, String> hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            writeToStream(bitMatrix, "jpg", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * 合并图片(按指定初始x、y坐标将附加图片贴到底图之上)
     * @param backPath 背景图片路径
     * @param additionPath 附加图片路径
     * @param x 附加图片的起始点x坐标
     * @param y  附加图片的起始点y坐标
     * @return
     * @throws IOException
     */
    public static OutputStream mergeBothImage(String backPath,String additionPath,int x,int y) throws IOException{
        InputStream is= null;
        InputStream is2= null;
        OutputStream os = null;
        try{
            is=new FileInputStream(backPath);
            is2=new FileInputStream(additionPath);
            BufferedImage image=ImageIO.read(is);
            BufferedImage image2=ImageIO.read(is2);
            Graphics g=image.getGraphics();
            g.drawImage(image2,x,y,null);
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
