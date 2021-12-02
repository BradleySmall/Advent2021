import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Submarine {
    private int horiz = 0;
    private int depth = 0;
    private int aim = 0;

    public int getHoriz() {
        return horiz;
    }

    public void forward(int distance) {
        horiz += distance;
        depth += aim * distance;
    }

    public int getDepth() {
        return depth;
    }

    public void down(int distance) {
        aim += distance;
    }

    public void up(int distance) {
        aim -= distance;
    }

    public int getPos() {
        return horiz * depth;
    }

    public void processList(List<Command> cmds) {
        for (Command cmd : cmds) {
            move(cmd);
        }
    }
    public void processList(List<Command> cmds, int steps) {
        for (int step = 0; step < steps; ++step) {
            move(cmds.get(step));
        }
    }

    private void move(Command cmd) {
        switch (cmd.action) {
            case "up":
                up(cmd.distance);
                break;
            case "down":
                down(cmd.distance);
                break;
            case "forward":
                forward(cmd.distance);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + cmd.action);
        }
    }

    class Command {
        String action;
        Integer distance;
    }
    public List<Command> getListFromFile(String fileName) throws FileNotFoundException {
        List<Command> list = new ArrayList<>();

        File f = new File(fileName);
        Scanner s = new Scanner(f);
        while(s.hasNext()) {
            Command c = new Command();
            c.action = s.next("(forward|down|up)");
            c.distance = s.nextInt();
            list.add(c);
        }

        return list;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Submarine sub = new Submarine();
        List<Command> list = sub.getListFromFile("day2/input.txt");
        sub.processList(list);
        int position = sub.getPos();
        System.out.println("Final position = " + position);
    }

}
