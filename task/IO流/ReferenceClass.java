import java.lang.ref.SoftReference;

public class ReferenceClass {
    static class A {
        private byte[] a;

        public A() {
            this.a = "111".getBytes();
        }
    }

    private static void getSoft() {
        A a = new A();
        SoftReference<A> soft = new SoftReference<>(a);
        for (int i = 0; i < 1000; i++) {
            if (soft != null) {
                a = soft.get();
                System.out.println("soft:"+new String(a.a));
            } else {
                a = new A();
                soft = new SoftReference<>(a);
                System.err.println("fort:"+new String(a.a));
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        getSoft();

    }
}
