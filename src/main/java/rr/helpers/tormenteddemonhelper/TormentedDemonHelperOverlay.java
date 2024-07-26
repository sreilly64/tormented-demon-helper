package rr.helpers.tormenteddemonhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;
import java.util.Map;

@Slf4j
public class TormentedDemonHelperOverlay extends Overlay {

    private final TormentedDemonHelperPlugin plugin;
    private final TormentedDemonHelperConfig config;
    private final Client client;

    @Inject
    public TormentedDemonHelperOverlay(TormentedDemonHelperPlugin plugin, TormentedDemonHelperConfig config, Client client) {
        super(plugin);
        this.config = config;
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    private void renderAccuracyBoostTimerOverlay(Graphics2D graphics, NPC npc, String text, Color color)
    {
        Integer zOffset = npc.getLogicalHeight() + (this.config.verticalOffset() - 70) * 10; // minimum of -700 places the overlay at the TDs feet
        Point textLocation = npc.getCanvasTextLocation(graphics, text, zOffset);
        if (textLocation != null)
        {
            OverlayUtil.renderTextLocation(graphics, textLocation, text, color);
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (config.showTimer())
        {
            // set font type and size from config panel
            if (config.fontType() == FontTypes.REGULAR)
            {
                graphics.setFont(new Font(FontManager.getRunescapeFont().getName(), Font.PLAIN, config.fontSize()));
            }
            else
            {
                graphics.setFont(new Font(config.fontType().toString(), Font.PLAIN, config.fontSize()));
            }

            // render timer overlay onto each TD that is currently being fought
            for (Map.Entry<Integer, Integer> entry: this.plugin.getAccuracyBoostTimerMap().entrySet())
            {
                Integer npcIndex = entry.getKey();
                Integer currentTimerCount = entry.getValue();
                Color color = currentTimerCount > this.plugin.getAmountOfTicksBeforeAccuracyBoost() ? this.config.boostedTickColor() : this.config.unboostedTickColor();
                renderAccuracyBoostTimerOverlay(graphics, client.getLocalPlayer().getWorldView().npcs().byIndex(npcIndex), currentTimerCount.toString(), color);
            }
        }
        return null;
    }
}
