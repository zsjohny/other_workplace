import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("G:\\Users\\Ness\\Pictures\\1.png");
        ITesseract instance = new Tesseract();
        instance.setLanguage("eng");
//        instance.setLanguage("chi_sim");
        instance.setDatapath("G:\\Users\\Ness\\Desktop\\tessdata");
        try {

            String result = instance.doOCR(file);

            System.out.println("图片名：2.png  识别结果：" + result);

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
