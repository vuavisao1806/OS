package com.deadlock.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    private int numberOfProcesses;
    private int numberOfResources;
    private Resources initialResources;
    private List<Resources> allocation;
    private List<Resources> max;
    private List<Resources> requests;
    private Resources request;
    private int requestPid;

    public Parser(AlgorithmType algorithmType, String path) throws FileNotFoundException {
        numberOfProcesses = numberOfResources = 0;
        allocation = new ArrayList<>();
        max = new ArrayList<>();
        requests = new ArrayList<>();

        Scanner scanner = new Scanner(new File(path));
        switch (algorithmType) {
            case SAFETY_SOLVER -> readSafetySolver(scanner);
            case RESOURCE_REQUEST_SOLVER -> readResourceRequestSolver(scanner);
            case DEADLOCK_DETECTION_SOLVER -> readDeadlockDetectionSolver(scanner);
        }
        scanner.close();
    }

    void readSafetySolver(Scanner scanner) {
        String line;
        int numberOfLines = 0;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] tokens = line.split(" ");
            if (numberOfLines == 0) {
                assert tokens.length == 2;
                numberOfProcesses = Integer.parseInt(tokens[0]);
                numberOfResources = Integer.parseInt(tokens[1]);
            } else if (numberOfLines == 1) {
                assert tokens.length == numberOfResources;
                List<Integer> resources = new ArrayList<>();
                for (String token: tokens) {
                    resources.add(Integer.parseInt(token));
                }
                initialResources = new Resources(resources);
            } else if (numberOfLines <= numberOfProcesses + 1) {
                assert tokens.length == numberOfResources * 2;
                List<Integer> resources = new ArrayList<>();
                for (int i = 0; i < numberOfResources; ++i) {
                    resources.add(Integer.parseInt(tokens[i]));
                }
                allocation.add(new Resources(resources));

                resources.clear();
                for (int i = numberOfResources; i < numberOfResources * 2; ++i) {
                    resources.add(Integer.parseInt(tokens[i]));
                }
                max.add(new Resources(resources));
            } else {
                assert false;
            }

            ++numberOfLines;
        }
    }

    void readResourceRequestSolver(Scanner scanner) {
        String line;
        int numberOfLines = 0;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] tokens = line.split(" ");
            if (numberOfLines == 0) {
                assert tokens.length == 2;
                numberOfProcesses = Integer.parseInt(tokens[0]);
                numberOfResources = Integer.parseInt(tokens[1]);
            } else if (numberOfLines == 1) {
                assert tokens.length == numberOfResources;
                List<Integer> resources = new ArrayList<>();
                for (String token: tokens) {
                    resources.add(Integer.parseInt(token));
                }
                initialResources = new Resources(resources);
            } else if (numberOfLines <= numberOfProcesses + 1) {
                assert tokens.length == numberOfResources * 2;
                List<Integer> resources = new ArrayList<>();
                for (int i = 0; i < numberOfResources; ++i) {
                    resources.add(Integer.parseInt(tokens[i]));
                }
                allocation.add(new Resources(resources));

                resources.clear();
                for (int i = numberOfResources; i < numberOfResources * 2; ++i) {
                    resources.add(Integer.parseInt(tokens[i]));
                }
                max.add(new Resources(resources));
            } else if (numberOfLines == numberOfProcesses + 2) {
                assert tokens.length == numberOfResources + 1;
                requestPid = Integer.parseInt(tokens[0]);

                List<Integer> resources = new ArrayList<>();
                for (int i = 1; i < tokens.length; ++i) {
                    resources.add(Integer.parseInt(tokens[i]));
                }
                request = new Resources(resources);
            } else {
                assert false;
            }

            ++numberOfLines;
        }
    }

    void readDeadlockDetectionSolver(Scanner scanner) {
        String line;
        int numberOfLines = 0;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] tokens = line.split(" ");
            if (numberOfLines == 0) {
                assert tokens.length == 2;
                numberOfProcesses = Integer.parseInt(tokens[0]);
                numberOfResources = Integer.parseInt(tokens[1]);
            } else if (numberOfLines == 1) {
                assert tokens.length == numberOfResources;
                List<Integer> resources = new ArrayList<>();
                for (String token: tokens) {
                    resources.add(Integer.parseInt(token));
                }
                initialResources = new Resources(resources);
            } else if (numberOfLines <= numberOfProcesses + 1) {
                assert tokens.length == numberOfResources * 2;
                List<Integer> resources = new ArrayList<>();
                for (int i = 0; i < numberOfResources; ++i) {
                    resources.add(Integer.parseInt(tokens[i]));
                }
                allocation.add(new Resources(resources));

                resources.clear();
                for (int i = numberOfResources; i < numberOfResources * 2; ++i) {
                    resources.add(Integer.parseInt(tokens[i]));
                }
                requests.add(new Resources(resources));
            } else {
                assert false;
            }

            ++numberOfLines;
        }
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public int getNumberOfResources() {
        return numberOfResources;
    }

    public Resources getInitialResources() {
        return initialResources;
    }

    public List<Resources> getAllocation() {
        return allocation;
    }

    public List<Resources> getMax() {
        return max;
    }

    public List<Resources> getRequests() {
        return requests;
    }

    public Resources getRequest() {
        return request;
    }

    public int getRequestPid() {
        return requestPid;
    }
}
