package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;

public class InterleavedFilter implements BlueprintFilters {
    @Override
    public Blueprint BLUEPRINTEdition(Blueprint blueprint) {
        for (int i = 0; i < blueprint.getPoints().size(); i += 2) {
            blueprint.getPoints().remove(i);
            }

        return blueprint;
    }

}