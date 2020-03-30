package com.tmdbapp.utils.transformations.blur;

import android.graphics.Bitmap;

import java.util.LinkedList;

public class BlurStack implements BlurEngine {

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
        int denominator = radius * (radius + 2) + 1;

        for(int row = 0; row < h; ++row) {
            LinkedList<Integer> rQueueOut = new LinkedList<>();
            LinkedList<Integer> gQueueOut = new LinkedList<>();
            LinkedList<Integer> bQueueOut = new LinkedList<>();

            LinkedList<Integer> rQueueIn = new LinkedList<>();
            LinkedList<Integer> gQueueIn = new LinkedList<>();
            LinkedList<Integer> bQueueIn = new LinkedList<>();

            int rSumOut = 0;
            int rSumIn = 0;
            int rSum = 0;

            int gSumOut = 0;
            int gSumIn = 0;
            int gSum = 0;

            int bSumOut = 0;
            int bSumIn = 0;
            int bSum = 0;

            int startIndex = row * w;
            int originalPixel = currentPixels[startIndex];
            int rOrig = originalPixel >>> 16 & 0xFF;
            int gOrig = originalPixel >>> 8 & 0xFF;
            int bOrig = originalPixel & 0xFF;

            for(int i = 1; i <= radius + 1; ++i) {
                rQueueOut.add(rOrig);
                gQueueOut.add(gOrig);
                bQueueOut.add(bOrig);

                rSumOut += rOrig;
                gSumOut += gOrig;
                bSumOut += bOrig;

                rSum += (i + 1) * rOrig;
                gSum += (i + 1) * gOrig;
                bSum += (i + 1) * bOrig;
            }

            for (int col = 1; col <= radius; ++col) {
                int nextPixelIndex = col > w - 1 ? w - 1 + startIndex : col + startIndex;
                int nextPixel = currentPixels[nextPixelIndex];
                int rNext = nextPixel >>> 16 & 0xFF;
                int gNext = nextPixel >>> 8 & 0xFF;
                int bNext = nextPixel & 0xFF;

                rQueueIn.add(rNext);
                gQueueIn.add(gNext);
                bQueueIn.add(bNext);

                rSumIn += rNext;
                gSumIn += gNext;
                bSumIn += bNext;
            }

            for (int col = 0; col < w; ++col) {
                int newPixelIndex = row * w + col;
                firstPassPixel[newPixelIndex] = (currentPixels[newPixelIndex] & -0x1000000) |
                                                (((rSum / denominator) & 0xFF << 16)) |
                                                (((gSum / denominator) & 0xFF << 8)) |
                                                (((bSum / denominator) & 0xFF));
                rSum -= rSumOut;
                gSum -= gSumOut;
                bSum -= bSumOut;

                int nextPixelIndex = col + 1 + radius > w -1 ? (row + 1) * w - 1 : row * w + col + radius + 1;
                int nextPixel = currentPixels[nextPixelIndex];
                int rNext = nextPixel >>> 16 & 0xFF;
                int gNext = nextPixel >>> 8 & 0xFF;
                int bNext = nextPixel & 0xFF;

                rQueueIn.add(rNext);
                gQueueIn.add(gNext);
                bQueueIn.add(bNext);

                rSumIn += rNext;
                gSumIn += gNext;
                bSumIn += bNext;

                rSum += rSumIn;
                gSum += gSumIn;
                bSum += bSumIn;

                rSumOut -= rQueueOut.pop();
                gSumOut -= gQueueOut.pop();
                bSumOut -= bQueueOut.pop();

                int rTransfer = rQueueIn.pop();
                int gTransfer = gQueueIn.pop();
                int bTransfer = bQueueIn.pop();

                rSumIn -= rTransfer;
                gSumIn -= gTransfer;
                bSumIn -= bTransfer;

                rQueueOut.add(rTransfer);
                gQueueOut.add(gTransfer);
                bQueueOut.add(bTransfer);

                rSumOut += rTransfer;
                gSumOut += gTransfer;
                bSumOut += bTransfer;
            }
        }

        for (int col = 0; col < w; ++col) {
            LinkedList<Integer> rQueueOut = new LinkedList<>();
            LinkedList<Integer> gQueueOut = new LinkedList<>();
            LinkedList<Integer> bQueueOut = new LinkedList<>();

            LinkedList<Integer> rQueueIn = new LinkedList<>();
            LinkedList<Integer> gQueueIn = new LinkedList<>();
            LinkedList<Integer> bQueueIn = new LinkedList<>();

            int rSumOut = 0;
            int gSumOut = 0;
            int bSumOut = 0;

            int rSumIn = 0;
            int gSumIn = 0;
            int bSumIn = 0;

            int rSum = 0;
            int gSum = 0;
            int bSum = 0;

            int originalPixel = firstPassPixel[col];
            int rOrig = originalPixel >>> 16 & 0xFF;
            int gOrig = originalPixel >>> 8 & 0xFF;
            int bOrig = originalPixel & 0xFF;

            for (int i = 1; i <= radius + 1; ++i) {
                rQueueOut.add(rOrig);
                gQueueOut.add(gOrig);
                bQueueOut.add(bOrig);

                rSumOut += rOrig;
                gSumOut += gOrig;
                bSumOut += bOrig;

                rSum += (i + 1) * rOrig;
                gSum += (i + 1) * gOrig;
                bSum += (i + 1) * bOrig;
            }

            for (int row = 1; row <= radius; ++row) {
                int nextPixelIndex = row > h - 1 ? (h - 1) * w + col : row * w + col;
                int nextPixel = firstPassPixel[nextPixelIndex];
                int rNext = nextPixel >>> 16 & 0xFF;
                int gNext = nextPixel >>> 8 & 0xFF;
                int bNext = nextPixel & 0xFF;

                rQueueIn.add(rNext);
                gQueueIn.add(gNext);
                bQueueIn.add(bNext);

                rSumIn += rNext;
                gSumIn += gNext;
                bSumIn += bNext;

                int multiplier = radius + 1 - row;
                rSum += multiplier * rNext;
                gSum += multiplier * gNext;
                bSum += multiplier * bNext;
            }

            for (int row = 0; row < h; ++row) {
                int newPixelIndex = row * w + col;
                newPixels[newPixelIndex] = (firstPassPixel[newPixelIndex] & -0x1000000) |
                                           ((rSum / denominator) & 0xFF << 16) |
                                           ((gSum / denominator) & 0xFF << 8) |
                                           ((bSum / denominator) & 0xFF);

                rSum -= rSumOut;
                gSum -= gSumOut;
                bSum -= bSumOut;

                int nextPixelIndex = row + 1 + radius > h - 1 ? (row + 1) * w + col : (row + radius + 1) * w + col;
                if(nextPixelIndex >= w * h) break;

                int nextPixel = firstPassPixel[nextPixelIndex];
                int rNext = nextPixel >>> 16 & 0xFF;
                int gNext = nextPixel >>> 8 & 0xFF;
                int bNext = nextPixel & 0xFF;

                rQueueIn.add(rNext);
                gQueueIn.add(gNext);
                bQueueIn.add(bNext);

                rSumIn += rNext;
                gSumIn += gNext;
                bSumIn += bNext;

                rSum += rSumIn;
                gSum += gSumIn;
                bSum += bSumIn;

                rSumOut -= rQueueOut.pop();
                gSumOut -= gQueueOut.pop();
                bSumOut -= bQueueOut.pop();

                int rTransfer = rQueueIn.pop();
                int gTransfer = gQueueIn.pop();
                int bTransfer = bQueueIn.pop();

                rSumIn -= rTransfer;
                gSumIn -= gTransfer;
                bSumIn -= bTransfer;

                rQueueOut.add(rTransfer);
                gQueueOut.add(gTransfer);
                bQueueOut.add(bTransfer);

                rSumOut += rTransfer;
                gSumOut += gTransfer;
                bSumOut += bTransfer;
            }
        }
    }

    @Override
    public String getType() {
        return "Stack Blur";
    }
}
