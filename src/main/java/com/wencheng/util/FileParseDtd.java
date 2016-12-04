package com.wencheng.util;

import com.wencheng.bean.Attr;
import com.wencheng.bean.AttrList;
import com.wencheng.bean.ChildNode;
import com.wencheng.bean.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParseDtd{
	
	private String path;
	private Map<String,Element> map;
	private Map<String, AttrList> attrmap;
	private String first = "";

	public FileParseDtd(String path1){
		this.path = path1;
		map = parseElement();
		attrmap = parseAttr();
	}

	/**
	 * 统一入口
	 * @param father
	 * @param num
     * @return
     */
	public JSONObject getBigJson(String father,String num){
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		jo.put("name", "xml");
		ja.add(getJson(father,num));
		Iterator<Element> it = map.values().iterator();
		while(it.hasNext()){
			Element ele = it.next();
			if(!ele.isUsed()){
				ja.add(getJson(ele.getName(),""));
			}
		}
		jo.put("children", ja);
		return jo;
		
	}
	
	public JSONObject getJson(String father,String num){

		father = father.replace("+","");
		father = father.replace("*","");
		father = father.replace("?","");
		father = father.trim();

		if(father.equals("first_item_256")){
			father = first;
		}

		if(father.startsWith("#")){
			JSONObject jo = new JSONObject();
			jo.put("name",father);
			return jo;
		}

		Element ele = map.get(father);
		if(ele == null){

			JSONObject jo = new JSONObject();
			jo.put("name", "【"+father+"】");
			return jo ;
		}
		JSONObject jo = new JSONObject();
		StringBuilder elename = new StringBuilder(num+"【"+ele.getName()+"】");
		AttrList al = attrmap.get(father);
		if(al != null){
			List<Attr> list = al.getList();
			Iterator<Attr> it = list.iterator();
			while(it.hasNext()){
				Attr next = it.next();
				elename.append("\0\0\0\0\0\0\0\0\n");
				elename.append(next.getName());
				elename.append("(");
				if(next.isRequired()){
					elename.append("r");
				}else{
					elename.append("i");
				}
				elename.append(")");
			}
		}
		jo.put("name", elename.toString());
		JSONArray ja = new JSONArray();
		ChildNode[] sons = ele.getSons();
		if(sons!= null && sons.length>0){
			boolean is = true;
			for(int i = 0; i<sons.length; i++){
				if(sons[i].getName().equals(ele.getName())){
					is = false;
				}else{
					Element element = map.get(sons[i].getName());
					if(element != null){
						ChildNode[] sons2 = element.getSons();
						for(int j = 0; sons2!=null&&j<sons2.length; j++){
							if(sons2[j].getName().equals(ele.getName())){
								is = false;
							}
						}
					}
				}
			}
			for(int i = 0; i<sons.length&&is; i++){
				ja.add(getJson(sons[i].getName(),sons[i].getNum()));
			}
			if(is)
				jo.put("children", ja);
		}
		return jo;
	}

	/**
	 * 将Dtd转化成Map
	 * @return
     */
	private Map<String,Element> parseElement() {
		Map<String,Element> list = new HashMap<String,Element>();
		String rawContent = "";

		try {
			rawContent = createContent();
		}catch (Exception e) {
			e.printStackTrace();
		}

		StringBuffer content = preProcessElement(rawContent);

		Pattern regexpElemet = Pattern.compile("<!ELEMENT\\s*([\\w-]+)\\s*\\(([^\\(\\)]*)[\\+\\?\\*]?\\).*?>",Pattern.MULTILINE);
		Matcher ma = regexpElemet.matcher(content.toString());

		boolean first = true;
		while(ma.find()){

			if(first){
				this.first = ma.group(1);
				first = false;
			}
			Element e = toElement(ma);
			list.put(e.getName(),e);
		}

		Pattern regexpElement2 = Pattern.compile("<!ELEMENT\\s*([\\w-]+)\\s*(:?EMPTY|SYSTEM|ANY)(:?[\"\\w\\s]*)>",Pattern.MULTILINE);
		Matcher ma2 = regexpElement2.matcher(rawContent);
		while(ma2.find()){
			toElement2(list, ma2);
		}

		return list;
	}

	private void toElement2(Map<String, Element> list, Matcher ma2) {
		Element e = toElement2(ma2);
		list.put(e.getName(), e);
	}


	/**
	 * 将匹配结果转化成Element
	 * @param ma
	 * @return
     */
	private Element toElement(Matcher ma) {
		Element e = toElement2(ma);
		String[] songsString = ma.group(2).replace(")","").replace("(", "").split("\\||,");

		ChildNode[] sons = new ChildNode[songsString.length];
		for(int i = 0; i<songsString.length ;i++){
            songsString[i] = songsString[i].trim();
            String name = "";
            String num = "";

            ChildNode cn = new ChildNode();
            if(songsString[i].endsWith("+")||songsString[i].endsWith("*")||songsString[i].endsWith("?")){
                name = songsString[i].substring(0,songsString[i].length()-1);
                num = songsString[i].substring(songsString[i].length()-1);
            }else{
                name = songsString[i];
                num = "唯一";
            }
            cn.setName(name.trim());
            cn.setNum(num.trim());
            sons[i] = cn;
        }
		e.setSons(sons);
		return e;
	}

	private Element toElement2(Matcher ma2) {
		Element e = new Element();
		e.setName(ma2.group(1));
		return e;
	}

	private StringBuffer preProcessElement(String rawContent) {

		StringBuffer noBlankContent = removeBlankInElement(rawContent);

		Pattern rep2 = Pattern.compile("\\(([^\\<\\>]*)\\)");
		Matcher mar2 = rep2.matcher(noBlankContent.toString());
		StringBuffer con2 = new StringBuffer();
		while(mar2.find()){
			mar2.appendReplacement(con2, "("+mar2.group().replace("(", "").replace(")","")+")");
		}
		mar2.appendTail(con2);
		return con2;
	}

	private StringBuffer removeBlankInElement(String rawContent) {
		Pattern rep = Pattern.compile("\\(([^,\\(\\)]+?)\\)([\\*\\+\\?])");
		Matcher mresult = rep.matcher(rawContent.replace("\n", " ").replace("\0", ""));

		StringBuffer con = new StringBuffer();
		while(mresult.find()){

			String str = mresult.group(1);
			String[] split = str.split("\\||,");

			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for(int i = 0; i<split.length; i++){
				sb.append(split[i]+mresult.group(2));
				if(i+1<split.length){
					sb.append(",");
				}
			}
			sb.append(")");

			mresult.appendReplacement(con, sb.toString());
		}
		mresult.appendTail(con);
		return con;
	}

	/**
	 * 将属性处理成Map
	 * @return
     */
	private Map<String,AttrList> parseAttr(){
		HashMap<String, AttrList> attrmap = new HashMap<>();
		String content = "";

		try {
			 content = createContent().trim().replace("\n", " ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Pattern regexpAttList= Pattern.compile("<!ATTLIST\\s*([\\w]*)\\s*(.*?)>");
		Pattern regexpAttElement = Pattern.compile("([\\w]*)\\s*(?:CDATA|\\(.*?\\))\\s*#([\\w]*\\s*?)");

		Matcher mat = regexpAttList.matcher(content);
		while(mat.find()){
			AttrList al = null;
			List<Attr> ll = null;

			String eName = mat.group(1);
			if(attrmap.get(eName)!=null){
				al = attrmap.get(eName);
				ll = al.getList();
			}else{
				al = new AttrList();
				al.setParent(eName);
				ll = new LinkedList<Attr>();
				al.setList(ll);
			}

			String attrcontent = mat.group(2);
			Matcher matattr = regexpAttElement.matcher(attrcontent);
			while(matattr.find()){
				Attr attr = new Attr();
				attr.setName(matattr.group(1));
				if(matattr.group(2).trim().equals("REQUIRED")){
					attr.setRequired(true);
				}else{
					attr.setRequired(false);
				}
				ll.add(attr);
			}
			attrmap.put(al.getParent(), al);
		}
		return attrmap;
	}

	/**
	 * 读取文件
	 * @return
	 * @throws IOException
     */
	private  String createContent() throws IOException{

		File file = new File(path);

		if(!file.exists()){
			return "";
		}

		return FileUtils.readFileToString(file, "utf-8");
	}

}
