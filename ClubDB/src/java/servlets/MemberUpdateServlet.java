
package servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import business.Member;
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jahmya Goodwin
 */
public class MemberUpdateServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
       
        String URL="/MemberScreen.jsp";
        String msg="", sql="", lnm="", fnm="", mnm="";
        long newpwd = 0;
        
        String dbURL = "jdbc:mysql://localhost:3306/CLUB";
        String dbUSER = "root";
        String dbPWD = "sesame";
        
        try{
            Member m = (Member) request.getSession().getAttribute("m");
            Member n = new Member();
            n.setMemid(m.getMemid());
            n.setStatus(m.getStatus());
            n.setMemdt(m.getMemdt());
            n.setPassattempt(m.getPassattempt());
            
            lnm = request.getParameter("lastname");
            if (!lnm.isEmpty()){
                n.setLastname(lnm);
            }else {
                msg += "Error: Last name is missing<br>";
            }
            
            fnm = request.getParameter("firstname");
            if (!fnm.isEmpty()){
                n.setFirstname(fnm);
            }else {
                msg += "Error: First name is missing<br>";
            }
            
            mnm = request.getParameter("middlename");
            if (!mnm.isEmpty()){
                n.setMiddlename(mnm);
            }else {
                msg += "Error: Middle name is missing<br>";
            }
            
            try{
                newpwd = Long.parseLong(request.getParameter("psswd"));
                if (newpwd > 0) {
                    n.setPassword(newpwd);
                }else{
                    msg += "Error: password is not legal <br>";
                }
            } catch (NumberFormatException e){
                msg += "Error: password is not numeric. <br>";
            }
            
            if (msg.isEmpty()){
                //update db based on new 'n' object
                Connection conn = 
                        DriverManager.getConnection(dbURL, dbUSER, dbPWD);
                sql = "UPDATE tblMembers SET " +
                        " LastName = ?, " +
                        " FirstName = ?, " +
                        " MiddleName = ?, " +
                        " Password = ? " +
                        " WHERE MemID = ? ";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, n.getLastname());
                ps.setString(2, n.getFirstname());
                ps.setString(3, n.getMiddlename());
                ps.setLong(4, n.getPassword());
                ps.setString (5, n.getMemid());
                int rc = ps.executeUpdate();
                if (rc == 0) {
                    msg += "Member was not updateted br>";
                }else if (rc == 1) {
                    msg += "Member updated<br>";
                    m = n;
                    request.getSession().setAttribute("m", m);
                } else{
                    msg += "Warning: multiple records updated.<br>";
                }
                
            }
        } catch (SQLException e){
            msg += "SQL Error: " + e.getMessage() + "<br>" + sql + "<br>";
        } catch (Exception e){
            msg += "Servlet error: " + e.getMessage();
        }
        request.setAttribute("msg", msg);
        
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
