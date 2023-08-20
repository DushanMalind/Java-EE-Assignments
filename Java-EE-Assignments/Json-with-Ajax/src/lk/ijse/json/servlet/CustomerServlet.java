package lk.ijse.json.servlet;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import static java.lang.Class.forName;

//http://localhost:8080/pos_one/customer
//http://localhost:8080/pos_one/pages/customer? 404
//http://localhost:8080/customer? 404

//http://localhost:8080/pos_one/pages/customer//
//http:://localhost:8080/pos_one/pages/customer
//http:://localhost:8080/pos_one/pages/customer

@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            /*<!--when the response received catch it and set it to the table-->*/

            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from customer");
            ResultSet rst = pstm.executeQuery();

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (rst.next()){
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                String contact=rst.getString(4);

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("id",id);
                objectBuilder.add("name",name);
                objectBuilder.add("address",address);
                objectBuilder.add("contact",contact);
                arrayBuilder.add(objectBuilder.build());
            }
            resp.setContentType("application/json");
            resp.getWriter().print(arrayBuilder.build());


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void objectBilderSuccess(HttpServletResponse resp) throws IOException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("status","success");
        objectBuilder.add("message","Customer Added..!");
        resp.setContentType("application/json");
        resp.getWriter().print(objectBuilder.build());
    }

    private void objectBilderFail(HttpServletResponse resp) throws IOException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("status","fail");
        objectBuilder.add("message","Customer Not Added..!");
        resp.setContentType("application/json");
        resp.setStatus(400);
        resp.getWriter().print(objectBuilder.build());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
        String cusSalary = req.getParameter("contact");
        String option = req.getParameter("option");

        try {
            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");

            switch (option) {
                case "add":
                    PreparedStatement pstm = connection.prepareStatement("Insert into customer values(?,?,?,?)");

                    pstm.setObject(1, cusID);
                    pstm.setObject(2, cusName);
                    pstm.setObject(3, cusAddress);
                    pstm.setObject(4, cusSalary);

                    if (pstm.executeUpdate() > 0) {
                       /* resp.getWriter().println("Customer Added..!");*/
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("status","success");
                        objectBuilder.add("message","Customer Added..!");
                        resp.setContentType("application/json");
                        resp.getWriter().print(objectBuilder.build());


                    }else {
                        resp.getWriter().println("Customer Not Added..!");
                        String jsonResponse = "{\"status\": \"fail\", \"message\": \"Customer Not Added..!\"}";
                        resp.getWriter().println(jsonResponse);
                    }
                    break;
              /*  case "delete":
                    PreparedStatement pstm2 = connection.prepareStatement("delete from Customer where id=?");
                    pstm2.setObject(1, cusID);
                    if (pstm2.executeUpdate() > 0) {
                        resp.getWriter().println("Customer Deleted..!");
                    }
                    break;*/
               /* case "update":
                    PreparedStatement pstm3 = connection.prepareStatement("update Customer set name=?,address=? where id=?");
                    pstm3.setObject(3, cusID);
                    pstm3.setObject(1, cusName);
                    pstm3.setObject(2, cusAddress);
                    if (pstm3.executeUpdate() > 0) {
                        resp.getWriter().println("Customer Updated..!");
                    }
                    break;*/
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status","success");
            objectBuilder.add("message",e.getMessage());
            objectBuilder.add("data",e.getErrorCode());
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print(objectBuilder.build());

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
        String contact = req.getParameter("contact");
        String option = req.getParameter("option");

        System.out.println(cusID+" "+cusName);

        try {
            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");


                PreparedStatement pstm3 = connection.prepareStatement("update customer set name=?, address=?, contact=? where id=?");

                pstm3.setObject(1, cusName);
                pstm3.setObject(2, cusAddress);
                pstm3.setObject(3, contact);
                pstm3.setObject(4, cusID);
                System.out.println("sql" + pstm3);
                if (pstm3.executeUpdate() > 0) {
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("status", "success");
                    objectBuilder.add("message", "Customer Updated..!");
                    resp.setContentType("application/json");
                    resp.getWriter().print(objectBuilder.build());

                } else {
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("status", "fail");
                    objectBuilder.add("message", "Customer Not Updated..!");
                    resp.setContentType("application/json");
                    resp.setStatus(400);
                    resp.getWriter().print(objectBuilder.build());
                }

        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status","success");
            objectBuilder.add("message",e.getMessage());
            objectBuilder.add("data",e.getErrorCode());
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print(objectBuilder.build());


        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        String option = req.getParameter("option");

        try {
            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");


                PreparedStatement pstm2 = connection.prepareStatement("delete from customer where id=?");
                pstm2.setObject(1,cusID);

                System.out.println("Sql"+pstm2);
                if (pstm2.executeUpdate()>0){
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("status","success");
                    objectBuilder.add("message","Customer Deleted..!");
                    resp.setContentType("application/json");
                    resp.getWriter().print(objectBuilder.build());
                }else {
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("status","fail");
                    objectBuilder.add("message","Customer Not Deleted..!");
                    resp.setContentType("application/json");
                    resp.setStatus(400);
                    resp.getWriter().print(objectBuilder.build());
                }

        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status","success");
            objectBuilder.add("message",e.getMessage());
            objectBuilder.add("data",e.getErrorCode());
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print(objectBuilder.build());

        }
    }
}
