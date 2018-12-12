package test;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringTokenTest {
  public static void main(String[] args) {
      String str = "abc,def;gh,ij;k;lm,no,p;qr,st";
      StringTokenizer st = new StringTokenizer(str,",;");
       while(st.hasMoreTokens() ){
          System.out.println(st.nextToken());
      }
       String images="540b3aed-c461-432e-b992-782dbbc1f107.jpg,73e45763-a7fd-46de-8f41-e030a37952d3.jpg";
        String[] imgList = images.split(",");
        System.out.println(imgList);
    }
}
