package data;

public class Car extends MotorVehicle {

    private Dashboard dashboard;

    public Car() {
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }
}
