from pydantic import BaseModel

# 예금 추천을 위한 입력 데이터 구조
class DepositInput(BaseModel):
    user_id: int
    gender: int
    region: str
    occupation: str
    late_payment: int
    financial_accident: int
    age: int
    annual_income: int
    debt: int
    credit_score: int
    annual_spending: int
    num_cards: int

# 적금 추천을 위한 입력 데이터 구조
class SavingInput(BaseModel):
    user_id: int
    gender: int
    region: str
    occupation: str
    late_payment: int
    financial_accident: int
    age: int
    annual_income: int
    debt: int
    credit_score: int
    annual_spending: int
    num_cards: int

# 대출 추천을 위한 입력 데이터 구조
class LoanInput(BaseModel):
    user_id: int
    gender: int
    region: str
    occupation: str
    late_payment: int
    financial_accident: int
    age: int
    annual_income: int
    debt: int
    credit_score: int
    annual_spending: int
    num_cards: int
    total_deposit: int
    total_savings: int
    total_assets: int
