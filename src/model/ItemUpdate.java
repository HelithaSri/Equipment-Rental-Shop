package model;

import view.tm.RentDetailTM;

import java.util.ArrayList;

public class ItemUpdate {
    private String status;
    private ArrayList<RentDetailTM> list;

    public ItemUpdate() {
    }

    public ItemUpdate(String status, ArrayList<RentDetailTM> list) {
        this.setStatus(status);
        this.setList(list);
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RentDetailTM> getList() {
        return list;
    }

    public void setList(ArrayList<RentDetailTM> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ItemUpdate{" +
                "status='" + status + '\'' +
                ", list=" + list +
                '}';
    }
}
