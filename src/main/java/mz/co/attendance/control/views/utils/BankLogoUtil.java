package mz.co.attendance.control.views.utils;

import mz.co.attendance.control.enums.Bank;

public class BankLogoUtil {

    public static String getBankLogoURL(Bank bank) {
        switch (bank) {
            case BCI:
                return "logos/bci.png";
            case STB:
                return "logos/standard.png";
            case MBIM:
                return "logos/bim.png";
            case FNB:
                return "logos/fnb.png";
            case SOCREMO:
                return "logos/socremo.png";
            case UBA:
                return "logos/uba.png";
            default:
                return "logos/bank.jpg";
        }
    }
}
