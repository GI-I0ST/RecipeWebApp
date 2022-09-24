package com.ghost.recipewebapp.dto;

import com.ghost.recipewebapp.validator.annotation.FieldsValueMatch;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "passwordConfirm",
                message = "Passwords do not match"
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 32)
    private String password;

    @NotBlank
    @Size(min = 6, max = 32)
    private String passwordConfirm;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
