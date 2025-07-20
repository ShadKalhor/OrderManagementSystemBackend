package OrderManager.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"Gender\"")
public class Gender {

    @Id
    private Integer Id;
    private String Name;


    public int getId(){
        return Id;
    }
    public void setId(int newId){
        Id = newId;
    }

    public String getName(){
        return Name;
    }
    public void setName(String newName){
        Name = newName;
    }


}
