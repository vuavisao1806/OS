package com.deadlock.algorithms;

import java.util.ArrayList;
import java.util.List;

public class ResourceRequestSolver extends SafetySolver {
    private final Resources request;
    private final int requestPid;

    public ResourceRequestSolver(int requestPid, Resources request,
                                 Resources initialResources,
                                 List<Resources> allocation,
                                 List<Resources> max) {
        super(initialResources, allocation, max);
        this.request = request.clone();
        this.requestPid = requestPid;

        if (!request.isLessThanOrEqual(need.get(requestPid))) {
            throw new RuntimeException("The request resources must be less than or equal the need resources");
        }
    }

    @Override
    void runAlgorithm() {
        if (!request.isLessThanOrEqual(available)) {
            return;
        }
        available.subtract(request);
        work = available.clone();
        allocation.get(requestPid).add(request);
        need.get(requestPid).subtract(request);
        super.runAlgorithm();
    }

    @Override
    String getResult() {
        if (result == null) {
            runAlgorithm();
        }
        if (result == null || result.size() < need.size()) {
            return "The system is not safe if accept the request " + request + " of P" + requestPid;
        }
        StringBuilder sResult = new StringBuilder("The system is safe if accept the request " + request + " of P" + requestPid + " (<");
        for (int i = 0; i < result.size(); ++i) {
            sResult.append(result.get(i));
            if (i < result.size() - 1) sResult.append(", ");
        }
        sResult.append(">)");
        return sResult.toString();
    }

    @Override
    public String toString() {
        return "ResourceRequestSolver{" +
                "\ninitialResources=" + initialResources +
                "\navailable=" + available +
                "\nallocation=" + allocation +
                "\nneed=" + need +
                "\nmax=" + max +
                "\nrequest=" + request +
                "\nrequestPid=" + requestPid +
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

//        int requestPid = 1;
//        Resources request = new Resources(List.of(1, 0, 2));
//        int requestPid = 4;
//        Resources request = new Resources(List.of(3, 3, 0));
        int requestPid = 0;
        Resources request = new Resources(List.of(0, 2, 0));
        ResourceRequestSolver resourceRequestSolver = new ResourceRequestSolver(
                requestPid, request,
                initialResources, allocation, max
        );
//        System.out.println(resourceRequestSolver);
        System.out.println(resourceRequestSolver.getResult());
//        System.out.println(resourceRequestSolver);
    }
}
