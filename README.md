# Tormented Demon Helper
This plugin adds quality of life improvements when fighting Tormented Demons (TDs).

## Features:

### Accuracy boost tracking
- Roughly 30 ticks after either engaging in combat with a TD or after their fire bomb attack, the player's accuracy is increased to 100% until the next fire bomb.
- This plugin tracks this boost timer and displays it as a counter on/above the TD (position is configurable).
- The timer is color coded to distinguish between unboosted and boosted ticks (colors are configurable).

### TD Attack style highlighting
- When fighting multiple TDs, it can be difficult to keep track of which attack style each one is using.
- After a TD has performed an attack, this plugin can reactively highlight the TD with a color coded outline indicating which attack style it is currently using. 
- The highlight colors can be configured, but the default values are:
    - melee = red 
    - mage = blue
    - range = green = range
    - fire bomb = yellow
