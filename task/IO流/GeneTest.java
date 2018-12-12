package org.finace.utils.test;

/**
 * Created by Ness on 2016/12/23.
 */
public class GeneTest {

    public void text() {
        System.out.println("into");
    }


    /**
     *  Class<T>的作用就是指明泛型的具体类型，而Class<T>类型的变量c，可以用来创建泛型类的对象。
     泛型方法不是仅仅可以有一个参数Class<T>，可以根据需要添加其他参数。
     * @param t
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T newStance(Class<T> t) throws IllegalAccessException, InstantiationException {

        return t.newInstance();
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        GeneTest.newStance(GeneTest.class).text();


    }

}
