package com.bt.listview.azlist.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public final class PinyinUtils {

	private static HanyuPinyinOutputFormat spellFormat = new HanyuPinyinOutputFormat();

	static {
		spellFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		spellFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		 spellFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	}

	/**
	 * 字符串转换成拼音
	 * 
	 * @param chineseStr
	 *            字符串
	 * @return
	 */
	public static String getPinYin(String chineseStr) {
		StringBuffer result = new StringBuffer();
		String[] array ;
		try {
			for (char c : chineseStr.toCharArray()) {
				if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {//c>128?
					array = PinyinHelper.toHanyuPinyinStringArray(c,spellFormat);
					if (array != null && array.length > 0)
						result.append(array[0]);
					else
						result.append("");
				} else
					result.append(c);//特殊字符或者字母
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 提取每个汉字的首字母
	 * 
	 * @param str
	 * @return String
	 * @throws BadHanyuPinyinOutputFormatCombination 
	 */
	public static String getPinYinHeadChar(String str) throws BadHanyuPinyinOutputFormatCombination {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		String convert = "";
		String[] pinyinArray = null;
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			if (String.valueOf(word).matches("[\\u4E00-\\u9FA5]+")) {
				// 提取汉字的首字母
				pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word,spellFormat);

				if (pinyinArray != null && pinyinArray.length > 0) {
					convert += pinyinArray[0].charAt(0);//转换成拼音，取首字母
				} else {
					convert += word;
				}
			}else {
				convert += word;//特殊字符
//				System.out.println("特殊字符字母："+word);
			}
		}
		return convert;
	}

	/**
	 * 将字符串转换成ASCII码
	 * 
	 * @param cnStr
	 * @return String
	 */
	public static String getCnASCII(String cnStr) {
		StringBuffer strBuf = new StringBuffer();
		// 将字符串转换成字节序列
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			// 将每个字符转换成ASCII码
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
		}
		return strBuf.toString();
	}
	
	/**
	 * 提取首字母 并排序
	 * @param beans
	 * @return
	 */
	public static ArrayList<String> getOrderChar(ArrayList<ContactBean> beans) {
		ArrayList<String> charsList = new ArrayList<String>();
		String name = "";
		for (Iterator<ContactBean> iterator = beans.iterator(); iterator.hasNext();) {
			ContactBean contactBean = (ContactBean) iterator.next();
			name = contactBean.getContactName();
			if (TextUtils.isEmpty(name) ) {
				if (!charsList.contains(name)) {
					charsList.add("");
				}
				contactBean.firstCharName = "";
			}else {
				String firstChar = "";
				try {
					firstChar = getPinYinHeadChar(name.substring(0, 1));
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
				
				char c = firstChar.charAt(0);
				if (c >= 'a' && c <= 'z') {
					if (!charsList.contains( String.valueOf((char)(c - 32)))) {
						charsList.add( String.valueOf((char)(c - 32)));
					}
					contactBean.firstCharName = String.valueOf((char)(c - 32));
				}else {
					if (!charsList.contains(firstChar)) {
						charsList.add(firstChar);
					}
					contactBean.firstCharName = firstChar;
				}
			}
			
		}
		Collections.sort(charsList);
		Collections.sort(beans, new Comparator<ContactBean>() {

			@Override
			public int compare(ContactBean lhs, ContactBean rhs) {
				return lhs.firstCharName.compareTo(rhs.firstCharName);
			}
		});
		return charsList;
	}

}
