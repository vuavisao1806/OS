package com.deadlock.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SafetySolver {
    protected final Resources initialResources;
    protected final Resources available;
    protected final List<Resources> allocation;
    protected final List<Resources> need;
    protected final List<Resources> max;

    protected Resources work;
    protected final Map<Integer, Boolean> finish;

    protected List<String> result;

    public SafetySolver(Resources initialResources,
                        List<Resources> allocation,
                        List<Resources> max) {
        this.initialResources = initialResources.clone();
        this.allocation = new ArrayList<>();
        for (Resources resources: allocation) this.allocation.add(resources.clone());
        this.max = new ArrayList<>();
        for (Resources resources: max) this.max.add(resources.clone());

        finish = new HashMap<>();
        for (int i = 0; i < max.size(); ++i) {
            finish.put(i, false);
        }

        available = initialResources.clone();
        for (Resources resources: allocation) {
            if (!resources.isLessThanOrEqual(available)) {
                throw new RuntimeException("The allocated resources must be less than or equal the initial resources");
            }
            available.subtract(resources);
        }
        work = available.clone();

        this.need = new ArrayList<>();
        for (int i = 0; i < allocation.size(); ++i) {
            if (!allocation.get(i).isLessThanOrEqual(max.get(i))) {
                throw new RuntimeException("The allocated resources must be less than or equal the max resources");
            }
            this.need.add(max.get(i).clone().subtract(allocation.get(i)));
        }
        result = null;
    }

    void runAlgorithm() {
        result = new ArrayList<>();
        for (int i = 0; i < need.size(); ++i) {  // iteration
            boolean find = false;
            for (int pid = 0; pid < need.size(); ++pid) { // pid
                if (finish.get(pid) == true) {
                    continue;
                }
                if (need.get(pid).isLessThanOrEqual(work)) {
                    find = true;
                    work.add(allocation.get(pid));
                    result.add("P" + pid);
                    finish.put(pid, true);
//                    System.out.println(work);
                    break;
                }
            }
            if (!find) {
                break;
            }
        }
    }

    String getResult() {
        if (result == null) {
            runAlgorithm();
        }
        if (result.size() < need.size()) {
            return "The system is not safe";
        }
        StringBuilder sResult = new StringBuilder("The system is safe (<");
        for (int i = 0; i < result.size(); ++i) {
            sResult.append(result.get(i));
            if (i < result.size() - 1) sResult.append(", ");
        }
        sResult.append(">)");
        return sResult.toString();
    }

    @Override
    public String toString() {
        return "SafetySolver{" +
                "\ninitialResources=" + initialResources +
                "\navailable=" + available +
                "\nallocation=" + allocation +
                "\nneed=" + need +
                "\nmax=" + max +
                "\n}";
    }

    static void main() {
        Resources initialResources = new Resources(List.of(10, 5, 7));
        List<Resources> allocation = new ArrayList<>();
        allocation.add(new Resources(List.of(0, 1, 0)));
        allocation.add(new Resources(List.of(2, 0, 0)));
        allocation.add(new Resources(List.of(3, 0, 2)));
        allocation.add(new Resources(List.of(2, 1, 1)));
        allocation.add(new Resources(List.of(0, 0, 2)));
        List<Resources> max = new ArrayList<>();
        max.add(new Resources(List.of(7, 5, 3)));
        max.add(new Resources(List.of(3, 2, 2)));
        max.add(new Resources(List.of(9, 0, 2)));
        max.add(new Resources(List.of(2, 2, 2)));
        max.add(new Resources(List.of(4, 3, 3)));

        SafetySolver safetySolver = new SafetySolver(initialResources, allocation, max);
        System.out.println(safetySolver);
        System.out.println(safetySolver.getResult());
    }
}
