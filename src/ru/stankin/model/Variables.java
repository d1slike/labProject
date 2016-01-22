package ru.stankin.model;


/**
 * Created by DisDev on 22.01.2016.
 */
public class Variables {
    private double H;
    private double R;
    private double L;
    private double E;
    private double ZC;
    private double RO;
    private double GAMMA;
    private double M;

    public Variables() {
        H = 0;
        R = 0;
        L = 0;
        E = 0;
        ZC = 0;
        RO = 0;
        GAMMA = 0;
        M = 0;
    }

    public double getH() {
        return H;
    }

    public double getR() {
        return R;
    }

    public double getL() {
        return L;
    }

    public double getE() {
        return E;
    }

    public double getZC() {
        return ZC;
    }

    public double getRO() {
        return RO;
    }

    public double getGAMMA() {
        return GAMMA;
    }

    public double getM() {
        return M;
    }

    public void setH(double newValue) {
        H = newValue;
    }

    public void setR(double newValue) {
        R = newValue;
    }

    public void setL(double newValue) {
        L = newValue;
    }

    public void setE(double newValue) {
        E = newValue;
    }

    public void setZC(double newValue) {
        ZC = newValue;
    }

    public void setRO(double newValue) {
        RO = newValue;
    }

    public void setGAMMA(double newValue) {
        GAMMA = newValue;
    }

    public void setM(double newValue) {
        M = newValue;
    }

}
