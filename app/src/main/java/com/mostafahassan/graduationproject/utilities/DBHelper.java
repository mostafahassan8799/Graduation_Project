package com.mostafahassan.graduationproject.utilities;


import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DBHelper {
    String fname;
    String orderId;
    String orderType;
    String date;
    String address;
    String serviceType;
    String payMethod;
    Connection con;
    ResultSet resultSet;
    Statement statement = null;
    PreparedStatement preparedStatement;
    Random random = new Random();
    int randomNum = random.nextInt((100000 - 10) + 1) + 10;
    String getRand = Integer.toString(randomNum);
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }




    public Connection connectDB() {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:jtds:sqlserver://den1.mssql8.gear.host/governmental", "governmental", "M01207443941i#");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
    public String register(String card, String fname, String lname, String email, String phone, String password)
    {
        String messageOk,messageFalse;
        messageOk="تم التسجيل بنجاح";
        messageFalse="الرقم القومي موجود بالفعل";
        String query="insert into AspNetUsers(Id,UserName,FirstName,LastName,Email,PasswordHash,PhoneNumber) values(?,?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,getRand);
            preparedStatement.setString(2, card);
            preparedStatement.setString(3, fname);
            preparedStatement.setString(4, lname);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, phone);
            preparedStatement.setString(7, password);
            preparedStatement.execute();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
           if (throwables.getErrorCode()==2627)
           {
               return messageFalse;
           }
        }
        return messageOk;
    }
    public ResultSet login(String sqlQuery)
    {
        try {
            Statement statement =con.createStatement();
            resultSet = statement.executeQuery(sqlQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        return resultSet;

    }
    public String contactUS(String fullName, String email, String phoneNumber, String message, String serviceType)
    {
        String messageOk,messageFalse;
        messageOk="تم الإرسال";
        messageFalse="تأكد من الاتصال بالانترنت";
        String query="insert into ContactUs(FullName,Email,PhoneNumber,Message,Serves) Values(?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setString(4, message);
            preparedStatement.setString(5, serviceType);
            preparedStatement.execute();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (throwables.getErrorCode()==40)
            {
                return messageFalse;
            }
        }
        return messageOk;
    }
   public String updateProfile(String id,String fname,String lname,String phone,String email,String pass)
    {
       String messageOk="تم تحديث بياناتك";
       String messageFalse="تأكد من الاتصال بالانترنت";
        String query="update AspNetUsers set FirstName=?,LastName =?,Email=?,PhoneNumber=?,PasswordHash=? where UserName =?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,fname);
            preparedStatement.setString(2, lname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, pass);
            preparedStatement.setString(6,id);
            preparedStatement.execute();
            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return messageFalse;
        }
        return messageOk;

    }
    public String idServiceUpload(String name, String id, String phone1, String phone2, String email, String governorate, String region, String address, String serviceType
                                    , String cardType, byte[] img1, byte[] img2, String notes, String paymentMethod,String orderType,String birth,String religion,String status
                                    ,String qualification,String type,int isPayed)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());


        String messageOk="تم انشاء الطلب بصوره صحيحة";
        String messageFalse="تأكد من الاتصال بالانترنت";
        String query="insert into RegisterCivil (FullName,CardID,Phone1,Phone2,Email,governorate,region,Address,service_type,card_type,Photo1,Photo2,Notes,payment_method,Date,OrderType,BirthDate,Religion,SocialStatus,qualification,Kind,IsPaid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, phone1);
            preparedStatement.setString(4, phone2);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6,governorate);
            preparedStatement.setString(7,region);
            preparedStatement.setString(8,address);
            preparedStatement.setString(9,serviceType);
            preparedStatement.setString(10,cardType);
            preparedStatement.setBytes(11,img1);
            preparedStatement.setBytes(12,img2);
            preparedStatement.setString(13,notes);
            preparedStatement.setString(14,paymentMethod);
            preparedStatement.setString(15,currentDate);
            preparedStatement.setString(16,orderType);
            preparedStatement.setString(17,birth);
            preparedStatement.setString(18,religion);
            preparedStatement.setString(19,status);
            preparedStatement.setString(20,qualification);
            preparedStatement.setString(21,type);
            preparedStatement.setInt(22,isPayed);
            preparedStatement.execute();
            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return messageFalse;
        }
        return messageOk;
    }

    /*public String orderTracking(String id)
    {
        String messageOk="تم";
        String messageFalse="لا يوجد طلب بهذا الرقم القومي";

        String query="select FullName from RegisterCivil where CardID ='"+id+"' ";
        try {
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()){
                fname = resultSet.getString(0);


            }
            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return messageFalse;
        }
        return messageOk;


    }*/





    }







