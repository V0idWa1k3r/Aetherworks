package v0id.aw.common.tile;

import v0id.aw.common.block.forge.Component;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public interface IForgePart
{
    void onForgeTick(IForge forge);

    Component.Type getComponentType();
}
