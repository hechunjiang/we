package wedemo.utils;

import android.graphics.Color;

import com.meicam.sdk.NvsColor;

/**
 * Created by admin on 2018/7/9.
 */

public class ColorUtil {
    public static String[] code = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static NvsColor colorStringtoNvsColor(String colorString) {
        if (colorString == null || (colorString != null && colorString.isEmpty()))
            return null;
        NvsColor color = new NvsColor(1, 1, 1, 1);
        int hexColocr = Color.parseColor(colorString);
        color.a = (float) ((hexColocr & 0xff000000) >>> 24) / 0xFF;
        color.r = (float) ((hexColocr & 0x00ff0000) >> 16) / 0xFF;
        color.g = (float) ((hexColocr & 0x0000ff00) >> 8) / 0xFF;
        color.b = (float) ((hexColocr) & 0x000000ff) / 0xFF;
        return color;
    }

    public static int[] nvsColortoRgba(NvsColor color) {
        int rgba[] = {255, 255, 255, 255};
        if (color == null)
            return rgba;
        int red = (int) Math.floor(color.r * 255 + 0.5D);
        int green = (int) Math.floor(color.g * 255 + 0.5D);
        int blue = (int) Math.floor(color.b * 255 + 0.5D);
        int alpha = (int) Math.floor(color.a * 255 + 0.5D);
        rgba[0] = alpha;
        rgba[1] = red;
        rgba[2] = green;
        rgba[3] = blue;
        for (int i = 0; i < rgba.length; i++) {
            if (rgba[i] < 0) {
                rgba[i] = 0;
            } else if (rgba[i] > 255) {
                rgba[i] = 255;
            }
        }
        return rgba;
    }

    public static String nvsColorToHexString(NvsColor color) {
        int rgba[] = nvsColortoRgba(color);
        String hexCode = "#";
        for (int i = 0; i < rgba.length; i++) {
            int rgbItem = rgba[i];
            int lCode = rgbItem / 16;
            int rCode = rgbItem % 16;
            hexCode += code[lCode];
            hexCode += code[rCode];
        }
        return hexCode;
    }
}
