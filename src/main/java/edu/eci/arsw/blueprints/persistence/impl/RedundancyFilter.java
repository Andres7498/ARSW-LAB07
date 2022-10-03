package edu.eci.arsw.blueprints.persistence.impl;


import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;

public class RedundancyFilter implements BlueprintFilters {
    @Override
    public Blueprint BLUEPRINTEdition(Blueprint blueprint) {
        Blueprint blueprintFiltered = new Blueprint(blueprint.getAuthor(), blueprint.getName());
        for (int i = 0; i < blueprint.getPoints().size() - 1; i += 2) {
            if (blueprint.getPoints().get(i).getX() == blueprint.getPoints().get(i + 1).getX() &&
                    blueprint.getPoints().get(i).getY() == blueprint.getPoints().get(i + 1).getY()) {
                Point point = new Point(blueprint.getPoints().get(i).getX(), blueprint.getPoints().get(i).getY());
                blueprintFiltered.addPoint(point);
            } else {
                Point point = new Point(blueprint.getPoints().get(i).getX(), blueprint.getPoints().get(i).getY());
                Point point2 = new Point(blueprint.getPoints().get(i + 1).getX(), blueprint.getPoints().get(i + 1).getY());
                blueprintFiltered.addPoint(point);
                blueprintFiltered.addPoint(point2);
            }
        }
        return blueprintFiltered;
    }
}