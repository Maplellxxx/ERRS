package t1;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class t2 {
	public String NextDay(int year,int month,int day) {
		String m="";
		String d="";
		if(month<10) {
			m="0"+String.valueOf(month);
		}else {
			m=String.valueOf(month);
		}
		
		if(day<10) {
			d="0"+String.valueOf(day);
		}else {
			d=String.valueOf(day);
		}
		
		String string = String.valueOf(year)+"-"+m+"-"+d;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = null;
		try {
            startdate = dateFormat.parse(string);
         
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		
		
		Date date = new Date();
		Calendar calendar =
		new GregorianCalendar();
		calendar.setTime(startdate); //你自己的数据进行类型转换
		calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime();
		
		

		return dateFormat.format(date);
		
		
		
		
		
	}
	
	
	public boolean IsLeapYear(int year) {
		if(year % 4== 0 && year%100!=0||year%400==0)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	
	public static void main(String[] args) {
		t2 t2 = new t2();
		int t=0;
		int i=0;
		int[] l = {0,0,0,0,0,0,0};
		while(t<3) {
		
			try {
				if(t==0) {
					System.out.print("Year:");
				}else if(t==1){
					System.out.print("Month:");
				}else {
					System.out.print("Day:");
				}
				Scanner sc=new Scanner(System.in);
				i=sc.nextInt();
			}catch(Exception e) {
				System.out.println("please enter an integer");
				continue;
			}
			
			
			
			if(t==0) {
				if(i>1811&&i<2013) {
					l[t]=i;
					t++;
				}else {
					System.out.println("The year of input should be between 1812 and 2012");
				}
			}else if(t==1) {
				if(i>0&&i<13) {
					l[t]=i;
					t++;
				}else {
					System.out.println("The month of input should be between 1 and 12");
				}
				
				
			}else if(t==2) {
				if(i>0&&i<32) {
					//判断天数是否存在
					if(l[1]==1||l[1]==3||l[1]==5||l[1]==7||l[1]==8||l[1]==10||l[1]==12) {
						l[t]=i;
						t++;
					}else if(l[t]==4||l[t]==6||l[t]==9||l[t]==11) {
						
						if(l[t]<30) {
							l[t]=i;
							t++;
						}else {
							System.out.println("There are only 30 days in this month.");
						}
						
					}else {
						if(t2.IsLeapYear(l[0])) {
							if(i<=29&&i>=1) {
								l[t]=i;
								t++;
							}else {
								System.out.println("There are only 29 days in this month.");
							}
							
						}else {
							
							if(i<=28&&i>=1) {
								l[t]=i;
								t++;
							}else {
								System.out.println("There are only 28 days in this month.");
							}
							
							
						}
						
						
					}
					
					
					
					
				}else {
					System.out.println("The day of input should be between 1 and 31");

				}
				
				
			}
			
			
			
			
			
			
		}
		
		System.out.println(l[0]);
		System.out.println(l[1]);
		System.out.println(l[2]);

		
		System.out.println(t2.NextDay(l[0], l[1], l[2]));
		
		
	}
}
