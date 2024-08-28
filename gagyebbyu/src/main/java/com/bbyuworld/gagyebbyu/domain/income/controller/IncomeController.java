package com.bbyuworld.gagyebbyu.domain.income.controller;

import com.bbyuworld.gagyebbyu.domain.expense.dto.param.ExpenseParam;
import com.bbyuworld.gagyebbyu.domain.income.dto.IncomeMonthDto;
import com.bbyuworld.gagyebbyu.domain.income.service.IncomeService;
import com.bbyuworld.gagyebbyu.global.jwt.RequireJwtToken;
import com.bbyuworld.gagyebbyu.global.jwt.UserContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;

    @GetMapping("/month")
    @RequireJwtToken
    public ResponseEntity<IncomeMonthDto> getIncomeAll(@ModelAttribute ExpenseParam param){
        return ResponseEntity.ok(incomeService.getIncomeAll(UserContext.getUserId(), param));
    }
}
