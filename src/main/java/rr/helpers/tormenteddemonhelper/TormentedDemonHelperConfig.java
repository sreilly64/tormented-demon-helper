package rr.helpers.tormenteddemonhelper;

import net.runelite.client.config.*;
import java.awt.*;

@ConfigGroup("tormenteddemonhelper")
public interface TormentedDemonHelperConfig extends Config
{

	@ConfigSection(
			name = "Accuracy Boost Timer",
			description = "Change the accuracy boost timer settings",
			position = 1
	)
	String AccuracyBoostTimerSettings = "Accuracy Boost Timer Settings";

	@ConfigItem(
			position = 1,
			keyName = "showTimer",
			name = "Show Accuracy Boost Timer",
			description = "Toggles whether to overlay the number of ticks since start of combat or last fire bomb attack",
			section = AccuracyBoostTimerSettings
	)
	default boolean showTimer()
	{
		return true;
	}

	@ConfigItem(
			position = 2,
			keyName = "unboostedTickColor",
			name = "Unboosted Tick Color",
			description = "Configures the color of unboosted timer ticks",
			section = AccuracyBoostTimerSettings
	)
	default Color unboostedTickColor()
	{
		return Color.RED;
	}

	@ConfigItem(
			position = 3,
			keyName = "boostedTickColor",
			name = "Boosted Tick Color",
			description = "Configures the color of boosted timer ticks",
			section = AccuracyBoostTimerSettings
	)
	default Color boostedTickColor()
	{
		return Color.GREEN;
	}

	@ConfigItem(
			position = 4,
			keyName = "fontType",
			name = "Timer Font Type",
			description = "Changes the font of the boost timer overlay",
			section = AccuracyBoostTimerSettings
	)
	default FontTypes fontType()
	{
		return FontTypes.REGULAR;
	}

	@Range(
			min = 8,
			max = 50
	)
	@ConfigItem(
			position = 5,
			keyName = "fontSize",
			name = "Timer Font Size",
			description = "Changes the font size of the boost timer overlay. Min = 8. Max = 50.",
			section = AccuracyBoostTimerSettings
	)
	default int fontSize()
	{
		return 18;
	}

	@Range(
			min = -70,
			max = 10
	)
	@ConfigItem(
			position = 6,
			keyName = "verticalOffset",
			name = "Timer Vertical Offset",
			description = "Changes the vertical position of the timer overlay. Min = -70. Max = 10",
			section = AccuracyBoostTimerSettings
	)
	default int verticalOffset()
	{
		return -30;
	}

}
