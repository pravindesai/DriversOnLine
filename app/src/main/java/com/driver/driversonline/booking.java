package com.driver.driversonline;
import java.io.Serializable;
/*                    node[id]-->|------startDate
                                 |------endDate
                                 |------owner number i.e. current user number
                                 |------owner city
                                 |------Driver num/id i.e user u
                                 |------Action [None/Accepted/Rejected]

                      */
public class booking implements Serializable{
    public String startDate;
    public String endDate;
    public String Onum;
    public String Oname;
    public String Ocity;
    public String Dnum;
    public String Action;

    public booking() {
    }

    public booking(String startDate, String endDate, String Onum,String Oname, String Ocity, String Dnum, String Action) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.Onum = Onum;
        this.Oname=Oname;
        this.Ocity = Ocity;
        this.Dnum = Dnum;
        this.Action = Action;
    }
}
