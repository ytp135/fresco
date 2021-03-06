/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.facebook.fresco.samples.showcase;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.fresco.samples.showcase.imageformat.keyframes.KeyframesDecoderExample;
import com.facebook.fresco.samples.showcase.imageformat.svg.SvgDecoderExample;
import com.facebook.imagepipeline.decoder.ImageDecoderConfig;

/**
 * Helper class to add custom decoders and drawable factories if enabled.
 */
public class CustomImageFormatConfigurator {

  private static final String IMAGE_FORMAT_PREFS = "fresco_image_format_prefs";
  private static final String IMAGE_FORMAT_SVG_KEY = "svg";
  private static final String IMAGE_FORMAT_KEYFRAMES_KEY = "keyframes";

  @Nullable
  public static ImageDecoderConfig createImageDecoderConfig(Context context) {
    ImageDecoderConfig.Builder config = ImageDecoderConfig.newBuilder();
    if (isSvgEnabled(context)) {
      config.addDecodingCapability(
          SvgDecoderExample.SVG_FORMAT,
          new SvgDecoderExample.SvgFormatChecker(),
          new SvgDecoderExample.SvgDecoder());
    }
    if (isKeyframesEnabled(context)) {
      config.addDecodingCapability(
          KeyframesDecoderExample.IMAGE_FORMAT_KEYFRAMES,
          KeyframesDecoderExample.createFormatChecker(),
          KeyframesDecoderExample.createDecoder());
    }
    return config.build();
  }

  public static void addCustomDrawableFactories(
      Context context,
      DraweeConfig.Builder draweeConfigBuilder) {
    if (isSvgEnabled(context)) {
      draweeConfigBuilder.addCustomDrawableFactory(new SvgDecoderExample.SvgDrawableFactory());
    }
    if (isKeyframesEnabled(context)) {
      draweeConfigBuilder.addCustomDrawableFactory(KeyframesDecoderExample.createDrawableFactory());
    }
  }

  public static boolean isSvgEnabled(Context context) {
    return getBoolean(context, IMAGE_FORMAT_SVG_KEY, false);
  }

  public static void setSvgEnabled(Context context, boolean svgEnabled) {
    setBoolean(context, IMAGE_FORMAT_SVG_KEY, svgEnabled);
  }

  public static boolean isKeyframesEnabled(Context context) {
    //TODO Add setting and notify user when the feature is not supported
    boolean defaultEnabled = Build.VERSION.SDK_INT >= 15;
    return getBoolean(context, IMAGE_FORMAT_KEYFRAMES_KEY, defaultEnabled);
  }

  public static void setKeyFramesEnabled(Context context, boolean keyFramesEnabled) {
    setBoolean(context, IMAGE_FORMAT_KEYFRAMES_KEY, keyFramesEnabled);
  }

  private static boolean getBoolean(Context context, String key, boolean defaultValue) {
    return context.getSharedPreferences(IMAGE_FORMAT_PREFS, Context.MODE_PRIVATE)
        .getBoolean(key, defaultValue);
  }

  private static void setBoolean(Context context, String key, boolean value) {
    context.getSharedPreferences(IMAGE_FORMAT_PREFS, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(key, value)
        .apply();
  }
}
