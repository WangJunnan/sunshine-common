package com.sunshine.common.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ImageUtils
 *
 * @author wangjn
 * @date 2019/4/19
 */
public class ImageUtils {

    /**
     * 打水印
     *
     * @param waterImg 水印图
     * @param g
     * @param x x轴坐标
     * @param y y轴坐标
     * @throws IOException
     */
    public static void addWaterPic(BufferedImage waterImg, Graphics2D g, int x, int y) throws IOException {
        int height = waterImg.getHeight();
        int width = waterImg.getWidth();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(waterImg, x, y, width, height, null);
        g.dispose();
    }

    /**
     * 图片指定位置打马赛克
     *
     * @param d
     * @param image
     * @param x 起点x坐标
     * @param y 起点y坐标
     * @param width 马赛克区域宽度
     * @param height 马赛克区域长度
     */
    public static void addMosaic(Graphics2D d, BufferedImage image, int x, int y, int width, int height) {
        // 马赛克矩形大小
        int size = 10;
        int xCount = 0;
        int yCount = 0;
        if (width % size == 0) {
            xCount = width/size;
        } else {
            xCount = width / size + 1;
        }

        if (height % size == 0) {
            yCount = height/size;
        } else {
            yCount = height / size + 1;
        }

        int startX = x;
        int startY = y;
        for (int i = 0; i < yCount; i++) {
            for (int j = 0; j < xCount; j++) {
                int mwidth = size;
                int mheight = size;

                int centerX = startX + mwidth/2;
                int centerY = startY + mheight/2;


                Color color = new Color(image.getRGB(centerX, centerY));
                //Color color = new Color(255, 180, 0);
                d.setColor(color);
                d.fillRect(startX, startY, mwidth, mheight);
                // 计算下一个矩形的x坐标
                startX = startX + size;
            }
            startX = x;
            // 计算y坐标
            startY = startY + size;
        }
        d.dispose();
    }
}
