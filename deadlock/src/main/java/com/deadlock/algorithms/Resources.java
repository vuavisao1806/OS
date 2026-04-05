package com.deadlock.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Resources {
    private List<Integer> resources;

    public Resources(List<Integer> resources) {
        this.resources = new ArrayList<>(resources);
    }

    public Resources(Resources other) {
        this(other.resources);
    }

    Resources add(Resources other) {
        if (this.resources.size() != other.resources.size()) {
            throw new RuntimeException("The two resources must be the same size before adding");
        }
        List<Integer> newResources = new ArrayList<>();
        for (int i = 0; i < this.resources.size(); ++i) {
            newResources.add(this.resources.get(i) + other.resources.get(i));
        }
        this.resources = newResources;
        return this;
    }

    Resources subtract(Resources other) {
        if (this.resources.size() != other.resources.size()) {
            throw new RuntimeException("The two resources must be the same size before subtracting");
        }
        List<Integer> newResources = new ArrayList<>();
        for (int i = 0; i < this.resources.size(); ++i) {
            newResources.add(this.resources.get(i) - other.resources.get(i));
        }
        this.resources = newResources;
        return this;
    }

    boolean isLessThanOrEqual(Resources other) {
        if (this.resources.size() != other.resources.size()) {
            throw new RuntimeException("The two resources must be the same size before comparing");
        }
        for (int i = 0; i < this.resources.size(); ++i) {
            if (this.resources.get(i) > other.resources.get(i)) {
                return false;
            }
        }
        return true;
    }

    boolean isEqualZero() {
        for (int i = 0; i < this.resources.size(); ++i) {
            if (this.resources.get(i) != 0) {
                return false;
            }
        }
        return true;
    }

    int getNumberOfResources() {
        return this.resources.size();
    }

    @Override
    public Resources clone() {
        return new Resources(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("(");
        for (Integer value: resources) {
            result.append(value).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append(")");
        return result.toString();
    }
}
