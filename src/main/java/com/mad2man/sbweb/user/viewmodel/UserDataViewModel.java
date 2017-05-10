package com.mad2man.sbweb.user.viewmodel;


import com.mad2man.sbweb.entity.ClientEntity;
import com.mad2man.sbweb.entity.RoleEntity;
import com.mad2man.sbweb.entity.UserDataEntity;
import com.mad2man.sbweb.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataViewModel {

    // example
    private String address;

    public UserDataViewModel(UserDataEntity userDataEntity) {

        this(userDataEntity.getAddress());
    }
}
