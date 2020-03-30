package com.tmdbapp.utils.transformations.blur;

import android.graphics.Bitmap;

import java.util.LinkedList;

public class BlurBoxOptimized implements BlurEngine {

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
        int[] firstPassPixel = new int[w * h];
        int denominator = radius * 2 + 1;

        for (int row = 0; row < h; ++row) {
            LinkedList<Integer> rQueue = new LinkedList<>();
            LinkedList<Integer> gQueue = new LinkedList<>();
            LinkedList<Integer> bQueue = new LinkedList<>();

            int startIndex = row * w;
            int originalPixel = currentPixels[startIndex];
            int rOrig = originalPixel >>> 16 & 0xFF;
            int gOrig = originalPixel >>> 8 & 0xFF;
            int bOrig = originalPixel & 0xFF;

            for (int i = 1; i <= radius + 1; ++i) {
                rQueue.add(rOrig);
                gQueue.add(gOrig);
                bQueue.add(bOrig);
            }

            int rSum = rOrig * (radius + 1);
            int gSum = gOrig * (radius + 1);
            int bSum = bOrig * (radius + 1);

            for (int col = 1; col <= radius; ++col) {
                int nextPixelIndex = (col > w - 1) ? startIndex + w - 1 : startIndex + col;
                int nextPixel = currentPixels[nextPixelIndex];
                int rNext = nextPixel >>> 16 & 0xFF;
                int gNext = nextPixel >>> 8 & 0xFF;
                int bNext = nextPixel & 0xFF;

                rQueue.add(rNext);
                gQueue.add(gNext);
                bQueue.add(bNext);

                rSum += rNext;
                gSum += gNext;
                bSum += bNext;
            }

            for (int col = 0; col < w; ++col) {
                int newPixelIndex = row * w + col;
                firstPassPixel[newPixelIndex] = (currentPixels[newPixelIndex] & -0x1000000) |
                                                ((rSum / denominator) & 0xFF << 16) |
                                                ((gSum / denominator) & 0xFF << 8) |
                                                ((bSum / denominator) & 0xFF);

                rSum -= rQueue.remove();
                gSum -= gQueue.remove();
                bSum -= bQueue.remove();

                int nextPixelIndex = (col + 1 + radius > w - 1) ? (row + 1) * w - 1 : row * w + col + radius + 1;
                int nextPixel = currentPixels[nextPixelIndex];
                int rNext = nextPixel >>> 16 & 0xFF;
                int gNext = nextPixel >>> 8 & 0xFF;
                int bNext = nextPixel & 0xFF;

                rQueue.add(rNext);
                gQueue.add(gNext);
                bQueue.add(bNext);

                rSum += rNext;
                gSum += gNext;
                bSum += bNext;
            }
        }

        for (int col = 0; col < w; ++col) {
            LinkedList<Integer> rQueue = new LinkedList<>();
            LinkedList<Integer> gQueue = new LinkedList<>();
            LinkedList<Integer> bQueue = new LinkedList<>();

            int originalPixel = firstPassPixel[col];
            int rOrig = originalPixel >>> 16 & 0xFF;
            int gOrig = originalPixel >>> 8 & 0xFF;
            int bOrig = originalPixel & 0xFF;

            for (int i = 1; i <= radius + 1; ++i) {
                rQueue.add(rOrig);
                gQueue.add(gOrig);
                bQueue.add(bOrig);
            }

            int rSum = rOrig * (radius + 1);
            int gSum = gOrig * (radius + 1);
            int bSum = bOrig * (radius + 1);

            for (int row = 1; row <= radius; ++row) {
                int nextPixelIndex = (row > h - 1) ? col + w * (h - 1) : col + row * w;
                int nextPixel = firstPassPixel[nextPixelIndex];
                int rNext = nextPixel >>> 16 & 0xFF;
                int gNext = nextPixel >>> 8 & 0xFF;
                int bNext = nextPixel & 0xFF;

                rQueue.add(rNext);
                gQueue.add(gNext);
                bQueue.add(bNext);

                rSum += rNext;
                gSum += gNext;
                bSum += bNext;
            }

            for (int row = 0; row < h; ++row) {
                int newPixelIndex = row * w + col;
                newPixels[newPixelIndex] = (firstPassPixel[newPixelIndex] & -0x1000000) |
                                           ((rSum / denominator) & 0xFF << 16) |
                                           ((gSum / denominator) & 0xFF << 8) |
                                           ((bSum / denominator) & 0xFF);

                rSum -= rQueue.remove();
                gSum -= gQueue.remove();
                bSum -= bQueue.remove();

                int nextPixelIndex = (row + 1 + radius > h - 1) ? col + w * (row + 1) : col + w * (row + radius + 1);
                if (nextPixelIndex >= w * h) break;

                int nextPixel = firstPassPixel[nextPixelIndex];
                int rNext = nextPixel >>> 16 & 0xFF;
                int gNext = nextPixel >>> 8 & 0xFF;
                int bNext = nextPixel & 0xFF;

                rQueue.add(rNext);
                gQueue.add(gNext);
                bQueue.add(bNext);

                rSum += rNext;
                gSum += gNext;
                bSum += bNext;
            }
        }
    }

    @Override
    public String getType() {
        return "Box Blur";
    }
}
