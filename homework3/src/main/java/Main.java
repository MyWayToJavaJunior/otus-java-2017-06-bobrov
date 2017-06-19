import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        myArrayList.add(10);
        myArrayList.add(9);
        myArrayList.add(8);
        myArrayList.add(7);
        myArrayList.add(6);
        myArrayList.add(5);
        myArrayList.add(4);
        myArrayList.add(3);
        myArrayList.add(2);
        myArrayList.add(1);
        Collections.addAll(myArrayList, 205, 204, 203, 202, 201);
        for (Integer i : myArrayList) System.out.print(i + " ");
        System.out.println();
        List<Integer> list = new ArrayList<>();
        list.add(304);
        list.add(303);
        list.add(302);
        list.add(301);
        list.add(300);
        Collections.copy(myArrayList, list);
        for (Integer i : myArrayList) System.out.print(i + " ");
        System.out.println();
        myArrayList.sort(Integer::compareTo);
        for (Integer i : myArrayList) System.out.print(i + " ");
        System.out.println();
    }
}
