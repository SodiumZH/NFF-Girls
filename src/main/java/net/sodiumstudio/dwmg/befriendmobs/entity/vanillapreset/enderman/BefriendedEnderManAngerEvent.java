package net.sodiumstudio.dwmg.befriendmobs.entity.vanillapreset.enderman;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@SuppressWarnings("unused")
@Cancelable
public class BefriendedEnderManAngerEvent extends LivingEvent
{
    protected final Player player;

    public BefriendedEnderManAngerEvent(AbstractBefriendedEnderMan enderman, Player player)
    {
        super(enderman);
        this.player = player;
    }

    /**
     * The player that is being checked.
     */
    public Player getPlayer()
    {
        return player;
    }

    @Override
    public AbstractBefriendedEnderMan getEntity()
    {
        return (AbstractBefriendedEnderMan) super.getEntity();
    }
}
