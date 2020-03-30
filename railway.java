import java.util.*;  
import java.sql.*;
import java.io.*;
import java.text.*;
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
 /////// User//////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////

class user
{
	admin ad=new admin();
	railway r=new railway();
	String tname,bp,dp;
	int tnum,seat,catnum,ch;
	String[] pname=new String[1000];
	int[] page=new int[1000];
	String[] pgen=new String[1000];
	
	Scanner a=new Scanner(System.in);
	
	int check1(int tnum) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");
		Statement s=c.createStatement();
		ResultSet r=s.executeQuery("select * from train where tnum='"+tnum+"' ");
		if(r.first())	
		return 1;
		else
		return 0;
	}
			
	void inputreserve() throws Exception
	{
		System.out.print("Enter train number : ");
		tnum=a.nextInt();
		if(check1(tnum)==0)
		{
			System.out.println("Train number doesn't exist");
			r.user_mode();
		}
		
		System.out.print("Enter boarding : ");
		bp=a.next();
		
		System.out.print("Enter destination : ");
		dp=a.next();
		
		System.out.print("Number of seats required : ");
		seat=a.nextInt();
		java.sql.Date dt2=null;
		try
		{
			System.out.print("Enter date of train's journey in (yyyy-mm-dd) format : ");
			String dt=a.next();
			java.util.Date dt1=new SimpleDateFormat("yyyy-MM-dd").parse(dt);
			dt2=new java.sql.Date(dt1.getTime());
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
			
		int j=0;
		int k=0;
		for(int i=0;i<seat;i++)
		{			
			System.out.print("Enter "+(i+1)+" passenger's name : ");
			pname[i]=a.next();
						
			System.out.print("Enter "+(i+1)+" passenger's age : ");
			page[i]=a.nextInt();
			if((page[i]>0 &&page[i]<=12)||(page[i]>=60&&page[i]<120))
			k++;
			if(page[i]<0||page[i]>120)
			{
				j=1;
				System.out.println("Enter a valid age");
			}
			System.out.print("Enter "+(i+1)+" passenger's gender : ");
			pgen[i]=a.next();
		}
		if(j==1)
		return;
		
		System.out.println("Enter the class : ");
		System.out.println("1 - First AC");
		System.out.println("2 - Second AC");
		System.out.println("3 - Third AC");
		System.out.println("4 - Sleeper coach");
		ch=a.nextInt();
		if((ch!=1)&&(ch!=2)&&(ch!=3)&&(ch!=4))
		System.out.println("Choose from above options only");
		
		else		
		{
			String coach;
			if(ch==1)  coach="First AC";
			else if(ch==2)  coach="Second AC";
			else if(ch==3)  coach="Third AC";
			else  coach="Sleeper Coach";
			
			System.out.println("Enter the concession category : ");
			System.out.println("1. Military Personnel");
			System.out.println("2. None");
			catnum=a.nextInt();
			if(catnum!=2&&catnum!=1)
			{
				System.out.println("Choose from above options only");
				r.user_mode();
			}				
			System.out.print("Confirm there is no turning back!!(y/n) ");
			String conf=a.next();
			
			if(conf=="n")
			{
				System.out.println("Your ticket is not booked");
				r.user_mode();
			}
			int fare=reserve(tnum,tname,bp,dp,seat,ch);
			if(fare==0)
			{
				System.out.println("Train number doesn't exist");
				r.user_mode();
			}
			if(catnum==1)
			{				
				System.out.println("Enter how many millitary personnel are traveling and make sure to carry your ID ");
				int mil=a.nextInt();
				System.out.println("Amount to be paid is "+(fare-(((mil*(fare/seat))*0.35)+((k*(fare/seat))*0.5))));					
			}
			else if(catnum==2)
			System.out.println("Amount to be paid is "+(fare-((k*(fare/seat))*0.5)));			
			chart(pname,page,coach,tnum,dt2);					
		}
	}
	int reserve(int tnum,String tname,String bp,String dp,int seat,int ch) 
	{ 
		int flag=0;
		int fare=0;
		try
		{
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");
			Statement s=c.createStatement();
			ResultSet r=s.executeQuery("select * from train");
			while(r.next())
			{
				if(tnum==r.getInt(1))	
				{			
					flag=1;	
					if(ch==1)
					fare=seat*r.getInt(6);
					else if(ch==2)
					fare=seat*r.getInt(7);
					else if(ch==3)	
					fare=seat*r.getInt(8);
					else			
					fare=seat*r.getInt(9);
					break;
				}
			}					
			if(flag!=0)
			{
				PreparedStatement st=c.prepareStatement("update train set seats=seats-'"+seat+"' where tnum='"+tnum+"' ");
				st.execute();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);	
		}
		if(flag==0)
		return 0;
		else
		return fare;
	}
	void tckt1() throws Exception
	{
		tckt();
		System.out.print("Do you want to continue or return to main menu (y/n) respectively  : ");
		String ch=a.next();
		if(ch.equals("y"))
		{
			r.user_mode();
		}
		else
		{
			r.main_menu();
		}
	}
	
	void tckt() 
	{
		try
		{	
			
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");
			Statement s1=c.createStatement();
			ResultSet r1=s1.executeQuery("select * from chart order by sno desc limit 1");
			r1.first();		
			Statement s2=c.createStatement();
			ResultSet r2=s2.executeQuery("select * from chart where pnr='"+r1.getLong(1)+"' ");
			r2.first();
			java.util.Date d=new java.util.Date();
			d.setTime(r2.getTimestamp(8).getTime());
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String str7 = df.format(d);		
			
			DateFormat dF = new SimpleDateFormat("dd/MM/yyyy");
			String str8 = dF.format(r2.getDate(9));
			System.out.println("***************************************************************************************");
			System.out.println("PNR Number : "+r2.getLong(1)+"                        "+"Coach : "+r2.getString(6));
			System.out.println("Name : "+r2.getString(2)+"             "+"Age : "+r2.getInt(3)+"     "+"Gender : "+r2.getString(4));
			System.out.println("Status : "+r2.getString(7)+"                           "+"Seat Number : "+r2.getInt(5));	
			while(r2.next())
			{
				System.out.println("Name : "+r2.getString(2)+"             "+"Age : "+r2.getInt(3)+"     "+"Gender : "+r2.getString(4));
				System.out.println("Status : "+r2.getString(7)+"                           "+"Seat Number : "+r2.getInt(5));	
			
			}
			System.out.println("Date of Travelling : "+str8+"                    "+"Booked on : "+str7);	
			System.out.println("***************************************************************************************");
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	void chart(String pname[],int page[],String coach,int tnum,java.sql.Date dt2)
	{
		try
		{
			
			java.util.Date date=new java.util.Date();
			java.sql.Timestamp sqt=new java.sql.Timestamp(date.getTime());
			Class.forName("com.mysql.jdbc.Driver");
			Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");
			Statement s2=c.createStatement();
			Statement s1=c.createStatement();
			Statement s3=c.createStatement();
			ResultSet r3=s3.executeQuery("select * from chart order by sno desc limit 1");
			r3.first();
			for(int i=0;i<seat;i++)
			{	
			ResultSet r2=s2.executeQuery("select * from chart order by sno desc limit 1");
			r2.first();
			ResultSet r1=s1.executeQuery("select * from train where tnum='"+tnum+"' and doj='"+dt2+"' ");
			r1.first();
			PreparedStatement st=c.prepareStatement("insert into chart (pnr,name,age,gender,seatno,coach,status,timestamp,dot,tnum) values(?,?,?,?,?,?,?,?,?,?)");
				st.setLong(1,r3.getLong(1)+1);
				st.setString(2,pname[i]);
				st.setInt(3,page[i]);
				st.setString(4,pgen[i]);				
				if(r1.getInt(3)>0)			st.setInt(5,r2.getInt(5)+1);
				else						st.setInt(5,0);
				st.setString(6,coach);
				if(r1.getInt(3)>0)			st.setString(7,"confirmed");
				else						st.setString(7,"waiting");
				st.setTimestamp(8,sqt);
				st.setDate(9,r1.getDate(10));
				st.setInt(10,tnum);				
				st.executeUpdate();		
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		System.out.println("Congrats!!!! Your ticket is booked. Have a nice day!!");
		try
		{
			tckt1();
		}
		catch(Exception e)	
		{
			System.out.println(e);
		}	
	}
	void cancel1() throws Exception
	{
		cancel();
		System.out.print("Do you want to continue or return to main menu (y/n) respectively  : ");
		String ch=a.next();
		if(ch.equals("y"))
		{
			r.user_mode();
		}
		else
		{
			r.main_menu();
		}
	}
		
	
	void cancel() throws Exception
	{
		long pnr;
		String j="cancel";
		Class.forName("com.mysql.jdbc.Driver");
		Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");	
		System.out.print("Enter PNR Number  ");
		pnr=a.nextLong();
		Statement stmt=c.createStatement();
		ResultSet r=stmt.executeQuery("select * from chart where pnr='"+pnr+"' ");
		if(r.first())
		{
			PreparedStatement st=c.prepareStatement("update chart set status='"+j+"' where pnr='"+pnr+"' ");
			st.executeUpdate();
		}
		else
		{	
			System.out.println("PNR number does not exist ");
		}
		
	}	
	void setw(int tnum, String str1, int seats,String str10,String str11, int fAc,int sAc,int tAc,int sc,java.sql.Date doj, String str7,String str9, int width)
	{
		String str=Integer.toString(tnum);
		System.out.print(str);
		for (int x = str.length(); x < width; ++x) 
		System.out.print(' ');	
		System.out.print(str1);		
		for (int x = str1.length(); x < width; ++x) 
		System.out.print(' ');		
		String str8=Integer.toString(seats);
		System.out.print(str8);		
		for (int x = str8.length(); x < width; ++x) 
		System.out.print(' ');		
		System.out.print(str10);		
		for (int x = str10.length(); x < width; ++x) 
		System.out.print(' ');
		System.out.print(str11);		
		for (int x = str11.length(); x < width; ++x) 
		System.out.print(' ');		
		String str2=Integer.toString(fAc);
		System.out.print(str2);
		for (int x = str2.length(); x < width; ++x) 
		System.out.print(' ');	
		String str3=Integer.toString(sAc);
		System.out.print(str3);
		for (int x = str3.length(); x < width; ++x) 
		System.out.print(' ');	
		String str4=Integer.toString(tAc);
		System.out.print(str4);
		for (int x = str4.length(); x < width; ++x) 
		System.out.print(' ');	
		String str5=Integer.toString(sc);
		System.out.print(str5);
		for (int x = str5.length(); x < width; ++x) 
		System.out.print(' ');
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String str6 = df.format(doj);
		System.out.print(str6);
		for (int x = str6.length(); x < width; ++x) 
		System.out.print(' ');
		System.out.print(str7);
		for (int x = str7.length(); x < width; ++x) 
		System.out.print(' ');
		System.out.println(str9);
	}
	void enquiry1() throws Exception
	{
		enquiry();
		System.out.print("Do you want to continue or return to main menu (y/n) respectively  : ");
		String ch=a.next();
		if(ch.equals("y"))
		{
			r.user_mode();
		}
		else
		{
			r.main_menu();
		}
	}
		
	void enquiry() throws Exception
	{
		System.out.print("From ");
		String from=a.next();	
		System.out.print("To ");
		String to=a.next();	
		Class.forName("com.mysql.jdbc.Driver");
		Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");	
		System.out.println("***************************************************************************************************************************************************************************************");
		System.out.println("Train Number   Train Name     Seats          Boarding       Destination    First AC       Second AC      Third AC       Sleeper Coach  Journey date   Departure      Arrival");
		System.out.println("***************************************************************************************************************************************************************************************");

		Statement st=c.createStatement();
		ResultSet r=st.executeQuery("select * from train where bp='"+from+"' and dp='"+to+"' and doj>=CURDATE()");
		while(r.next())
		{
			//tnum*****tname****seats******bp*******dp********fAC****sAC*****tAC******sc***doj*******dtime***atime*****sno
			setw(r.getInt(1),r.getString(2),r.getInt(3),r.getString(4),r.getString(5),r.getInt(6),r.getInt(7),r.getInt(8),r.getInt(9),r.getDate(10),r.getString(11),r.getString(12),15);
			
			
		}
	}
	

}




  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
 /////// Admin//////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////

class admin
{
	railway r=new railway();
	Scanner i=new Scanner(System.in);
	void train_info()
	{
		int num,seats;
		String name,bp,dp;
		int[] f=new int[4];
		
		
		System.out.print("Enter train number : ");
		num=i.nextInt();
		if(num<9999&&num>99999)
		{
			System.out.println("Enter correct train number!!!");
		}
		else
		{	
			System.out.print("Enter Train name for Train number "+num+" : ");
			name=i.next();
			java.sql.Date date=	null;
			System.out.print("Enter date of train's journey in (yyyy-mm-dd) format : ");
			String dt=i.next();
			try
			{
				java.util.Date dt1=new SimpleDateFormat("yyyy-MM-dd").parse(dt);
				date=new java.sql.Date(dt1.getTime());
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		
			
			System.out.print("Enter Boarding Point : "); 
			bp=i.next();
			System.out.print("Destination Point : ");
			dp=i.next();
			
			System.out.print("Enter Departure time of train's journey in (hh:mm:ss) format : "); 
			String dtime=i.next();
			
			System.out.print("Enter arrival time of train's journey in (hh:mm:ss) format : "); 
			String atime=i.next();
			
			
			System.out.print("Enter the total number of seats in the train : ");
			seats=i.nextInt();
		
			
		
			
		
		
			System.out.println("Enter price for each ticket type : ");
			System.out.print("First AC : "); 
			f[0]=i.nextInt();
			System.out.print("Second AC : "); 
			f[1]=i.nextInt();
			System.out.print("Third AC : "); 
			f[2]=i.nextInt();
			System.out.print("Sleeper Class : "); 
			f[3]=i.nextInt();
			
			
			train_db(num,name,seats,bp,dp,f[0],f[1],f[2],f[3],date,dtime,atime);
		}
	}
	
	void user_signup() throws Exception
	{

		String uname;
		String password;
		String gen;
		int age;
		System.out.print("Enter Username : ");
		uname=i.next();
		Class.forName("com.mysql.jdbc.Driver");
		Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");
		Statement s=c.createStatement();
		ResultSet rl=s.executeQuery("select uname from user where uname='"+uname+"' ");
		if(!rl.first())		
		{
			System.out.print("Please Enter Your Password : ");
			password=i.next();
			System.out.print("Please Enter Your Age : ");
			age=i.nextInt();
			System.out.print("Enter Gender(M/F) : ");
			gen=i.next();
			user_db(uname,password,age,gen);
		}
		else
		System.out.println("Username alredy exist");
		System.out.print("Do you want to continue or return to main menu (y/n) respectively  : ");
		String ch=i.next();
		if(ch.equals("y"))
		{
			r.user_mode();
		}
		else
		{
			r.main_menu();
		}
	}
	
	int user_login() throws Exception
	{
		
		String uname;
		String password;
		int flag=0;
		System.out.print("Enter Username : ");
		uname=i.next();
		System.out.print("Please Enter Your Password : ");
		password=i.next();
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");		
		Statement s=c.createStatement();
		ResultSet r=s.executeQuery("select uname,pass from user");
		r.first();
		if(uname.equals(r.getString(1))&&password.equals(r.getString(2)))
		{
			return 1;
		}
		else
		{
			while(r.next())
			{
				if(uname.equals(r.getString(1))&&password.equals(r.getString(2)))
				return 1;
				
			}
		}
		return 0;
	}
		
	void train_db(int tnum,String tname,int seats,String bp,String dp,int i,int j,int k,int l,java.sql.Date date,String dtime,String atime)
	{
		
		
		
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");
			PreparedStatement st=c.prepareStatement("insert into train (tnum,tname,seats,bp,dp,fAC,sAC,tAC,sc,doj,dtime,atime) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			st.setInt(1,tnum);
			st.setString(2,tname);
			st.setInt(3,seats);
			st.setString(4,bp);
			st.setString(5,dp);	
			st.setInt(6,i);
			st.setInt(7,j);
			st.setInt(8,k);
			st.setInt(9,l);
			st.setDate(10,date);
			st.setString(11,dtime);
			st.setString(12,atime);			
			st.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	void user_db(String uname,String pass,int age,String g)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");
			java.util.Date date=new java.util.Date();
			java.sql.Timestamp sqt=new java.sql.Timestamp(date.getTime());
			PreparedStatement st=c.prepareStatement("insert into user(uname,pass,age,g,timestamp) values(?,?,?,?,?)");
			st.setString(1,uname);
			st.setString(2,pass);
			st.setInt(3,age);
			st.setString(4,g);
			st.setTimestamp(5,sqt);				
			st.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	void cr_train_info() throws Exception
	{	
		String ch="y";
		while(ch=="y")
		{
			train_info();
			System.out.print("Do you want to continue (y/n) : ");
			ch=i.next();
		}
		r.admin_mode();
	}
	
	
	void train_update_date() throws Exception
	{
		try
		{
			int tnum,d;
			Class.forName("com.mysql.jdbc.Driver");
			Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");
			System.out.print("Enter train Number whose date you want to update : ");
			tnum=i.nextInt();
			System.out.print("Enter number of days to be incremented : ");
			d=i.nextInt();	
			PreparedStatement st=c.prepareStatement("update train set doj = DATE_ADD(doj, INTERVAL '"+d+"' DAY) where tnum='"+tnum+"' ");
			st.execute();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		System.out.print("Do you want to continue or return to main menu (y/n) respectively  : ");
		String ch=i.next();
		if(ch.equals("y"))
		{
			r.admin_mode();
		}
		else
		{
				r.main_menu();
		}
		
	}			
	
	void setwR(int tnum, String str1, int seats,String str10,String str11, int fAc,int sAc,int tAc,int sc,java.util.Date doj, String str7,String str9, int width)
	{
		String str=Integer.toString(tnum);
		System.out.print(str);
		for (int x = str.length(); x < width; ++x) 
		System.out.print(' ');	
		System.out.print(str1);		
		for (int x = str1.length(); x < width; ++x) 
		System.out.print(' ');		
		String str8=Integer.toString(seats);
		System.out.print(str8);		
		for (int x = str8.length(); x < width; ++x) 
		System.out.print(' ');		
		System.out.print(str10);		
		for (int x = str10.length(); x < width; ++x) 
		System.out.print(' ');
		System.out.print(str11);		
		for (int x = str11.length(); x < width; ++x) 
		System.out.print(' ');		
		String str2=Integer.toString(fAc);
		System.out.print(str2);
		for (int x = str2.length(); x < width; ++x) 
		System.out.print(' ');	
		String str3=Integer.toString(sAc);
		System.out.print(str3);
		for (int x = str3.length(); x < width; ++x) 
		System.out.print(' ');	
		String str4=Integer.toString(tAc);
		System.out.print(str4);
		for (int x = str4.length(); x < width; ++x) 
		System.out.print(' ');	
		String str5=Integer.toString(sc);
		System.out.print(str5);
		for (int x = str5.length(); x < width; ++x) 
		System.out.print(' ');
		
		
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String str6 = df.format(doj);
		System.out.print(str6);
		for (int x = str6.length(); x < width; ++x) 
		System.out.print(' ');
		System.out.print(str7);
		for (int x = str7.length(); x < width; ++x) 
		System.out.print(' ');
		System.out.println(str9);
	}
	void dis_train_db()
	{
		disp_train_db();
		try
		{
			System.out.print("Do you want to continue or return to main menu (y/n) respectively  : ");
			String ch=i.next();
			if(ch.equals("y"))
			{
				r.admin_mode();
			}
			else
			{
				r.main_menu();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
		
	//tnum*****tname****seats******bp*******dp********fAC****sAC*****tAC******sc***doj*******dtime***atime*****sno
	void disp_train_db()
	{
		String ch;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");	
					System.out.println("***************************************************************************************************************************************************************************************");
			System.out.println("Train Number   Train Name     Seats          Boarding       Destination    First AC       Second AC      Third AC       Sleeper Coach  Journey date   Departure      Arrival");
			System.out.println("***************************************************************************************************************************************************************************************");
			Statement st=c.createStatement();
			ResultSet r=st.executeQuery("select * from train");
			while(r.next())
			{
				setwR(r.getInt(1),r.getString(2),r.getInt(3),r.getString(4),r.getString(5),r.getInt(6),r.getInt(7),r.getInt(8),r.getInt(9),r.getDate(10),r.getString(11),r.getString(12),15);
			}
		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
	void setwR(long pnr, String str1,int age,String str3, int seats,String str5,String str6,Timestamp tm,java.util.Date dot, int width)
	{
		String str=Long.toString(pnr);
		System.out.print(str);
		for (int x = str.length(); x < width; ++x) 
		System.out.print(' ');	
		System.out.print(str1);		
		for (int x = str1.length(); x < width; ++x) 
		System.out.print(' ');	
		String str2=Integer.toString(age);
		System.out.print(str2);
		for (int x = str2.length(); x < width; ++x) 
		System.out.print(' ');	
		System.out.print(str3);
		for (int x = str3.length(); x < width; ++x) 
		System.out.print(' ');	
		String str4=Integer.toString(seats);
		System.out.print(str4);		
		for (int x = str4.length(); x < width; ++x) 
		System.out.print(' ');		
		System.out.print(str5);
		for (int x = str5.length(); x < width; ++x) 
		System.out.print(' ');		
		System.out.print(str6);
		for (int x = str6.length(); x < width; ++x) 
		System.out.print(' ');
		
		java.util.Date d=new java.util.Date();
		d.setTime(tm.getTime());
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String str7 = df.format(d);
		
		
		System.out.print(str7);
		for (int x = str7.length(); x < width; ++x) 
		System.out.print(' ');		
		
		DateFormat dF = new SimpleDateFormat("dd/MM/yyyy");
		String str8 = dF.format(dot);
		System.out.println(str8);
	}


	void disp_chart() throws Exception
	{
		try
		{
			System.out.print("Enter the train number ");
			int tn=i.nextInt();	
			Class.forName("com.mysql.jdbc.Driver");
			Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");	
			System.out.println("***************************************************************************************************************************************************************************************");
			System.out.println("PNR Number          Name                Age                 Gender              Seat Number         Coach               Status              Booking time        Date of travelling");
			System.out.println("***************************************************************************************************************************************************************************************");
			Statement st=c.createStatement();
			ResultSet r=st.executeQuery("select * from chart where pnr>7999999999 and tnum='"+tn+"' ");
			while(r.next())
			{
				setwR(r.getLong(1),r.getString(2),r.getInt(3),r.getString(4),r.getInt(5),r.getString(6),r.getString(7),r.getTimestamp(8),r.getDate(9),20);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		System.out.print("Do you want to continue or return to main menu (y/n) respectively  : ");
		String ch=i.next();
		if(ch.equals("y"))
		{
			r.admin_mode();
		}
		else
		{
				r.main_menu();
		}
		
		
	}	
	void setwR(String str,int age,String str3,Timestamp tm,int width)
	{
		System.out.print(str);
		for (int x = str.length(); x < width; ++x) 
		System.out.print(' ');		
		String str2=Integer.toString(age);
		System.out.print(str2);
		for (int x = str2.length(); x < width; ++x) 
		System.out.print(' ');	
		System.out.print(str3);
		for (int x = str3.length(); x < width; ++x) 
		System.out.print(' ');	
		java.util.Date d=new java.util.Date();
		d.setTime(tm.getTime());
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String str7 = df.format(d);
		System.out.println(str7);

	}

	void disp_user() throws Exception
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/railway?autoReconnect=true&useSSL=false","root","1234");	
			System.out.println("***************************************************************************************************************************************************************************************");
			System.out.println("Username                 Age                      Gender                   Booking time");
			System.out.println("***************************************************************************************************************************************************************************************");		
			Statement st=c.createStatement();
			ResultSet r=st.executeQuery("select * from user");
			while(r.next())
			{
				setwR(r.getString(1),r.getInt(3),r.getString(4),r.getTimestamp(5),25);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		System.out.print("Do you want to continue or return to main menu (y/n) respectively  : ");
		String ch=i.next();
		if(ch.equals("y"))
		{
			r.admin_mode();
		}
		else
		{
				r.main_menu();
		}
		
	}	
	
}

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
 //    MAIN      /////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
class railway
{
	Scanner r=new Scanner(System.in);
	void main_menu() throws Exception
	{
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~MAIN MENU~~~~~~~~~~~~~~~~~~~~~~~~~\n1. Admin Mode\n2. User Mode\n3. Exit");
		int ch=r.nextInt();
		switch(ch)
		{
			case 1: admin_log();
				break;
			case 2: u_user_login();
				break;
			default:System.out.println("Made By Ayush Rai & Anshuman Mishra");
				   System.exit(0);
				break;
		
		}
			
	}
	void admin_log() throws Exception 
	{
		System.out.print("Enter password : ");
		String ps=r.next();
		if(ps.equals("toluene"))
		{
			admin_mode();
		}
		else
		{
			System.out.println("Wrong password contact to the creator!!!!!!");
			main_menu();
		}
	}
	
		
	void admin_mode() throws Exception
	{
		
		admin ad=new admin();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~ADMINISTRATOR MENU~~~~~~~~~~~~~~~~~~~~~~~~~\n1. Create detail database of trains\n2. Add Details of trains\n3. Display all the database of trains\n4. Display Chart of a train\n5. Display all users\n6. Update train date\n7. Return to main menu \n8. Exit");
		int ch=r.nextInt();
		switch(ch)
		{
			case 1:ad.cr_train_info();
				break;
			case 2: ad.train_info();
				break;
			case 3: ad.dis_train_db();
				break;
			case 4: ad.disp_chart();
				break;
			case 5: ad.disp_user();
				break;
			case 6:ad.train_update_date();
				break;		
			case 7:main_menu();
				break;	
			default:System.out.println("Made By Ayush Rai & Anshuman Mishra");
					System.exit(0);
				break;
		}
		
	}
	
	void u_user_login() throws Exception
	{
		admin ad=new admin();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~WELCOME TO USER MENU~~~~~~~~~~~~~~~~~~~~~~~~~\n1. Login\n2. Sign Up\n3. Return to main menu\n4. Exit");
		int ch=r.nextInt();
		switch(ch)
		{
			case 1:user_mode();
				break;
			case 2:ad.user_signup();
				break;
			case 3:main_menu();
				break;
			default:System.out.println("Made By Ayush Rai & Anshuman Mishra");
					System.exit(0);
				break;
		}
	}
	
	void user_mode() throws Exception
	{
		user us=new user();
		admin ad=new admin();
		if(ad.user_login()==1)
		{
			System.out.println("1. Book a ticket\n2. Cancel a ticket\n3. Enquiry\n4. Return to main menu\n5. Exit");
			int ch=r.nextInt();
			switch(ch)
			{
				case 1: us.inputreserve();
					break;
				case 2: us.cancel1();
					break;
				case 3: us.enquiry1();
					break;
				case 4: main_menu();
					break;
				default:System.out.println("Made By Ayush Rai & Anshuman Mishra");
					 System.exit(0);
					break;
			}
		}
		else
		{
			System.out.println("Wrong username or password!!!!!!");
			u_user_login();
		
		}
	}
		
	public static void main(String args[]) throws Exception
	{
	System.out.println("**************************************************************************************************************");
	System.out.println("                                      RAILWAY RESERVATION SYSTEM                                              ");
	System.out.println("**************************************************************************************************************");
		System.out.println();
		System.out.println();	
		railway rail=new railway();
		rail.main_menu();
	}			
}
