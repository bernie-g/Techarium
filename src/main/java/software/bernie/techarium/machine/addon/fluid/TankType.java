package software.bernie.techarium.machine.addon.fluid;

public enum TankType {
    FILL_ONLY(true, false),
    DRAIN_ONLY(false, true),
    FILL_AND_DRAIN(true, true),
    NONE(false, false);

    private final boolean fillable;
    private final boolean drainable;

    TankType(boolean fillable, boolean drainable){
        this.drainable = drainable;
        this.fillable = fillable;
    }

    public boolean canBeFilled(){
        return fillable;
    }

    public boolean canBeDrained(){
        return drainable;
    }

}
