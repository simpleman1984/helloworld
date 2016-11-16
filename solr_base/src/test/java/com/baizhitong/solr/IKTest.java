package com.baizhitong.solr;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IKTest {
	@Test
	public void test() {
		// 单独使用
		// 检索内容
//		String text = "据说WWDC要推出iPhone6要出了？与iPhone5s相比怎样呢？@2014巴西世界杯";
//		String text = "kjfkasdx&cxvxmc vgdfg中国人说汉语";
		String text = "阳光透过树叶间的缝隙，上勾股定理解许多圆形的光斑。这是光的           形成的；我们能从不同方向看到一朵花，这是由于光在花朵表面发生了        反射。透过蓝色玻璃观察红花时，看到的花是";

		// 创建分词对象
		StringReader reader = new StringReader(text);

		IKSegmenter ik = new IKSegmenter(reader, true);// 当为true时，分词器进行最大词长切分
		Lexeme lexeme = null;
		try {
			while ((lexeme = ik.next()) != null)
				System.out.println(lexeme.getLexemeText());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
	}
}
