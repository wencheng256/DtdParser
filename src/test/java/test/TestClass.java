package test;

import com.wencheng.util.FileParseDtd;
import org.junit.Test;

/**
 * Created by Administrator on 2016/12/4 0004.
 */
public class TestClass {

    @Test
    public void testSplit(){
        String[] split = "property|many-to-one|one-to-one|component|dynamic-component|properties|any|map|set|list|bag|idbag|array|primitive-array".split("\\|");
        for(int i = 0; i<split.length; i++){
            System.out.println(split[i]);
        }
    }

    @Test
    public void testParse(){
        FileParseDtd f = new FileParseDtd("1.dtd");
        System.out.println(f.getJson("first_item_256","起始"));
    }

}
