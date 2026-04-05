package com.deadlock.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeadlockDetectionSolver {
    protected final Resources initialResources;
    protected final Resources available;
    protected final List<Resources> allocation;
    protected final List<Resources> request;

    protected Resources work;
    protected final Map<Integer, Boolean> finish;

    protected List<String> result;

    public DeadlockDetectionSolver(Resources initialResources,
                        List<Resources> allocation,
                        List<Resources> request) {
        this.initialResources = initialResources.clone();
        this.allocation = new ArrayList<>();
        for (Resources resources: allocation) this.allocation.add(resources.clone());
        this.request = new ArrayList<>();
        for (Resources resources: request) this.request.add(resources.clone());

        finish = new HashMap<>();
        for (int i = 0; i < request.size(); ++i) {
            if (allocation.get(i).isEqualZero()) {
                finish.put(i, true);
            } else {
                finish.put(i, false);
            }
        }

        available = initialResources.clone();
        for (Resources resources: allocation) {
            if (!resources.isLessThanOrEqual(available)) {
                throw new RuntimeException("The allocated resources must be less than or equal the initial resources");
            }
            available.subtract(resources);
        }
        work = available.clone();
        result = null;
    }

    void runAlgorithm() {
        result = new ArrayList<>();
        for (int i = 0; i < request.size(); ++i) {
            boolean find = false;
            for (int pid = 0; pid < request.size(); ++pid) {
                if (finish.get(pid) == true) {
                    continue;
                }
                if (request.get(pid).isLessThanOrEqual(work)) {
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
        StringBuilder sResult;
        if (result.size() < request.size()) {
            sResult = new StringBuilder("The system is in a deadlocked state (consisting of processes ");
            for (var entry: finish.entrySet()) {
                if (!entry.getValue()) {
                    sResult.append("P" + entry.getKey()).append(", ");
                }
            }
            sResult.delete(sResult.length() - 2, sResult.length());
            sResult.append(").");
        } else {
            sResult = new StringBuilder("The system is not in a deadlocked state safe (<");
            for (int i = 0; i < result.size(); ++i) {
                sResult.append(result.get(i));
                if (i < result.size() - 1) sResult.append(", ");
            }
            sResult.append(">)");
        }
        return sResult.toString();
    }

    @Override
    public String toString() {
        return "SafetySolver{" +
                "\ninitialResources=" + initialResources +
                "\navailable=" + available +
                "\nallocation=" + allocation +
                "\nrequest=" + request +
                "\n}";
    }

    static void main() {
        Resources initialResources = new Resources(List.of(7, 2, 6));
        List<Resources> allocation = new ArrayList<>();
        allocation.add(new Resources(List.of(0, 1, 0)));
        allocation.add(new Resources(List.of(2, 0, 0)));
        allocation.add(new Resources(List.of(3, 0, 3)));
        allocation.add(new Resources(List.of(2, 1, 1)));
        allocation.add(new Resources(List.of(0, 0, 2)));
        List<Resources> request = new ArrayList<>();
        request.add(new Resources(List.of(0, 0, 0)));
        request.add(new Resources(List.of(2, 0, 2)));
        request.add(new Resources(List.of(0, 0, 1)));
        request.add(new Resources(List.of(1, 0, 0)));
        request.add(new Resources(List.of(0, 0, 2)));

        DeadlockDetectionSolver deadlockDetectionSolver = new DeadlockDetectionSolver(initialResources, allocation, request);
        System.out.println(deadlockDetectionSolver);
        System.out.println(deadlockDetectionSolver.getResult());
    }
}
