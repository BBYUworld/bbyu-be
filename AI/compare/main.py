from fastapi import FastAPI
from pydantic import BaseModel
import pandas as pd
import numpy as np

class Loan:
    def __init__(self, amount, interest_rate, years):
        self.amount = amount
        self.interest_rate = interest_rate
        self.years = years

    def total_payment(self):
        return self.amount * (1 + self.interest_rate * self.years)

    def annual_payment(self):
        return self.total_payment() / self.years

class CoupleLoanSystem:
    def __init__(self, male_income, female_income, male_debt, female_debt, target_amount, loan_products,
                 male_credit_score, female_credit_score, male_mortgage_loan_amount, female_mortgage_loan_amount):
        self.male_income = male_income
        self.female_income = female_income
        self.male_debt = male_debt  # 스프링에서 스트레스 금리가 반영된 연간 상환액
        self.female_debt = female_debt  # 스프링에서 스트레스 금리가 반영된 연간 상환액
        self.target_amount = target_amount
        self.loan_products = loan_products
        self.male_credit_score = male_credit_score
        self.female_credit_score = female_credit_score
        self.male_mortgage_loan_amount = male_mortgage_loan_amount
        self.female_mortgage_loan_amount = female_mortgage_loan_amount

    def initial_ratio(self):
        male_dsr = self.male_debt / self.male_income
        female_dsr = self.female_debt / self.female_income

        male_factor = self.male_income / (self.male_income + self.female_income) * (1 - male_dsr)
        female_factor = self.female_income / (self.male_income + self.female_income) * (1 - female_dsr)

        total_factor = male_factor + female_factor
        male_ratio = male_factor / total_factor
        female_ratio = female_factor / total_factor

        return male_ratio, female_ratio

    def check_dsr_compliance(self, male_ratio, female_ratio, male_loan, female_loan):
        # 새로운 대출에 대한 연간 상환액 계산
        male_annual_payment = Loan(self.target_amount * male_ratio, male_loan.interest_rate / 100,
                                   male_loan.loan_term_months / 12).annual_payment()
        female_annual_payment = Loan(self.target_amount * female_ratio, female_loan.interest_rate / 100,
                                     female_loan.loan_term_months / 12).annual_payment()

        # 이미 스트레스 금리가 반영된 부채값 사용
        male_dsr = (self.male_debt + male_annual_payment) / self.male_income
        female_dsr = (self.female_debt + female_annual_payment) / self.female_income

        # DSR이 40%를 초과하는지 여부를 판단
        if male_dsr > 0.4 or female_dsr > 0.4:
            return False, male_dsr, female_dsr
        return True, male_dsr, female_dsr

    def optimize_loan_combination(self):
        combinations = []
        used_combinations = set()

        initial_male_ratio, _ = self.initial_ratio()

        for male_loan in self.loan_products.itertuples(index=False):
            if self.male_credit_score < male_loan.credit_score_requirement:
                continue

            for female_loan in self.loan_products.itertuples(index=False):
                if self.female_credit_score < female_loan.credit_score_requirement:
                    continue

                for male_ratio in np.linspace(0, 1, 101):
                    total_payment = self.calculate_total_payment(male_ratio, male_loan, female_loan)
                    compliance, male_dsr, female_dsr = self.check_dsr_compliance(
                        male_ratio, 1 - male_ratio, male_loan, female_loan)

                    loan_combination = (male_loan.loan_id, female_loan.loan_id)

                    if compliance and loan_combination not in used_combinations:
                        combinations.append({
                            "male_loan_id": male_loan.loan_id,
                            "male_interest_rate": male_loan.interest_rate,
                            "male_loan_limit": male_loan.loan_limit,
                            "male_loan_term_months": male_loan.loan_term_months,
                            "male_credit_score_requirement": male_loan.credit_score_requirement,
                            "female_loan_id": female_loan.loan_id,
                            "female_interest_rate": female_loan.interest_rate,
                            "female_loan_limit": female_loan.loan_limit,
                            "female_loan_term_months": female_loan.loan_term_months,
                            "female_credit_score_requirement": female_loan.credit_score_requirement,
                            "male_ratio": male_ratio,
                            "female_ratio": 1 - male_ratio,
                            "total_payment": total_payment,
                            "male_dsr": male_dsr,
                            "female_dsr": female_dsr,
                            "male_stress_dsr": 0,
                            "female_stress_dsr": 0
                        })
                        used_combinations.add(loan_combination)

        top_combinations = sorted(combinations, key=lambda x: x['total_payment'])[:3]
        return top_combinations

    def calculate_total_payment(self, male_ratio, male_loan, female_loan):
        male_payment = Loan(self.target_amount * male_ratio, male_loan.interest_rate / 100,
                            male_loan.loan_term_months / 12).total_payment()
        female_payment = Loan(self.target_amount * (1 - male_ratio), female_loan.interest_rate / 100,
                              female_loan.loan_term_months / 12).total_payment()
        return male_payment + female_payment


# FastAPI 인스턴스 생성
app = FastAPI()

# 입력 모델 정의
class LoanRequest(BaseModel):
    male_income: int
    female_income: int
    male_debt: int  # 이미 스트레스 금리가 반영된 연간 상환액
    female_debt: int  # 이미 스트레스 금리가 반영된 연간 상환액
    target_amount: int
    stress_rate: float  # 스프링에서 이미 반영된 금리라면, 이 변수는 사용되지 않음
    male_credit_score: int
    female_credit_score: int
    male_mortgage_loan_amount: int
    female_mortgage_loan_amount: int


# 최적의 대출 조합을 계산하는 엔드포인트 정의
@app.post("/ai/recommend/compare")
def optimize_loan(request: LoanRequest):
    # 대출 상품 데이터를 불러옵니다 (실제 사용 시 경로를 조정해야 할 수도 있음)
    loan_products_df = pd.read_csv('realistic_korean_loan_products.csv')

    # 시스템 초기화
    system = CoupleLoanSystem(
        request.male_income,
        request.female_income,
        request.male_debt,
        request.female_debt,
        request.target_amount,
        loan_products_df,
        request.male_credit_score,
        request.female_credit_score,
        request.male_mortgage_loan_amount,
        request.female_mortgage_loan_amount
    )

    # 최적의 대출 조합 계산
    top_combinations = system.optimize_loan_combination()

    # 결과 반환
    return top_combinations


# 앱 실행 (개발 환경에서 사용, 실제 배포 시 Uvicorn을 사용)
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
