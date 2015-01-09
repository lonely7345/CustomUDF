package com.jd.bi.hive.udf;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * �ƶ����UDF
 * @author cuiming
 *
 */
public class MoveSumUDF extends UDF {

	private String comparedColumn[] = null;
	
	private Queue<String> queue = new LinkedList<String>();;
	
	private int offset = 0;

	/**
	 * ��һ��������ֵ���ڶ���������ƫ������
	 * 1��û�з���������ڶ��������ֵ�����統ǰ���е�Ԫ�ظ�������ƫ�������򵯳�����ͷԪ��
	 * 2���з����������ȽϷ����������һ����¼�ķ����ֶ��Ƿ���ȫһ�£������һ�£��������գ�
	 * @param args
	 * @return
	 */
	public double evaluate(Object... args) {
		if (args == null || args.length < 2)
			throw new IllegalArgumentException("������Ҫ2������");
		
		String columnValue[] = new String[args.length -2];
		if (args.length > 2){
			for (int i = 2; i < args.length; i++)
				columnValue[i-2] = args[i].toString();
		}
		String value = args[0].toString();
		if (comparedColumn == null)
			comparedColumn = Arrays.copyOf(columnValue, columnValue.length);
		
		if (args.length > 1 && offset == 0)
			offset = Integer.parseInt(args[1].toString());
		
		//�����ǰ�ĸ��к�֮ǰ���в�һ��
		for (int i = 0; i < columnValue.length; i++) {
			if (!comparedColumn[i].equals(columnValue[i])) {
				for (int j = 0; j < columnValue.length; j++) {
					comparedColumn[j] = columnValue[j];
				}
				queue.clear();
			}
		}
		queue.offer(value);
		if (queue.size() > offset)
			queue.poll();
		
		return sum(queue);
	}
	private double sum(Queue<String> queue){
		if (queue == null || queue.isEmpty())
			return 0.0;
		BigDecimal sum = new BigDecimal(0);
		BigDecimal decimal = null;
		Iterator<String> iter = queue.iterator();
		while(iter.hasNext()){
			decimal = new BigDecimal(iter.next());
			sum = sum.add(decimal);
		}
		return sum.doubleValue();
	}
}
