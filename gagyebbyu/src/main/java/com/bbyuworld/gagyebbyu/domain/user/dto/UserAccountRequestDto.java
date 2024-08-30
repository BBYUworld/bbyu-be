package com.bbyuworld.gagyebbyu.domain.user.dto;

import com.bbyuworld.gagyebbyu.domain.user.entity.Occupation;
import com.bbyuworld.gagyebbyu.domain.user.entity.Region;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.AccountDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountRequestDto {
    private AdditionalInfo additionalInfo;
    private List<AccountDto> selectedAccounts;

    @Override
    public String toString() {
        return "UserAccountRequestDto{" +
                "additionalInfo=" + additionalInfo +
                ", selectedAccounts=" + selectedAccounts +
                "}\n";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalInfo {
        private String gender;
        private int age;
        private int salary;
        private int desiredSpending;
        private Region region;
        private Occupation occupation;

        @Override
        public String toString() {
            return "AdditionalInfo{" +
                    "gender='" + gender + '\'' +
                    ", age=" + age +
                    ", salary=" + salary +
                    ", desiredSpending=" + desiredSpending +
                    "}\n";
        }
    }
}
