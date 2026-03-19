package com.example.cpuschedulergui.algorithms;

import java.util.List;

public class GanttChart {
    public static String draw(List<CpuScheduler.Event> events) {
        int factor = 1;
        for (CpuScheduler.Event event: events) {
            int duration = event.getDuration();
            int lenPid = String.valueOf(event.getProcessPid()).length() + 1;
            if (lenPid > duration) {
                factor = (int) Math.ceil((double) lenPid / duration);
            }
        }
        List<StringBuilder> lines = List.of(new StringBuilder("┌"), new StringBuilder("│"), new StringBuilder("└"), new StringBuilder("0"));

        if (events.getFirst().getStartTime() > 0) {
            int space = events.getFirst().getStartTime() * factor;
            appendSpace(lines, events.getFirst().getStartTime(), -1, space, 0, false);
        }
        for (int i = 0; i < events.size(); ++i) {
            CpuScheduler.Event event = events.get(i);
            int duration = event.getDuration();
            int pid = event.getProcessPid();
            int startTime = event.getStartTime();
            int endTime = event.getEndTime();

            if (i > 0 && events.get(i - 1).getEndTime() != startTime) {
                int lastEndTime = events.get(i - 1).getEndTime();
                appendSpace(lines, startTime, -1,
                        startTime, String.valueOf(startTime).length() - 1, false);
            }

            appendSpace(lines, endTime, pid,
                    duration * factor, String.valueOf(startTime).length() - 1, (i == events.size() - 1));
        }
        StringBuilder res = new StringBuilder();
        for (StringBuilder line: lines) {
            res.append(line);
            res.append("\n");
        }
        return res.toString();
    }

    private static void appendSpace(List<StringBuilder> lines,
                                    int endTime, int pid,
                                    int space, int offset, boolean isLast) {
        // pid = -1 mean empty
        assert (lines.size() == 4);
        lines.get(0).append("─".repeat(space));
        lines.get(0).append((isLast ? "┐" : "┬"));
        lines.get(2).append("─".repeat(space));
        lines.get(2).append((isLast ? "┘" : "┴"));

        if (pid == -1) {
            lines.get(1).append(" ".repeat(space));
            lines.get(1).append("│");
        } else {
            String sPid = "P" + String.valueOf(pid);
            int leftSpace = (space - sPid.length()) / 2;
            lines.get(1).append(" ".repeat(leftSpace));
            lines.get(1).append(sPid);
            lines.get(1).append(" ".repeat(space - leftSpace - sPid.length()));
            lines.get(1).append("│");
        }

        lines.get(3).append(" ".repeat(space - offset));
        lines.get(3).append(endTime);
    }
}
