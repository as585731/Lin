import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class demo1 {
    public static void main(String[] args) {
        List<Integer> o1 = new ArrayList<>();
        //o1.add(new DemoI(1));
        //o1.add(new DemoI(2));
        //o1.add(new DemoI(3));
        o1.add(1);
        o1.add(2);
        o1.add(3);
        
        System.out.println(o1);
        
        
        for (Integer o : o1) {
            o+=1;
            //System.out.println(o);
        }
        
        System.out.println(o1);


    }
}
