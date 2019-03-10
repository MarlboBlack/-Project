import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;



public class Demo {

    public String getCode(){
        Random random = new Random();
        String str = random.nextInt(10000)+"";
        if(str.length() != 4){
            return getCode();
        }
        return str;
    }
    public static void main(String[] args){
        StringBuffer s = new StringBuffer();
        String companyCode = "00000004";
        s.append(companyCode);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        s.append(formatter.format(date));
        s.append(new Demo().getCode());
        System.out.println(s);
    }



}
