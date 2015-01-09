package com.jd.bi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import com.jd.bi.hive.util.DateUtil;

/**
 * �������ڣ���ȡ��ǰ������һ���еĵڼ��죻����Ϊ��1�죬����Ϊ��7�죻
 * ������ʽΪ����1�����������ڵ��ַ������飻��2������Ϊ���ڵĸ�ʽ��
 * ���û�е�2����������Ĭ�ϸ�ʽ��yyyy-MM-dd
 * @author cuiming
 *
 */
public class DayOfWeekUDF extends UDF {
	
	public int evaluate(Object... args) {
		if(args == null || args.length < 1)
			throw new IllegalArgumentException("������Ҫ1������");
		String date_str = args[0].toString();
		String format = null;
		if (args.length == 2)
			format = args[1].toString();
		if (format == null || format.trim().length() == 0)
			return DateUtil.getDateOfWeek(date_str, "yyyy-MM-dd");
		else
			return DateUtil.getDateOfWeek(date_str,format);
	}

}
