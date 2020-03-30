package com.tmdbapp.utils.transformations.blur;

import android.graphics.Bitmap;

public class BlurBox implements BlurEngine {

    @Override
    public Bitmap blur(Bitmap image, int radius) {
        int w = image.getWidth();
        int h = image.getHeight();
        int[] currentPixels = new int[w * h];
        int[] newPixels = new int[w * h];
        image.getPixels(currentPixels, 0, w, 0, 0, w, h);
        blurProcess(w, h, currentPixels, newPixels, radius);
        return Bitmap.createBitmap(newPixels, w, h, Bitmap.Config.ARGB_8888);
    }

    private void blurProcess(int w, int h, int[] currentPixels, int[] newPixels, int radius) {
        for (int col = 0; col < w; ++col)
            for (int row = 0; row < h; ++row)
                newPixels[row * w + col] = getSurroundAverage(currentPixels, col, row, h, w, radius);
    }

    private int getSurroundAverage(int[] currentPixels, int col, int row, int h, int w, int radius) {
        int originalPixel = currentPixels[row * w + col];
        int a = originalPixel >> 24 & 0xFF;
        int rOrig = originalPixel >>> 16 & 0xFF;
        int gOrig = originalPixel >>> 8 & 0xFF;
        int bOrig = originalPixel & 0xFF;

        int rSum = rOrig;
        int gSum = gOrig;
        int bSum = bOrig;

        for(int y  = row - radius; y <= row + radius; ++y)
            for (int x = col - radius; x <= col + radius; ++x) {
                if (y < 0 || y > h - 1 || x < 0 || x > w - 1) {
                    rSum += rOrig;
                    gSum += gOrig;
                    bSum += bOrig;
                } else if (y == row && x == col) {
                    //Nothing
                } else {
                    int sidePixel = currentPixels[y * w + x];
                    rSum += sidePixel >>> 16 & 0xFF;
                    gSum += sidePixel >>> 8 & 0xFF;
                    bSum += sidePixel & 0xFF;
                }
            }

        int denominator = (radius * 2 + 1) * (radius * 2 + 1);
        return ((a & 0xFF) << 24) |
               ((rSum / denominator) & 0xFF << 16) |
               ((gSum / denominator) & 0xFF << 8) |
               ((bSum / denominator) & 0xFF);
    }

    @Override
    public String getType() {
        return "Basic Blur";
    }
}
