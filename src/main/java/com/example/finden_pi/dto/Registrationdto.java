package com.example.finden_pi.dto;

import com.example.finden_pi.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para cadastro de novos usuários.
 * Inclui validações extras para telefone, senha e campos de fornecedores.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Registrationdto {

    // ------------------------------
    // Dados pessoais
    // ------------------------------
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "Confirmação de senha é obrigatória")
    private String confirmPassword;

    @NotNull(message = "Tipo de usuário é obrigatório")
    private User.UserType userType;

    /**
     * Telefone / WhatsApp
     * Formatos aceitos:
     * (11) 91234-5678
     * 11912345678
     * +55 11 91234-5678
     */
    @Pattern(regexp = "^((\\+55)?\\s?\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4})$", message = "Telefone inválido. Formato esperado: (11) 91234-5678")
    private String phone;

    private String city;

    @Size(max = 50, message = "Estado deve ter no máximo 50 caracteres")
    private String state;

    // ------------------------------
    // Dados opcionais (somente fornecedores)
    // ------------------------------

    @Size(max = 100, message = "Nome da empresa deve ter no máximo 100 caracteres")
    private String companyName;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    // Utilitário para validar senha = confirmPassword (feito no controller)
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}
