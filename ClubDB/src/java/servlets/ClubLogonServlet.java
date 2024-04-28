
package servlets;

import business.Member;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jahmya Goodwin
 */
public class ClubLogonServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String URL = "/Logon.jsp", msg="", sql="", memid="";
        long passattempt = -1;
        Member m;
        
        String dbURL = "jdbc:mysql://localhost:3306/CLUB";
        String dbUSER = "root";
        String dbPWD = "sesame";
        
        try {
            memid = request.getParameter("userid").trim();
            passattempt = Long.parseLong( request.getParameter("password").trim());
            
            //load and register the JDBC driver for MySql
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(dbURL, dbUSER, dbPWD);
            Statement s = conn.createStatement();
            
            sql = "SELECT * FROM tblmembers WHERE MemID = '" + memid + "'";
            ResultSet r = s.executeQuery(sql);
            if (r.next()) {
                m = new Member();
                m.setMemid(memid);
                m.setPassword(r.getLong("Password"));
                m.setPassattempt(passattempt);
                if (m.isAuthenticated()){
                    m.setLastname(r.getString("LastName"));
                    m.setFirstname(r.getString("FirstName"));
                    m.setMiddlename(r.getString("MiddleName"));
                    m.setStatus(r.getString("Status"));
                    m.setMemdt(r.getString("Memdt"));
                    msg += "Member " + memid + " authinticated! <br>";
                    URL = "/MemberScreen.jsp";
                }else {
                    msg += "Member " + memid + " on file but not authenticated <br>";
                }
                request.getSession().setAttribute("m",m);
            }else {
                msg += "Member " + memid + " not found in db<br>";
            }
            r.close();
            s.close();
            conn.close();
        }catch (NumberFormatException e){
            msg += "Password not numeric.<br>";
        }catch (ClassNotFoundException e){
            msg += "JDBC Driver not found in project<br>";
        } catch (SQLException e){
            msg += "Connection error: " + e.getMessage() + "<br>";
        }catch (Exception e) {
            msg += "Servlet error: " + e.getMessage();
        }
        
        request.setAttribute("msg", msg);
        Cookie uid = new Cookie("userid", memid);
        uid.setMaxAge(60*60*5);
        uid.setPath("/");
        response.addCookie(uid);
        
        RequestDispatcher disp = getServletContext().getRequestDispatcher(URL);
        disp.forward(request,response);
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
