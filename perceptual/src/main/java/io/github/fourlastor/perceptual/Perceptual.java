package io.github.fourlastor.perceptual;

/**
 * These are utilities to help make our volume controls feel more natural.
 * Our hearing follows a logarithmic scale. We perceive less difference
 * between loud sounds than we do between soft sounds. We want our volume
 * controls to mirror how we perceive sound.
 * <p>
 * In order to make our controls more natural, we'll use an exponential scale.
 * We'll present the user a scale between 0 and 100%, but the percentage is
 * actually selecting a percentage of VOLUME_DYNAMIC_RANGE_DB, which is
 * denominated in decibels. For example, if this range is 60dB, then 50%
 * corresponds to 50% * 60 = 30dB. However, this range is actually -60 to 0,
 * so we subtract 60dB, so 30 - 60 = -30. From here, we just convert decibels
 * to amplitude using an established formula, amplitude = 10 ^ (db /20),
 * so for example 10 ^ (-30/20) = 10 ^ (-1.5) = 0.03. So 50% perceived loudness
 * yields about 3% of the total amplitude.
 * <p>
 * There are two more things to note. First, we tend to express everything as
 * percentages everywhere, so these functions take a percentage in and express
 * a percentage out. So in the previous example, perceptualToAmplitude(50%) = 3%.
 * Second, we allow users to boost the volume of other users, and we use a different
 * scale for perceived &gt; 100%. We scale these to a different "boost" range.
 */
public final class Perceptual {

    /** Default standard volume range, in dB. */
    public static float DEFAULT_VOLUME_DYNAMIC_RANGE_DB = 50;

    /** Default boost volume range, in dB. */
    public static float DEFAULT_VOLUME_BOOST_DYNAMIC_RANGE_DB = 6;

    private Perceptual() {}

    /**
     * Takes a user-presented control value and converts to amplitude
     *
     * @param perceptual Number between 0 and 2
     * @return Number between 0 and 2
     */
    public static float perceptualToAmplitude(float perceptual) {
        return perceptualToAmplitude(
                perceptual, DEFAULT_VOLUME_DYNAMIC_RANGE_DB, DEFAULT_VOLUME_BOOST_DYNAMIC_RANGE_DB);
    }

    /**
     * Takes a user-presented control value and converts to amplitude
     *
     * @param perceptual Number between 0 and 2
     * @param range      Dynamic range of perceptual value from 0 to 1
     * @return Number between 0 and 2
     */
    public static float perceptualToAmplitude(float perceptual, float range) {
        return perceptualToAmplitude(perceptual, range, DEFAULT_VOLUME_BOOST_DYNAMIC_RANGE_DB);
    }

    /**
     * Takes a user-presented control value and converts to amplitude
     *
     * @param perceptual Number between 0 and 2
     * @param range      Dynamic range of perceptual value from 0 to 1
     * @param boostRange Dynamic range of perceptual value from 1 to 2
     * @return Number between 0 and 2
     */
    public static float perceptualToAmplitude(float perceptual, float range, float boostRange) {
        if (perceptual == 0) {
            return 0;
        }
        float db;
        if (perceptual > 1f) {
            db = (perceptual - 1f) * boostRange;
        } else {
            db = (perceptual) * range - range;
        }
        return (float) Math.pow(10, db / 20);
    }

    /**
     * Takes a volume amplitude and converts to user-presented control
     *
     * @param amplitude  Number between 0 and 2
     * @return Number between 0 and 2
     */
    public static float amplitudeToPerceptual(float amplitude) {
        return amplitudeToPerceptual(amplitude, DEFAULT_VOLUME_DYNAMIC_RANGE_DB, DEFAULT_VOLUME_BOOST_DYNAMIC_RANGE_DB);
    }

    /**
     * Takes a volume amplitude and converts to user-presented control
     *
     * @param amplitude Number between 0 and 2
     * @param range     Dynamic range of amplitude value from 0 to 1
     * @return Number between 0 and 2
     */
    public static float amplitudeToPerceptual(float amplitude, float range) {
        return amplitudeToPerceptual(amplitude, range, DEFAULT_VOLUME_BOOST_DYNAMIC_RANGE_DB);
    }

    /**
     * Takes a volume amplitude and converts to user-presented control
     *
     * @param amplitude        Number between 0 and 2
     * @param range      Dynamic range of amplitude value from 0 to 1
     * @param boostRange Dynamic range of amplitude value from 1 to 2
     * @return Number between 0 and 2
     */
    public static float amplitudeToPerceptual(float amplitude, float range, float boostRange) {
        if (amplitude == 0) {
            return 0;
        }
        float db = (float) (20 * Math.log10(amplitude));
        float perceptual;
        if (db > 0) {
            perceptual = db / boostRange + 1;
        } else {
            perceptual = (range + db) / range;
        }
        return perceptual;
    }
}
