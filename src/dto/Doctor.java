package dto;

public class Doctor extends Person {
    int specialityId;

    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }

    public int getSpecialityId(){
        return specialityId;
    }
}
