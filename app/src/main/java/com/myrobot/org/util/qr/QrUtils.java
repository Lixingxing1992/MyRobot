package com.myrobot.org.util.qr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import cn.bertsir.zbar.Qr.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * @author Lixingxing
 */
public class QrUtils {
    public static final String TAG = "QrUtils";

    /**
     * 以字节的格式压缩位图
     *
     * @param bytes 拍照所得字节位图
     * @return 压缩后的Bitmap
     */
    public static Bitmap byteForCompressBitmap(byte[] bytes) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = computeSampleSize(options, -1, 1920 * 1080);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }


    /**
     * 计算图片样本大小 for google
     *
     * @param options        BitmapOption 记得inJustBounds设置为true
     * @param minSideLength  最小边长 一般为 -1
     * @param maxNumOfPixels 最大像素数
     * @return 返回可缩放倍数
     */
    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 计算图片初始样本大小
     *
     * @param options        开关
     * @param minSideLength  最小边长
     * @param maxNumOfPixels 最大像素数目
     * @return 传入图片初始样本大小
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }



    public static String decodeQRcode(Bitmap barcodeBmp) throws Exception {
        int width = barcodeBmp.getWidth();
        int height = barcodeBmp.getHeight();
        int[] pixels = new int[width * height];
        barcodeBmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Image barcode = new Image(width, height, "RGB4");
        barcode.setData(pixels);
        ImageScanner reader = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        int result = reader.scanImage(barcode.convert("Y800"));
        String qrCodeString = null;
        if (result != 0) {
            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                qrCodeString = sym.getData();
            }
        }
        return qrCodeString;
    }

    public static String decodeBarcode(Bitmap barcodeBmp)
    {
        int width = barcodeBmp.getWidth();
        int height = barcodeBmp.getHeight();
        int[] pixels = new int[width * height];
        barcodeBmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Image barcode = new Image(width, height, "RGB4");
        barcode.setData(pixels);
        ImageScanner reader = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.CODE128, Config.ENABLE, 1);
        reader.setConfig(Symbol.CODE39, Config.ENABLE, 1);
        reader.setConfig(Symbol.EAN13, Config.ENABLE, 1);
        reader.setConfig(Symbol.EAN8, Config.ENABLE, 1);
        reader.setConfig(Symbol.UPCA, Config.ENABLE, 1);
        reader.setConfig(Symbol.UPCE, Config.ENABLE, 1);
        int result = reader.scanImage(barcode.convert("Y800"));
        String qrCodeString = null;
        if (result != 0) {
            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                qrCodeString = sym.getData();
            }
        }
        return qrCodeString;
    }


    public static String decodeCode(int width,int height,byte[] data)
    {
        Image barcode = new Image(width, height, "RGB4");
        barcode.setData(data);
        ImageScanner reader = new ImageScanner();
        reader.setConfig(Symbol.NONE, Config.ENABLE, 0);
        reader.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        reader.setConfig(Symbol.CODE128, Config.ENABLE, 1);
        reader.setConfig(Symbol.CODE39, Config.ENABLE, 1);
        reader.setConfig(Symbol.EAN13, Config.ENABLE, 1);
        reader.setConfig(Symbol.EAN8, Config.ENABLE, 1);
        reader.setConfig(Symbol.UPCA, Config.ENABLE, 1);
        reader.setConfig(Symbol.UPCE, Config.ENABLE, 1);
        int result = reader.scanImage(barcode.convert("Y800"));
        String qrCodeString = null;
        if (result != 0) {
            SymbolSet syms = reader.getResults();
            for (Symbol sym : syms) {
                qrCodeString = sym.getData();
            }
        }
        return qrCodeString;
    }


    /**
     * 生成二维码
     *
     * @param content
     * @return
     */
    public static Bitmap createQRCode(String content) {
        return createQRCode(content, 300, 300);
    }


    /**
     * 生成二维码
     *
     * @param content
     * @return
     */
    public static Bitmap createQRCode(String content, int width, int height) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//这里调整二维码的容错率
            hints.put(EncodeHintType.MARGIN, 1);   //设置白边取值1-4，值越大白边越大
            result = multiFormatWriter.encode(new String(content.getBytes("UTF-8"), "ISO-8859-1"), BarcodeFormat
                    .QR_CODE, width, height, hints);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
