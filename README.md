# UltraMinions高仿HpySkyblock的小人插件
spigot原帖地址https://www.spigotmc.org/resources/ultra-minions-0-lag-custom-minion-skins-upgrades-fuels-dialogues-perk-skyblock-or-survival.72434/

这是一个烂尾的付费插件，作者不再更新了。但是这是全网做的和hpy最相似的小人插件，具有一下鲜明的特性

* 小人挥手的动效
* 小人破坏方块的特效（发包实现）
* 自定义小人
* 自定义小人的动作动画

这些特点都是其他的小人插件所不具备的，很可惜不支持1.18以后的版本。所以本人对此进行魔改

需要修改的地方是底层的发包，插件原本使用NMS进行NBT的编辑和发包，本人修改后，插件依赖
NBTAPI和
Protocollib进行发包
因此本插件理论支持所有版本
 
