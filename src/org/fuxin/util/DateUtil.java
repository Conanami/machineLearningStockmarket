package org.fuxin.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat dateFormat = null;
	/***
	 * ��ȥ�����죬����ǰ������
	 * @param day
	 * @param i
	 * @return
	 */
	public static Date Sub(Date day, long i) {
		// TODO Auto-generated method stub
		long daytime = day.getTime();
		long periodtime = i * 24 * 60 * 60 * 1000;
		long mytime = daytime - periodtime;
		return new Date(mytime);
	}
	
	//�Ƚ��������ڣ�a<b����1
	public static int CompareTo(Date a,Date b)
	{
		if(a==b)
		{
			return 0;
		}
		if(a.getTime()<b.getTime())
		{
			return 1;
		}
		else if(a.getTime()==b.getTime())
		{
			return 0;
		}
		else
			return -1;
	}
	
	/***
	 * ���϶����죬����������
	 * @param day
	 * @param l
	 * @return
	 */
	public static Date Add(Date day, long l) {
		// TODO Auto-generated method stub
		long daytime = day.getTime();
		long periodtime = l * 24 * 60 * 60 * 1000;
		long mytime = daytime + periodtime;
		return new Date(mytime);
	}
	
	public static String show(Date day)
	{
		if(day==null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
		return sdf.format(day);
	}

	/***
	 * ������������֮��ļ��
	 * @param enddate
	 * @param startdate
	 * @return
	 */
	public static long Sub(Date enddate, Date startdate) {
		long starttime = startdate.getTime();
		long endtime = enddate.getTime();
		long daytime = 24 * 60 * 60 * 1000;
		return (endtime-starttime)/daytime;
	}
	
    public static boolean isValidDate(String s)
    {
        try
        {
        	dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	dateFormat.parse(s);
            return true;
        }
        catch (Exception e)
        {
            // ���throw java.text.ParseException����NullPointerException����˵����ʽ����
            return false;
        }
    }
    
    /***
     * ȡ�����ڼ�
     * @param date
     * @return
     */
    public static int getDay(Date date) {
    	  Calendar cal = Calendar.getInstance();
    	  cal.setTime(date);
    	  return (cal.get(Calendar.DAY_OF_WEEK)-1);
    }
}
