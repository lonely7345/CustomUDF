package com.jd.bi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * �����ַ����е��Ӵ���֧��������Һͷ������
 * @author cuiming
 *
 */
public class IndexStringUDF extends UDF {
	
	public int evaluate(String str,String str_search,int offset_search,int count){
		
		if (offset_search > 0)
			return indexString(str,str_search,offset_search,count);
		else if (offset_search < 0)
			return reverseIndexString(str,str_search,count);
		return -1;
		
	}
	/**
	 * �������
	 * @param string Դ�ַ���
	 * @param str_search �Ӵ�
	 * @param offset_search ���ҵ���ʼλ�ã���1λ�ַ�Ϊ1����2λ�ַ�Ϊ2���Դ�����
	 * @param count �����ִ��Ĵ���
	 * @return ���ز��ҵ����Ӵ���λ�� û���ҵ�����0
	 */
	private int indexString(String string,String str_search,int offset_search,int count){
		int offset = offset_search -1;
		int result = 0;
		for(int i = count; i > 0; i --){
			result = string.indexOf(str_search,offset);
			if (result == -1)
				return 0;
			offset = result + str_search.length();
			if (i == 1)
				return result + 1;
		}
		return 0;
		
	}
	/**
	 * �������
	 * @param string Դ�ַ���
	 * @param str_search �Ӵ�
	 * @param count �����ִ��Ĵ���
	 * @return ���ز��ҵ����Ӵ���λ�� û���ҵ�����0
	 */
	private int reverseIndexString(String string,String str_search,int count){
		
		int result = 0;
		for(int i = count; i > 0; i --){
			result = string.lastIndexOf(str_search);
			if (result == -1)
				return 0;
			string = string.substring(0,result);
			if (i == 1)
				return result + 1;
		}
		return 0;
		
	}
	public int evaluate(String str,String str_search,int offset){
		return evaluate(str,str_search,offset,1);
	}

	public int evaluate(String str,String str_search){
		return evaluate(str,str_search,1,1);
	}
}
