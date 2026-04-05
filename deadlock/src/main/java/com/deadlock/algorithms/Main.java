package com.deadlock.algorithms;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new RuntimeException("The number of arguments must be two (the algorithm type and the path to the input file)");
        }
        String type = args[0];
        String path = args[1];

        try {
            switch (type) {
                case "Safety" -> {
                    Parser parser = new Parser(AlgorithmType.SAFETY_SOLVER, path);
                    SafetySolver safetySolver = new SafetySolver(
                            parser.getInitialResources(),
                            parser.getAllocation(),
                            parser.getMax());
                    System.out.println(safetySolver);
                    System.out.println(safetySolver.getResult());
                }
                case "Request" -> {
                    Parser parser = new Parser(AlgorithmType.RESOURCE_REQUEST_SOLVER, path);
                    ResourceRequestSolver resourceRequestSolver = new ResourceRequestSolver(
                            parser.getRequestPid(), parser.getRequest(),
                            parser.getInitialResources(),
                            parser.getAllocation(),
                            parser.getMax());
                    System.out.println(resourceRequestSolver.getResult());
                    System.out.println(resourceRequestSolver);
                }
                case "Detection" -> {
                    Parser parser = new Parser(AlgorithmType.DEADLOCK_DETECTION_SOLVER, path);
                    DeadlockDetectionSolver deadlockDetectionSolver = new DeadlockDetectionSolver(
                            parser.getInitialResources(),
                            parser.getAllocation(),
                            parser.getRequests());
                    System.out.println(deadlockDetectionSolver);
                    System.out.println(deadlockDetectionSolver.getResult());
                }
                default -> throw new RuntimeException("The algorithm is not implemented yet");
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("The input file argument is not correct (eg. the file does not exist)");
        }
    }
}
