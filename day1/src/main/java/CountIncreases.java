import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CountIncreases {
    public int countIncreases(String fileName) throws FileNotFoundException {
        return count(getListFromFile(fileName));
    }
    public int countIncreases(String fileName, int slideCount) throws FileNotFoundException {
        return count(slideCountList(getListFromFile(fileName),slideCount));
    }

    public int count(List<Integer> list) {
        int count = 0;
        Integer last = list.get(0);

        for (Integer element : list) {
            if (element > last) ++count;
            last = element;
        }
        return count;
    }

    public List<Integer> getListFromFile(String fileName) throws FileNotFoundException {
        List<Integer> list = new ArrayList<Integer>();

        File f = new File(fileName);
        Scanner s = new Scanner(f);
        while(s.hasNext()) {
            list.add(Integer.parseInt(s.nextLine()));
        }

        return list;
    }
    public List<Integer> slideCountList(List<Integer> l, int count) {
        List list = new ArrayList<Integer>();

        int current = 0;
        int value = 0;

        while (current <= l.size() - count) {
            for (int x = 0; x < count; ++x) {
                value += l.get(current + x);
            }
            list.add(value);
            value = 0;
            current += 1;
        }



       return list;
    }

    public static void main(String[] args) {
        CountIncreases ci = new CountIncreases();
        try {
            int count = ci.countIncreases("day1/input.txt");
            System.out.println("Increases " + count);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            int count = ci.countIncreases("input.txt", 3);
            System.out.println("Increases " + count);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
