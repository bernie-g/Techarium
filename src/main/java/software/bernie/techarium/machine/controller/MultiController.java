package software.bernie.techarium.machine.controller;

import java.util.ArrayList;
import java.util.List;

public class MultiController {

    private final List<MachineController<?>> controllers = new ArrayList<>();

    private int activeTier = 1;

    public MultiController(){
    }

    public void addController(MachineController<?> controller){
        controllers.add(controller);
    }

    public MachineController<?> getControllerByTier(int tier){
        if(controllers.size() <= activeTier){
           return controllers.stream().filter(controller -> controller.getTier() == tier).findFirst().orElseGet(() -> controllers.get(0));
        }
        return controllers.get(0);
    }

    public int getActiveTier() {
        return activeTier;
    }

    public void setActiveTier(int activeTier) {
        this.activeTier =  Math.max(Math.min(1, activeTier),controllers.size());
    }

    public MachineController<?> getActiveController(){
        if(controllers.size() > 1){
           return controllers.stream().filter(controller -> controller.getTier() == getActiveTier()).findFirst().orElseGet(() -> controllers.get(0));
        }
        return controllers.get(0);
    }
}
