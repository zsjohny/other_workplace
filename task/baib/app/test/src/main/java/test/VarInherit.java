package test;

class Person {
    String name;        
    int age; // 声明两个成员变量        
    public Person(String name, int age) {   // 有参构造方法
    this.name = name;
    this.age = age; 
    System.out.println("父类的有参构造器：");
    }

    public Person() {       // 无参构造方法
    this.name = "person name";  
    this.age = 23;  
    System.out.println("父类的无参构造器：");
    }
    
    void pprint() {     // 成员方法，此时显示的是父类中成员变量的结果
    System.out.println("class:Person;  " + "Name: " + this.name + ";  age: "+ this.age);
    }
}
class Student extends Person {  // 基于Person类定义Student子类
      String name;      // 在派生类中声明自己的成员变量
      int age;
      int classno;      // 声明新成员变量
      public Student() {    // 无参构造方法
      this.name = "student name";    
      this.age = 20;
      System.out.println("子类的无参构造器：");
      }
      public Student(String name, int age, int classno) {// 有参构造方法
      this.name = name;
      this.age = age;
      this.classno = classno;
      System.out.println("子类的有参构造器：");
      }
      void sprint() {   // 成员方法，此时显示的是子类中成员变量的结果
      System.out.println("class:Student;  " + "Name: " + this.name + ";"
            + "  age: "+ this.age + "; " + "   "+ "   classno: " + this.classno);
      }
}


public class VarInherit {       // 声明公共类
    public static void main(String[] args) {
           Student obj1 = new Student(); // 调用无参构造方法创建对象
           System.out.println("-----------"+obj1.name+"---"+obj1.age);
           System.out.println("--------------");
           Student obj2 = new Student("LiXiao", 18, 1); // 调用有参构造方法创建对象
           Person obj3 = new Person("guangzhu", 55);
           System.out.println("---/////////////--pprint");
            obj1.pprint();   
            obj2.pprint();  // 调用父类的成员方法
            System.out.println("/////////////---sprint");
            obj1.sprint();   
            obj2.sprint();  // 调用子类的成员方法
            
            obj3.pprint();
            System.out.println("/////////////");

    }
}