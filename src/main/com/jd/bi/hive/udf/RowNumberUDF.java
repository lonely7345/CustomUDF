package com.jd.bi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * ��ȡ��ǰ�еı�š�
 * ��Ź���
 * a�����ֻ��һ������
 * �Ƚϵ�ǰֵ��ǰһ��ֵ�����ֵ��ͬ����ǰ�б����ǰһ�б�ż�1�������Ų��䣻
 * b������ж������
 * ���һ�������ǱȽϵ�ֵ��ǰ��Ĳ���������У�
 * �ȱȽϷ�����У����������в�ͬ����������Ϊ1
 * ����Ƚϵ�����ͬ����(a)�еĹ�����бȽϡ�
 * @author cuiming
 *
 */
public class RowNumberUDF extends UDF {
	private static int MAX_VALUE = 50;
	private String comparedColumn[] = new String[MAX_VALUE];
	private int rowNum = 1;

	public int evaluate(Object... args) {
		if (args == null || args.length == 0) {
			return rowNum++;
		} else {
			String columnValue[] = new String[args.length];
			for (int i = 0; i < args.length; i++)
				columnValue[i] = args[i].toString();
			if (rowNum == 1) {
				for (int i = 0; i < columnValue.length; i++)
					comparedColumn[i] = columnValue[i];
			}
			for (int i = 0; i < columnValue.length; i++) {
				if (!comparedColumn[i].equals(columnValue[i])) {
					for (int j = 0; j < columnValue.length; j++) {
						comparedColumn[j] = columnValue[j];
					}
					rowNum = 1;
					return rowNum++;
				}
			}
			return rowNum++;
		}
	}
}
