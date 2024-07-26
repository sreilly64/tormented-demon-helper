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
		description = "Provides QoL improvements for fighting Tormented Demons",
		tags = {"combat", "pve", "pvm", "slayer", "td", "tormented demon"}
)
public class TormentedDemonHelperPlugin extends Plugin
{
	private static final Set<Integer> TD_IDS = ImmutableSet.of(
			NpcID.TORMENTED_DEMON,
			NpcID.TORMENTED_DEMON_13600,
			NpcID.TORMENTED_DEMON_13601,
			NpcID.TORMENTED_DEMON_13602,
			NpcID.TORMENTED_DEMON_13603,
			NpcID.TORMENTED_DEMON_13604,
			NpcID.TORMENTED_DEMON_13605,
			NpcID.TORMENTED_DEMON_13606
	);

	private final String TORMENTED_DEMON = "Tormented Demon";

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
		if (TD_IDS.contains(npc.getId()))
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

			Integer currentTick = client.getTickCount();
			log.info("{} - TD screech played and timers reset", currentTick);
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

		if (TD_IDS.contains(npc.getId()) && hitsplatApplied.getHitsplat().isMine())
		{
			//TODO remove logging
//			Integer currentTick = client.getTickCount();
//			log.info("{} - Hit TD {} for {} damage", currentTick, npc.getIndex(), hitsplatApplied.getHitsplat().getAmount());

			Integer value = this.accuracyBoostTimerMap.putIfAbsent(npc.getIndex(), 0);
			if (value == null) // if newly added to map and therefore just initiated combat with a new TD
			{
				// ignore timer reset on next tormentedDemonShieldScreechSoundId as only this new TD is screeching, not the ones that the player was already fighting
				skipNextTimerReset = true;
			}
		}
	}

	//TODO for attack style detection
//	@Subscribe
//	public void onAnimationChanged(AnimationChanged animationChanged)
//	{
//		Actor actor = animationChanged.getActor();
//		if (!(actor instanceof NPC))
//		{
//			return;
//		}
//		NPC npc = (NPC) actor;
//		log.info("npc id = {}", npc.getId());
//		if (Text.removeTags(actor.getName()).equalsIgnoreCase(TORMENTED_DEMON))
//		{
//			log.info("TD getAnimation = {}, getPoseAnimation = {}", actor.getAnimation(), actor.getPoseAnimation());
//		}
//	}

}
