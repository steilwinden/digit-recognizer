package de.steilwinden.service.data;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Cluster {

    private final List<Point> points;

    public Cluster(Point point) {
        points = new ArrayList<>();
        points.add(point);
    }

    public void addCluster(Cluster cluster) {
        points.addAll(cluster.getPoints());
    }

    public List<Point> getPoints() {
        return points;
    }

    public boolean contains(Point point) {
        return points.contains(point);
    }
}
