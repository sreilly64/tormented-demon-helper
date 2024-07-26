package rr.helpers.tormenteddemonhelper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TormentedDemonHelperPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TormentedDemonHelperPlugin.class);
		RuneLite.main(args);
	}
}