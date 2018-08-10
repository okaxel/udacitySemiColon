package hu.drorszagkriszaxel.semicolon.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import hu.drorszagkriszaxel.semicolon.R;

/**
 *
 * SemiColon
 *
 * © 2018 by Axel Ország-Krisz Dr.
 *
 * @author  Axel Ország-Krisz Dr.
 * @version 1.0 - first try
 *
 * ---
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---
 *
 * For more information and source check out:
 *
 * https://github.com/okaxel/udacitySemiColon
 *
 * ---
 *
 * Simple utility class to help out Widget in graphics
 *
 */
public final class BitmapUtils {

    /**
     * Converts Widget's drawable background to an image.
     *
     * @param context A good context is always welcome
     * @return        Bitmap
     */
    public static Bitmap getBackground(Context context) {

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.widget_background);

        Bitmap bitmap = null;
        Canvas canvas = null;

        if (drawable != null) {

            drawable = (DrawableCompat.wrap(drawable)).mutate();

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);

            if (bitmap != null) {

                canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

            }

        }

        return bitmap;

    }

}
