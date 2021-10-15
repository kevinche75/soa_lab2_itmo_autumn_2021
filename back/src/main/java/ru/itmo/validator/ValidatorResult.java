package ru.itmo.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ValidatorResult {

    private String message = "";
    private boolean status = true;
    private int code;

    public void addMessage(String newMessage){
        this.message += newMessage + "\n";
        this.status = false;
    }
}
