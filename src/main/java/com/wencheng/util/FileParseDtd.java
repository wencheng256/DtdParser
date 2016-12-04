package com.wencheng.util;

import com.wencheng.bean.Attr;
import com.wencheng.bean.AttrList;
import com.wencheng.bean.ChildNode;
import com.wencheng.bean.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
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
		//System.out.println(father);
		Element ele = map.get(father);
		if(ele == null){
			//System.out.println(father);
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
	
	private Map<String,Element> parseElement() {
		Map<String,Element> list = new HashMap<String,Element>();
		String content = "";
		try {
			content = createContent();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		Pattern rep = Pattern.compile("\\(([^,\\(\\)]+?)\\)([\\*\\+\\?])");
		Matcher mar = rep.matcher(content.replace("\n", " ").replace("\0", ""));
		StringBuffer con = new StringBuffer();
		while(mar.find()){
			String str = mar.group(1);
			String[] split = str.split("\\||,");
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for(int i = 0; i<split.length; i++){
				sb.append(split[i]+mar.group(2));
				if(i+1<split.length){
					sb.append(",");
				}
			}
			sb.append(")");
			mar.appendReplacement(con, sb.toString());
		}
		mar.appendTail(con);
		//System.out.println(con.toString());
		
		Pattern rep2 = Pattern.compile("\\(([^\\<\\>]*)\\)");
		Matcher mar2 = rep2.matcher(con.toString());
		StringBuffer con2 = new StringBuffer();
		while(mar2.find()){
			mar2.appendReplacement(con2, "("+mar2.group().replace("(", "").replace(")","")+")");
		}
		mar2.appendTail(con2);
		//System.out.println(con2.toString());
		
		
		
		
		Pattern p = Pattern.compile("<!ELEMENT\\s*([\\w-]+)\\s*\\(([^\\(\\)]*)[\\+\\?\\*]?\\).*?>",Pattern.MULTILINE);
		Matcher ma = p.matcher(con2.toString());
		//System.out.println(content.replace("\n", " ").replace("\0", ""));
		boolean first = true;
		while(ma.find()){
			if(first){
				this.first = ma.group(1);
				first = false;
			}
			//System.out.println(ma.group(1));
			Element e = new Element();
			e.setName(ma.group(1));
			//System.out.println(ma.group(2));
			String[] sons = ma.group(2).replace(")","").replace("(", "").split("\\||,");
			ChildNode[] so = new ChildNode[sons.length];
			for(int i = 0; i<sons.length ;i++){
				sons[i] = sons[i].trim();
				//System.out.println(sons[i]);
				String name = "";
				String num = "";
				ChildNode cn = new ChildNode();
				if(sons[i].endsWith("+")||sons[i].endsWith("*")||sons[i].endsWith("?")){
					name = sons[i].substring(0,sons[i].length()-1);
					num = sons[i].substring(sons[i].length()-1);
				}else{
					name = sons[i];
					num = "唯一";
				}
				cn.setName(name.trim());
				cn.setNum(num.trim());
				so[i] = cn;
			}
			e.setSons(so);
			//System.out.println(e);
			list.put(e.getName(),e);
		}
		Pattern p2 = Pattern.compile("<!ELEMENT\\s*([\\w-]+)\\s*(:?EMPTY|SYSTEM|ANY)(:?[\"\\w\\s]*)>",Pattern.MULTILINE);
		Matcher ma2 = p2.matcher(content);
		while(ma2.find()){
			Element e = new Element();
			e.setName(ma2.group(1));
			list.put(e.getName(), e);
		}
		
		
		return list;
	}
	
	private Map<String,AttrList> parseAttr(){
		HashMap<String, AttrList> attrmap = new HashMap<String,AttrList>();
		String content = "";
		try {
			 content = createContent().trim().replace("\n", " ");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pattern pat = Pattern.compile("<!ATTLIST\\s*([\\w]*)\\s*(.*?)>");
		Pattern patattr = Pattern.compile("([\\w]*)\\s*(?:CDATA|\\(.*?\\))\\s*#([\\w]*\\s*?)");
		Matcher mat = pat.matcher(content);
		while(mat.find()){
			AttrList al = null;
			List<Attr> ll = null;
			if(attrmap.get(mat.group(1))!=null){
				al = attrmap.get(mat.group(1));
				ll = al.getList();
			}else{
				al = new AttrList();
				al.setParent(mat.group(1));
				ll = new LinkedList<Attr>();
				al.setList(ll);
			}
			String attrcontent = mat.group(2);
			Matcher matattr = patattr.matcher(attrcontent);
			while(matattr.find()){
				//System.out.println(matattr.group());
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

	public  String createContent() throws FileNotFoundException,
			IOException, UnsupportedEncodingException {
		File f = new File(path);
		if(!f.exists()){
			return "";
		}
		InputStream in = new FileInputStream(f);
		StringBuilder sb = new StringBuilder();
		byte[] b = new byte[1024];
		int len = 0;
		while((len = in.read(b))>0){
			sb.append(new String(b,0,len));
		}
		
		in.close();
		return sb.toString();
	}

}
