package dto;

public abstract class Person {
    int id;
    String name;
    int healthInsuranceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealthInsuranceId() {
        return healthInsuranceId;
    }

    public void setHealthInsuranceId(int healthInsuranceId) {
        this.healthInsuranceId = healthInsuranceId;
    }
}
