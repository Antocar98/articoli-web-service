package com.xantrix.webapp.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CodArtValidator  implements ConstraintValidator<CodArt, String>
{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        //Verifichiamo che sia numerico
        if (value == null || !pattern.matcher(value).matches())
        {
            return false;
        }

        //verifichiama che inizi con il valore 500
        if (!value.substring(0, 3).equals("500"))
        {
            return false;
        }

        return true;
    }

}