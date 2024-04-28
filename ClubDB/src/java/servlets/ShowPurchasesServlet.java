
package servlets;

import business.ConnectionPool;
import business.Member;
import business.Purchase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author CITStudent
 */
public class ShowPurchasesServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String URL="/MemberScreen.jsp", sql="", msg="", dy="", yr="", mo="";
        String sqlwhere="";
        ArrayList<Purchase>pur;
        double totalBalanceDue = 0.0;
        double totalPaidAmount = 0.0;
        try{
            Member m = (Member) request.getSession().getAttribute("m");
            mo = request.getParameter("month");
            dy = request.getParameter("day");
            yr = request.getParameter("year");
            if (!mo.isEmpty() && !dy.isEmpty() && !yr.isEmpty()) {
                sqlwhere = yr + "-" + mo + "-" + dy;
            }
            
            sql = "SELECT p.MemID, p.PurchaseDt, p.TransType, p.TransCd, " +
                    " c.TransDesc, p.amount " +
                    " FROM tblpurchases p, tblcodes c " +
                    " WHERE p.TransCd = c.TransCd AND p.MemID = ?" ;
            if (!sqlwhere.isEmpty()){
                sql += " AND p.PurchaseDt >= ? ";
            }
             sql += " ORDER BY p.PurchaseDt";
                    
            
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection conn = pool.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
           
            ps.setString(1, m.getMemid());
            if (!sqlwhere.isEmpty()){
                ps.setString(2, sqlwhere);
            }
            
            ResultSet r = ps.executeQuery();
            pur = new ArrayList<>();
            //Your job in Finall version: add amount due to line
            while (r.next()) {
                Purchase p = new Purchase();
                p.setMemid(m.getMemid());
                p.setPurchasedt(r.getString("PurchaseDt"));
                p.setTranstype(r.getString("TransType"));
                p.setTranscd(r.getString("TransCd"));
                p.setTransdesc(r.getString("TransDesc"));
                p.setAmount(r.getDouble("amount"));
                pur.add(p);
                
                totalBalanceDue += p.getAmount();
            }
            
            request.setAttribute("pur", pur);
            r.last();
            //You: Improve line below to include total amount due
            String paymentSql = "SELECT SUM(amount) AS YTD_Total FROM tblpurchases WHERE MemID = ? AND TransType = 'P'";
            PreparedStatement paymentPs = conn.prepareStatement(paymentSql);
            paymentPs.setString(1, m.getMemid());
            ResultSet paymentRs = paymentPs.executeQuery();
            if (paymentRs.next()) {
                totalPaidAmount = paymentRs.getDouble("YTD_Total");
            }
            double totalBalanceDueA = totalBalanceDue - totalPaidAmount;
            
            msg += "Transaction records = " + r.getRow() + " ";
            msg += "Total Balance Due = " + totalBalanceDueA + "<br>";
            r.close();
            pool.freeConnection(conn);
            conn.close();
            URL = "/Purchases.jsp";
        }catch (SQLException e){
            msg += "SQL Error: " + e.getMessage() + "<br>" + sql + "<br>";
        }catch ( Exception e) {
            msg += "Servlet Error: " + e.getMessage() + "<br>";
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
