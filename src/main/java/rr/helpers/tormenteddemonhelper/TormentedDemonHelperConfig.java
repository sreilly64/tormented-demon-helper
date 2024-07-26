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
			description = "Toggles whether to display the accuracy boost timer overlay when fighting Tormented Demons",
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
			min = 10,
			max = 50
	)
	@ConfigItem(
			position = 5,
			keyName = "fontSize",
			name = "Timer Font Size",
			description = "Changes the font size of the boost timer overlay. Min = 10. Max = 50.",
			section = AccuracyBoostTimerSettings
	)
	default int fontSize()
	{
		return 20;
	}

	@Range(
			min = 0,
			max = 80
	)
	@ConfigItem(
			position = 6,
			keyName = "timerHeight",
			name = "Timer Height",
			description = "Changes the vertical position of the timer overlay. Min = 0. Max = 80",
			section = AccuracyBoostTimerSettings
	)
	default int timerHeight()
	{
		return 40;
	}

}
