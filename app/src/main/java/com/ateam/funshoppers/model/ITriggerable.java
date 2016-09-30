
package com.ateam.funshoppers.model;

import java.util.List;

public interface ITriggerable {
    List<ActionBeacon> getActions();

    void addAction(ActionBeacon action);

    void addActions(List<ActionBeacon> actions);
}
