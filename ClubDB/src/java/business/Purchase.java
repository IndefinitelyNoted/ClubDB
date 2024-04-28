
package business;

/**
 *
 * @author Jahmya Goodiwn
 */
public class Purchase {
    private String memid, purchasedt, transtype, transcd, transdesc;
    private double amount;
    
    public Purchase(){
        this.memid = "";
        this.purchasedt = "";
        this.transtype = "";
        this.transcd = "";
        this.transdesc = "";
        this.amount = 0;
    }

    public String getMemid() {
        return memid;
    }

    public void setMemid(String memid) {
        this.memid = memid;
    }

    public String getPurchasedt() {
        return purchasedt;
    }

    public void setPurchasedt(String purchasedt) {
        this.purchasedt = purchasedt;
    }

    public String getTranstype() {
        return transtype;
    }

    public void setTranstype(String transtype) {
        this.transtype = transtype;
    }

    public String getTranscd() {
        return transcd;
    }

    public void setTranscd(String transcd) {
        this.transcd = transcd;
    }

    public String getTransdesc() {
        return transdesc;
    }

    public void setTransdesc(String transdesc) {
        this.transdesc = transdesc;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
}
