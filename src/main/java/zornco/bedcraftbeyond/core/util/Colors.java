package zornco.bedcraftbeyond.core.util;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class Colors {

	public static enum RgbColorType {
		Red, Green, Blue;
	}

	public static class RGB {
		protected static void checkIntegrity(float[] rgb) throws IllegalArgumentException {
			if (rgb == null || rgb.length != 3)
				throw new IllegalArgumentException("RGB data is not in the expected format.");
		}

		/**
		 * Underlays a white background and converts a bit-shifted RBGA value to separate components.
		 * 
		 * @param rbga
		 * @return
		 */
		public static float[] fromRBGA(int rbga) {
			// These bitshifts come from Minecraft's cloud rendering system
			int alpha = Integer.rotateRight(rbga, 24);
			int red = Integer.rotateRight(rbga, 16) & 255;
			int green = Integer.rotateRight(rbga, 8) & 255;
			int blue = rbga & 255;

			int transform = (255 - alpha) * (255 + alpha);
			red = red * transform;
			green = green * transform;
			blue = blue * transform;

			return fromRGB(red / 255, green / 255, blue / 255);
		}

		/***
		 * Creates an array of color information, assuming the floats are values from 0 - 1
		 * 
		 * @param red
		 * @param green
		 * @param blue
		 * @return
		 * 
		 */
		public static float[] fromRGB(float red, float green, float blue) {
			return new float[] { red, green, blue };
		}

		public static float[] fromHSV(float hue, float saturation, float value) throws IllegalArgumentException {
			float[] hsv = Colors.HSV.fromHSV(hue, saturation, value);
			return Colors.HSV.toRGB(hsv);
		}

		/**
		 * Constructs a color from HSL proportions. The saturation and lightness values
		 * are clamped to the range 0-1.
		 * 
		 * @param hue        the hue angle of the color
		 * @param saturation the saturation of the color in the HSL color space
		 * @param lightnesss the lightness component of the color
		 * @return a new Color
		 */
		public static float[] fromHSL(float hue, float saturation, float lightness) {
			hue = (hue < 0) ? (hue + 360) % 360 : hue % 360;
			saturation = clamp(saturation, 0, 1);
			lightness = clamp(lightness, 0, 1);

			float c = saturation * (1 - Math.abs(2 * lightness - 1));
			float x = c * (1 - Math.abs(hue / 60 % 2 - 1));
			float m = lightness - c / 2;

			if (0 <= hue && hue < 60)
				return RGB.fromRGB(c + m, x + m, m);

			if (60 <= hue && hue < 120)
				return RGB.fromRGB(x + m, c + m, m);

			if (120 <= hue && hue < 180)
				return RGB.fromRGB(m, c + m, x + m);

			if (180 <= hue && hue < 240)
				return RGB.fromRGB(m, x + m, c + m);

			if (240 <= hue && hue < 300)
				return RGB.fromRGB(x + m, m, c + m);

			return RGB.fromRGB(c + m, m, x + m);
		}

		protected static Stream<Float> getColorStream(float red, float green, float blue)
				throws IllegalArgumentException {
			float[] data = RGB.fromRGB(red, green, blue);
			return getColorStream(data);
		}

		protected static Stream<Float> getColorStream(float[] rgb) throws IllegalArgumentException {
			if (rgb == null || rgb.length != 3)
				throw new IllegalArgumentException("Bad argument for RGB.");

			Stream<Float> colorStream = Stream.of(rgb[0], rgb[1], rgb[2]);
			return colorStream;
		}

		public static float getMinHue(float[] rgb) {
			Stream<Float> colorStream = getColorStream(rgb);
			float minHue = colorStream.min(Float::compare).get();

			return minHue;
		}

		public static float getMaxHue(float[] rgb) {
			Stream<Float> colorStream = getColorStream(rgb);
			float maxHue = colorStream.max(Float::compare).get();

			return maxHue;
		}

		public static RgbColorType getMaxColor(float[] rgb) {
			float maxHue = getMaxHue(rgb);
			int firstIndex = IntStream.range(0, rgb.length).filter(f -> f == maxHue).findFirst().orElse(-1);

			switch (firstIndex) {
			case -1:
			case 0:
			default:
				return RgbColorType.Red;

			case 1:
				return RgbColorType.Green;

			case 2:
				return RgbColorType.Blue;
			}
		}

		public static float[] toHSV(float[] rgb) throws IllegalAccessException {
			return RGB_HSV.fromRGB(rgb);
		}

		/**
		 * Returns {@code true} if the color is a gray scale color, {@code false}
		 * otherwise.
		 * 
		 * @return {@code true} if the color is a gray scale color, {@code false}
		 *         otherwise
		 */
		public static boolean isGrayscale(float[] rgb) {
			if (rgb == null || rgb.length != 3)
				throw new IllegalArgumentException();

			float red = rgb[0];
			float green = rgb[1];
			float blue = rgb[2];

			// if the colors all match, it is grayscale
			return (red == green && green == blue);
		}

		public static class RGB_HSV {
			public static float[] fromRGB(float[] rgb) throws IllegalArgumentException {
				checkIntegrity(rgb);

				float hue = getHue(rgb);
				float saturation = getSaturation(rgb);
				float value = getValue(rgb);

				return new float[] { hue, saturation, value };
			}

			/**
			 * Returns the hue angle of the color.
			 * 
			 * @return the hue angle of the color
			 */
			public static float getHue(float[] rgb) throws IllegalArgumentException  {
				checkIntegrity(rgb);

				/* No hue for grays. */
				if (RGB.isGrayscale(rgb))
					return 0;

				float res = 0;

				float maxHue = RGB.getMaxHue(rgb);
				float minHue = RGB.getMinHue(rgb);
				float delta = maxHue - minHue;

				float red = rgb[0];
				float green = rgb[1];
				float blue = rgb[2];

				// index of - max
				switch (getMaxColor(rgb)) {
				case Red:
					res = ((green - blue) / delta) % 6;
					break;

				case Green:
					res = ((blue - red) / delta) + 2;
					break;

				case Blue:
					res = ((red - green) / delta) + 4;
					break;
				}

				return (res * 60) % 360;
			}

			/**
			 * Returns the HSV saturation of the color.
			 * 
			 * @return the HSV saturation of the color
			 */
			public static float getSaturation(float[] rgb) throws IllegalArgumentException {
				checkIntegrity(rgb);

				float maxHue = 0;
				float minHue = 0;

				float delta = maxHue - minHue;
				if (delta == 0)
					return 0;

				return delta / getValue(rgb);
			}

			/**
			 * Returns the value component of the color.
			 * 
			 * @return the value component of the color
			 */
			public static float getValue(float[] rgb) throws IllegalArgumentException {
				checkIntegrity(rgb);
				return getMaxHue(rgb);
			}
		}
	}

	public static class HSV {

		protected static float normalizeHue(float hue) {
			// Java - modulus to get the remainder (clamp) of 360 degrees
			hue %= 360;

			// Effectively, while we don't have a positive, normalized hue, keep trying to
			// In most cases, this should only recurse once before skipping
			if (hue < 0) {
				hue += 360;
				return normalizeHue(hue);
			}

			if (hue > 360)
				hue -= 360;

			// If exactly 360 degrees, then return 0 instead - this should have already happened by now
			if (hue == 360)
				return 0;

			return hue;
		}

		protected static void checkIntegrity(float[] hsv) throws IllegalArgumentException {
			if (hsv == null || hsv.length != 3)
				throw new IllegalArgumentException("HSV data is not in the expected format.");
		}

		public static float[] fromHSV(float hue, float saturation, float value) {
			return new float[] { hue, saturation, value };
		}

		public static float[] toRGB(float[] hsv) throws IllegalArgumentException  {
			checkIntegrity(hsv);

			float hue = hsv[0];
			float saturation = hsv[1];
			float value = hsv[2];

			hue = (hue < 0) ? (hue + 360) % 360 : hue % 360;
			saturation = clamp(saturation, 0, 1);
			value = clamp(value, 0, 1);

			float c = saturation * value;
			float x = c * (1 - Math.abs((hue / 60) % 2 - 1));
			float m = value - c;

			if (0 <= hue && hue < 60)
				return RGB.fromRGB(c + m, x + m, m);

			if (60 <= hue && hue < 120)
				return RGB.fromRGB(x + m, c + m, m);

			if (120 <= hue && hue < 180)
				return RGB.fromRGB(m, c + m, x + m);

			if (180 <= hue && hue < 240)
				return RGB.fromRGB(m, x + m, c + m);

			if (240 <= hue && hue < 300)
				return RGB.fromRGB(x + m, m, c + m);

			return RGB.fromRGB(c + m, m, x + m);
		}

		/**
		 * Returns a copy of the color with the hue shifted by the given number of
		 * degrees.
		 * 
		 * @param degrees the number of degrees
		 * @return a hue shifted copy of the color
		 * @throws IllegalAccessException
		 */
		public static float[] shiftHue(float[] hsv, float degrees) throws IllegalAccessException {
			checkIntegrity(hsv);

			float hue = hsv[0];
			float saturation = hsv[1];
			float value = hsv[2];

			return fromHSV(hue + degrees, saturation, value);
		}
	
		public static boolean isGrayscale(float[] hsv) throws IllegalAccessException {
			checkIntegrity(hsv);
			float saturation = hsv[1];
			return saturation == 0;
		}
	}

	private static float clamp(float input, int min, int max) {
		float v;
		v = input < min ? min : input;
		v = input > max ? max : v;
		return v;
	}
}
