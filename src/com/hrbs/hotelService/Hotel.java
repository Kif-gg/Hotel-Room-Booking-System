package com.hrbs.hotelService;

import com.hrbs.Main;

public class Hotel {
    private double totalIncome;
    private double totalCancellationFees;

    public Hotel() {
        this.totalIncome = 0;
        this.totalCancellationFees = 0;
    }

    public void setTotalIncome(double currentIncome) {
        this.totalIncome += currentIncome;
    }

    public void setTotalCancellationFees(double currentFee) {
        this.totalCancellationFees += currentFee;
    }

    public double getTotalIncome() {
        if (Main.checkIfAdminAccessOn()) {
            return this.totalIncome;
        } else {
            System.out.println("ACCESS DENIED: NO PERMISSION!");
            return 0;
        }
    }

    public double getTotalCancellationFees() {
        if (Main.checkIfAdminAccessOn()) {
            return this.totalCancellationFees;
        } else {
            System.out.println("ACCESS DENIED: NO PERMISSION!");
            return 0;
        }
    }
}
