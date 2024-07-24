package rr.helpers.tormenteddemonhelper;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
		name = "Tormented Demons Helper",
		description = "Provides QoL improvements for fighting Tormented Demons",
		tags = {"combat", "pve", "pvm", "slayer"}
)
public class TormentedDemonHelperPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private TormentedDemonHelperConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}


	@Provides
	TormentedDemonHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TormentedDemonHelperConfig.class);
	}
}
