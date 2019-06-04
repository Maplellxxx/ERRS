package t1;
import java.util.*;
public class t1 {
	
	public static void main(String[] args) {
		System.out.println("Please enter three integers:");
		int j = 0;
		int t=0;
		int[] l = {0,0,0,0,0,0,0};
		while(t!=3) {
			
			try {
				if(t==0) {
					System.out.print("a:");
				}else if(t==1){
					System.out.print("b:");
				}else {
					System.out.print("c:");
				}
				Scanner sc=new Scanner(System.in);
				j=sc.nextInt();
			}catch(Exception e) {
				System.out.println("请输入整数");
				continue;
			}
			if(j<=200&&j>0) {
				
				l[t] = j;
				t++;
			}else {
				System.out.println("请输入数字小于200");
			}
		}
		
		int a=l[0];
		int b=l[1];
		int c=l[2];
		
		
		if(a+b<=c||a+c<=b||b+c<=a) {
			System.out.println("Not a Triangle");
		}else if(a==b&&b==c&&a==c) {

			System.out.println("Isosceles");
	
		}else if(a!=b&&b!=c&&a!=c) {
			System.out.println("Scalene");
		}else {
			System.out.println("Equilateral");
		}
		
		
    }

}
