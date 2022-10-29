package org.fuxin.util;

import java.util.ArrayList;

/***
 * ������������ʱ�����ϣ�
 * @author Administrator
 *
 */
public class TimePeriodMaker {

	/***
	 * ����3��ʱ��ε����
	 * @return
	 */
	public static ArrayList<ArrayList<Integer>> make3Combination() {
		ArrayList<ArrayList<Integer>> rttp = new ArrayList<ArrayList<Integer>>();
		for(int i1=10;i1<=30;i1=i1+10)
			for(int i2=40;i2<=100;i2=i2+20)
				for(int i3=40;i3<=120;i3=i3+20)
				{
					ArrayList<Integer> single = new ArrayList<Integer>();
					single.add(i1);
					single.add(i2);
					single.add(i3);
					rttp.add(single);
				}
		return rttp;
		
	}

	/***
	 * �����ĸ�ʱ��ε����
	 * @return
	 */
	public static ArrayList<ArrayList<Integer>> make4Combination() {
		ArrayList<ArrayList<Integer>> rttp = new ArrayList<ArrayList<Integer>>();
		for(int i0=2;i0<=3;++i0)
			for(int i1=10;i1<=30;i1=i1+10)
				for(int i2=60;i2<=100;i2=i2+20)
					for(int i3=60;i3<=120;i3=i3+20)
					{
						ArrayList<Integer> single = new ArrayList<Integer>();
						single.add(i0);
						single.add(i1);
						single.add(i2);
						single.add(i3);
						rttp.add(single);
					}
		return rttp;
		
	}

}
