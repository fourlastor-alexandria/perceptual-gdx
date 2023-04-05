# Perceptual

This is a port of Discord's [perceptual](https://www.github.com/discord/perceptual/) to Java.

## Setup

### Gradle

Perceptual is hosted at Maven Central, you can get it by adding the following dependencies:

```kts
implementation("io.github.fourlastor.gdx:perceptual:$perceptualVersion")
```

### GWT

Add the following to your GWT xml file:

```xml
<inherits name="io.github.fourlastor.Perceptual" />
```

## Usage

### Convert Interface Value to Amplitude

If you have a volume slider or other control which returns a Number from 0 to 1, the following will convert the slider's position to an amplitude.

```java
float sliderValue = 0.5;
Perceptual.perceptualToAmplitude(sliderValue);
// 0.056234132519034905
```

This tells us that the corresponding amplitude that will be perceived as 50% as loud is about 5.6%. By default, this method uses a 50dB range.

We can also boost the original volume using a separate dynamic range,

```java
Perceptual.perceptualToAmplitude(1.5f, 60, 6);
// 1.4125375446227542
```

With our 6dB boost range, 150% loudness yields 141% amplitude.

### Convert Amplitude to Interface Value

`Perceptual` also supplies a method to calculate from amplitudes to perceptual interface values.

```java
Perceptual.amplitudeToPerceptual(0.5f);
// 0.8795880017344075
```

This tells us that 50% amplitude corresponds to about 88% perceived loudness on our default 50dB range.

## Theory

This is a utility to help make our volume controls feel more natural. Our hearing follows a logarithmic scale. We perceive less difference between loud sounds than we do between soft sounds. We want our volume controls to mirror how we perceive sound.

In order to make our controls more natural, we'll use an exponential scale. We'll present the user a scale between 0 and 100%, but the percentage will actually select a percentage of a reasonably-chosen range denominated in decibels. For example, if this range is 60dB, then 50% corresponds to 50% * 60 = 30dB. However, this range is negative; it starts at -60dB and increases toward 0dB so we subtract 60dB, so 30 - 60 = -30. From here, we just convert decibels to amplitude using an established formula, amplitude = 10<sup>(db/20)</sup>, so for example 10<sup>(-30/20)</sup> = 10<sup>-1.5</sup> = 0.03. So 50% perceived loudness yields about 3% of the total amplitude.

There are two more things to note. First, we'll express everything as percentages everywhere, so these functions take a percentage in and express a percentage out. In the previous example, perceptualToAmplitude(50%) = 3%. Second, we allow users to boost the volume of other users, and we use a different scale for perceived > 100%. We scale these to a different "boost" range, which by default is 6dB of boost.
