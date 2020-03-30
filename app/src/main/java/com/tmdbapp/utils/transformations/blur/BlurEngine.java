package com.tmdbapp.utils.transformations.blur;

import android.graphics.Bitmap;

public interface BlurEngine {
    Bitmap blur(Bitmap image, int radius);
    String getType();
}
