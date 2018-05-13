package v0id.aw.common.tile;

import teamroots.embers.power.DefaultEmberCapability;

/**
 * Created by V0idWa1k3r on 02-Jun-17.
 */
public class SyncableEmberCapacity extends DefaultEmberCapability
{
    private final ISyncable owner;

    public SyncableEmberCapacity(ISyncable syncable)
    {
        super();
        this.owner = syncable;
    }

    @Override
    public void setEmber(double value)
    {
        super.setEmber(value);
        if (!this.owner.isRemote())
        {
            this.owner.sync();
        }
    }

    @Override
    public void setEmberCapacity(double value)
    {
        super.setEmberCapacity(value);
        if (!this.owner.isRemote())
        {
            this.owner.sync();
        }
    }

    @Override
    public double addAmount(double value, boolean doAdd)
    {
        double added = super.addAmount(value, doAdd);
        if (!this.owner.isRemote() && doAdd && added > 0)
        {
            this.owner.sync();
        }

        return added;
    }

    @Override
    public double removeAmount(double value, boolean doRemove)
    {
        double removed = super.removeAmount(value, doRemove);
        if (!this.owner.isRemote() && doRemove && removed > 0)
        {
            this.owner.sync();
        }

        return removed;
    }
}
