package com.bbyuworld.gagyebbyu.domain.income.service;

import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.expense.dto.param.ExpenseParam;
import com.bbyuworld.gagyebbyu.domain.expense.dto.response.ExpenseOverviewDto;
import com.bbyuworld.gagyebbyu.domain.income.dto.IncomeMonthDto;
import com.bbyuworld.gagyebbyu.domain.income.dto.IncomeOverviewDto;
import com.bbyuworld.gagyebbyu.domain.income.repository.IncomeRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.error.ErrorCode;
import com.bbyuworld.gagyebbyu.global.error.type.DataNotFoundException;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final CoupleRepository coupleRepository;
    private final UserRepository userRepository;


    public IncomeMonthDto getIncomeAll(Long userId, ExpenseParam param){
        Integer month = param.getMonth() != null ? param.getMonth() : LocalDateTime.now().getMonthValue();
        Integer year = param.getYear() != null ? param.getYear() : LocalDateTime.now().getYear();
        String sort = param.getSort() != null ? param.getSort() : "asc";

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
        System.out.println("user id = "+user.getUserId());
        Couple couple = coupleRepository.findById(user.getCoupleId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.COUPLE_NOT_FOUND));
        System.out.println("couple id = "+couple.getCoupleId());

        long totalAmount = 0;
        long targetAmount = couple.getMonthlyTargetAmount();
        List<Tuple> incomesTuple = incomeRepository.findIncomesByMonth(month, year, couple.getCoupleId(), sort);
        List<IncomeOverviewDto> incomes = new ArrayList<>();
        for (Tuple tuple : incomesTuple) {
            LocalDateTime dateTime = tuple.get(0, LocalDateTime.class);
            LocalDate date = dateTime.toLocalDate();
            Long amount = tuple.get(2, Long.class);
            totalAmount += amount;
            incomes.add(new IncomeOverviewDto(couple.getCoupleId(), date, amount));
        }
        return null;
    }
}
