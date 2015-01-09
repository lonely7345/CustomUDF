package com.jd.bi.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * �Ӳ�ѯ���ص�ÿһ�У����������������е����λ�á�
 * ���� DISTRIBUTE BY �Ӿ��б��ʽ��ֵ���Ӳ�ѯ���ص�ÿһ�У����������������е����λ�á�
 * ���ڵ����ݰ� ORDER BY �Ӿ�����Ȼ���ÿһ�и�һ���ţ��Ӷ��γ�һ�����У� �����д� 1 ��ʼ�������ۼӡ�
 * ÿ�� ORDER BY ���ʽ��ֵ�����仯ʱ��������Ҳ��֮���ӡ�
 * ��ͬ��ֵ���еõ�ͬ����������ţ���Ϊ null ʱ��ȵģ���
 * Ȼ����������е�ȷ�õ�ͬ���������������������Ծ��
 * ����������Ϊ 1 ����û������ 2 �����н������е���һ�з���ֵ 3 �� DENSE_RANK ��û���κ���Ծ
 * 
 * �������Ź�Աнˮ����ĸ�ʽ (id int,name string,depno int,salary short/int/long/float/double/string)
 * ����idΪ��ԱID��nameΪ��Ա���ƣ�depnoΪ���ű�ţ�salaryΪ��Աнˮ
 * ʹ�÷���������Hive�иú�����Ϊrank_over����
 * a�������й�Աнˮ��������
 * select *,rank_over(salary) from 
 * 		(select id,name,depno,salary from emp_salary distribute by depno sort by depno,salary) t;
 * b�������ŷ��飬��нˮ��������
 * select *,rank_over(depno,salary) from 
 * 		(select id,name,depno,salary from emp_salary distribute by depno sort by depno,salary) t;
 * @author cuiming
 *
 */
public class RankUDF extends UDF {
	private String comparedColumn[] = null;
	private String comparedValue = new String();
	//�ܵ��������
	private int rowNum = 1;
	//��ǰ�еı��
	private int current_rowNum = 1;

	/**
	 * ���������У����һ��ֵΪ�Ƚϵ�ֵ��ǰ��Ĳ����ǰ�order by�������
	 * @param args
	 * @return
	 */
	public int evaluate(Object... args) {
		if(args == null || args.length < 1)
			throw new IllegalArgumentException("������Ҫ1������");
		String columnValue[] = new String[args.length -1];
		if (args.length > 1){
			for (int i = 1; i < args.length; i++)
				columnValue[i-1] = args[i].toString();
		}
		String value = args[0].toString();
		//��һ�У��ȸ��Ƹ��е�comparedColumn������
		if (comparedColumn == null) {
			comparedColumn = new String[columnValue.length];
			for (int i = 0; i < columnValue.length; i++)
				comparedColumn[i] = columnValue[i];
			comparedValue = value;
			return current_rowNum;
		}
		//�����ǰ�ĸ��к�֮ǰ���в�һ�£�rowNum��current_rowNum����Ϊ1
		for (int i = 0; i < columnValue.length; i++) {
			if (!comparedColumn[i].equals(columnValue[i])) {
				for (int j = 0; j < columnValue.length; j++) {
					comparedColumn[j] = columnValue[j];
				}
				rowNum = 1;
				current_rowNum = 1;
				comparedValue = value;
				return current_rowNum;
			}
		}
		
		//��һ�£���ֵ���бȽ�
		rowNum++;
		if (!comparedValue.equalsIgnoreCase(value))
			current_rowNum = rowNum;
		comparedValue = value;
		return current_rowNum;
	}
}
