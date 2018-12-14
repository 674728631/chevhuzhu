package com.zccbh.demand.controller.weChat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * 微信消息解析帮助类型
 * @author Administrator
 *
 */
public class XmlPullParserUtil {

	/**
	 * 参数: InputStream,Reader 为了兼容测试,使用Reader
	 * 返回值:
	 *     方案1:解析为一个java对象,抽取一个基础类BaseMsg,里面包含所有消息的公共属性,为每一种消息再定制一个子类,
	 *     让它继承与BaseMsg并且扩展自己属性(TextMsg,ImgMsg....)
	 *     方案2:直接返回一个Map,可以表示任何消息,消息的key不一致罢了.(采纳)
	 * 
	 * @return
	 */
	public static Map<String, String> parse(Reader reader) {
		Map<String, String> result = new HashMap<>();
		try {
			//1)获取xmlpull解析器
			XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
			//2)设置解析内容
			xmlPullParser.setInput(reader);
			//3)进行解析
			//xmlpull常用事件
			//XmlPullParser.START_DOCUMENT 文档开始
			//XmlPullParser.START_TAG 标签开始
			//XmlPullParser.END_DOCUMENT 文档结束
			//XmlPullParser.END_TAG 标签结束
			//xmlpull当前事件状态
			int eventType = xmlPullParser.getEventType();
			//一直解析直到文档结束,意味着解析完毕
			while (eventType != XmlPullParser.END_DOCUMENT) {
				//解析时机,就是标签开始并且标签名不为xml
				String tagName = xmlPullParser.getName();
				if (eventType == XmlPullParser.START_TAG && !"xml".equals(tagName)) {
					String tagContent = xmlPullParser.nextText();
					result.put(tagName, tagContent);
				}

				//解析完当前事件后,需要改变当前事件为下一个事件
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	public static void main(String[] args) {
		String content = "<xml>"
				+ "<ToUserName><![CDATA[gh_ae9b8f50b1b3]]></ToUserName>"
				+ "<FromUserName><![CDATA[oxLXms6cL0ECuVST7vQiDZdg4RbU]]></FromUserName>"
				+ "<CreateTime>1501308945</CreateTime>"
				+ "<MsgType><![CDATA[text]]></MsgType>"
				+ "<Content><![CDATA[test]]></Content>"
				+ "<MsgId>6448072820526206355</MsgId>"
				+ "</xml>";
		Map<String, String> result = parse(new StringReader(content));
		// for (String key : result.keySet()) {
		// String value = result.get(key);
		// }
		
		//遍历map最好的方式
		Set<Entry<String, String>> entrySet = result.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			System.out.println(next.getKey()+"-->"+next.getValue());
		}
		
	}
}
