import objects.*;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class test{
   public static void main(String[]args){
   
      ArrayList<BusStudent> list=new ArrayList<BusStudent>();
      list.add(new BusStudent(1, "moaz mahm bai","fatherPhone","motherPhone", "grade",  1,  1,  "path",  "subscription"));
      list.add(new BusStudent(1, "moaaz ahmed dddd","fatherPhone","motherPhone", "grade",  1,  1,  "path",  "subscription"));
      list.add(new BusStudent(1, "sdvvv sdsdv mahmoud","fatherPhone","motherPhone", "grade",  1,  1,  "path",  "subscription"));

      
      System.out.println(list.get(0).getName());
      
      String s="moaaz mahmoud baiumy";
      StringTokenizer t =new StringTokenizer(s," ");
      System.out.println(t.countTokens());
      
      
      BusStudent testStudent = new BusStudent(1, "moaz mahm bai","mmm","motherPhone", "grade",  1,  1,  "path",  "subscription");
      
      
      System.out.println(list.get(0).equals(testStudent));
      
//       BusStudent testStudent = new BusStudent(1, "moaz mahm bai","fatherPhone","motherPhone", "grade",  1,  1,  "path",  "subscription");

      
      
      
   }
}
