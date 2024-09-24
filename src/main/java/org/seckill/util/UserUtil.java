package org.seckill.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.seckill.entity.User;

/**
 * PreparedStatement<br>Java JDBC中用于执行预编译SQL语句的接口<br>
 * 特点:<br>
 * 预编译：执行之前对SQL语句进行预编译,SQL语句解析和编译只会发生一次,
 * 之后可以多次执行相同的SQL语句，只需更改参数值<br>
 * 参数化查询：使用占位符?表示SQL语句中的参数,可以有效防止SQL注入攻击<br>
 * 批处理支持：支持批量执行多条SQL语句，可以显著提高性能<br>
 * 使用方法:<br>
 * 1.通过Connection对象创建PreparedStatement：<br>
 * PreparedStatement preState = conn.prepareStatement(sql);<br>
 * 2.设置参数,使用setXXX方法设置SQL语句中的参数<br>
 * 3.执行SQL语句<br>
 * 3.1.executeQuery()：用于执行查询语句,返回ResultSet对象;<br>
 * 3.2.executeUpdate()：用于执行更新,插入或删除操作,返回受影响的行数;<br>
 * 3.3.executeBatch()：用于批量执行添加到批处理中的SQL语句;<br>
 * 关闭PreparedStatement,释放资源：<br>
 * preState.close();<br>
 */
public class UserUtil {

    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<User>(count);

        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(13000000000L + i);
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPassToDbPass("123456", user.getSalt()));
            users.add(user);
        }
        System.out.println("create user");

        Connection conn = DBUtil.getConn();
        String sql = "insert into sk_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";

        PreparedStatement preState = conn.prepareStatement(sql);
        for (User user : users) {
            preState.setInt(1, user.getLoginCount());
            preState.setString(2, user.getNickname());
            preState.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            preState.setString(4, user.getSalt());
            preState.setString(5, user.getPassword());
            preState.setLong(6, user.getId());
            preState.addBatch(); // 添加到批处理
        }
        preState.executeBatch(); // 执行批处理
        preState.close();
        conn.close();
        System.out.println("insert to db");

        String urlString = "http://localhost:8080/login/do_login";
        File file = new File("D:/tokens.txt");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("delete file");
            } else {
                System.out.println("delete fail");
            }
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        if (file.createNewFile()) {
            System.out.println("create file");
        } else {
            System.out.println("create fail");
        }
        raf.seek(0); // 设置文件指针偏移位置，以字节为单位从文件开头开始测量

        for (User user : users) {
            // 建立HTTP连接
            URL url = new URL(urlString);
            HttpURLConnection connHttp = (HttpURLConnection) url.openConnection();
            connHttp.setRequestMethod("POST");
            connHttp.setDoOutput(true); // 允许输出数据
            // 准备请求参数,发送请求
            OutputStream out = connHttp.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            // 读取响应
            InputStream inputStream = connHttp.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            // 解析JSON响应
            String response = bout.toString();
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token : " + user.getId());
            // 写入文件
            String row = user.getId() + "," + token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();

        System.out.println("over");
    }

    public static void main(String[] args) throws Exception {
        int count = 0;
        Scanner sc = new Scanner(System.in);
        count = sc.nextInt();
        createUser(count);
    }
}
