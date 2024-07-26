package rr.helpers.tormenteddemonhelper;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.*;

@Slf4j
@Getter
@PluginDescriptor(
		name = "Tormented Demon Helper",
		description = "Provides QoL improvements when fighting Tormented Demons",
		tags = {"combat", "pve", "pvm", "slayer", "td", "tormented demon"}
)
public class TormentedDemonHelperPlugin extends Plugin
{
	private static final Set<Integer> TORMENTED_DEMON_IDS = ImmutableSet.of(
			NpcID.TORMENTED_DEMON,
			NpcID.TORMENTED_DEMON_13600,
			NpcID.TORMENTED_DEMON_13601,
			NpcID.TORMENTED_DEMON_13602,
			NpcID.TORMENTED_DEMON_13603,
			NpcID.TORMENTED_DEMON_13604,
			NpcID.TORMENTED_DEMON_13605,
			NpcID.TORMENTED_DEMON_13606
	);

	private final Integer tormentedDemonShieldScreechSoundId = 9259;

	private final Integer amountOfTicksBeforeAccuracyBoost = 30; //from testing, boost becomes active after the first 30 ticks following the above screech sound event

	// key = unique npc index; value = number of ticks that have passed since either the start if combat or the last fire bomb attack
	private final Map<Integer, Integer> accuracyBoostTimerMap = new HashMap<>();

	private boolean skipNextTimerReset = false;

	@Inject
	private Client client;

	@Inject
	private TormentedDemonHelperConfig config;

	@Inject
	private TormentedDemonHelperOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		accuracyBoostTimerMap.clear();
		skipNextTimerReset = false;
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		accuracyBoostTimerMap.clear();
	}


	@Provides
	TormentedDemonHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TormentedDemonHelperConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		for (Map.Entry<Integer, Integer> entry: accuracyBoostTimerMap.entrySet())
		{
			Integer npcIndex = entry.getKey();
			Integer incrementedTickCount = entry.getValue() + 1;
			accuracyBoostTimerMap.put(npcIndex, incrementedTickCount);
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		NPC npc = npcDespawned.getNpc();
		Integer npcIndex = npc.getIndex();
		if (TORMENTED_DEMON_IDS.contains(npc.getId()))
		{
			this.accuracyBoostTimerMap.remove(npcIndex);
		}
	}


	@Subscribe
	public void onSoundEffectPlayed(SoundEffectPlayed soundEffectPlayed)
	{
		if (soundEffectPlayed.getSoundId() == tormentedDemonShieldScreechSoundId) {
			if (skipNextTimerReset)
			{
				skipNextTimerReset = false;
			}
			else
			{
				this.accuracyBoostTimerMap.replaceAll((index, tick) -> 0);
			}
		}
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied)
	{
		Actor actor = hitsplatApplied.getActor();
		if (!(actor instanceof NPC))
		{
			return;
		}

		NPC npc = (NPC) actor;

		if (TORMENTED_DEMON_IDS.contains(npc.getId()) && hitsplatApplied.getHitsplat().isMine())
		{
			Integer value = this.accuracyBoostTimerMap.putIfAbsent(npc.getIndex(), 0);
			if (value == null) // if newly added to map and therefore just initiated combat with a new TD
			{
				// ignore timer reset on next tormentedDemonShieldScreechSoundId as only this new TD is screeching, not the ones that the player was already fighting
				skipNextTimerReset = true;
			}
		}
	}

	@Subscribe
	public void onInteractingChanged(InteractingChanged interactingChanged)
	{
		// ultimately want to filter interactingChanged events so that we only process TDs that have stopped interacting with the local player (changing to no target actor)
		if (interactingChanged.getTarget() != null)
		{
			return;
		}

		Actor sourceActor = interactingChanged.getSource();
		// if source actor is not an NPC and therefore not a TD, skip
		if (!(sourceActor instanceof NPC))
		{
			return;
		}

		NPC sourceNpc = (NPC) sourceActor;

		// if not a TD, skip
		if (!TORMENTED_DEMON_IDS.contains(sourceNpc.getId()))
		{
			return;
		}

		// if TD is not in combat with the local player, skip
		if (!accuracyBoostTimerMap.containsKey(sourceNpc.getIndex()))
		{
			return;
		}

		// at this point there are only interactingChanged with source = TD, target = none, and TD is in combat with the local player
		if (sourceNpc.getHealthRatio() == -1) // if the health bar of the TD is no longer rendered when its interacting changes to no target, then assume the player has safe spotted the TD and is no longer engaged in combat
		{
			accuracyBoostTimerMap.remove(sourceNpc.getIndex());
		}
	}

}
