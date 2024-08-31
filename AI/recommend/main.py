from fastapi import FastAPI
from pandas import DataFrame
from models import deposit_model, saving_model, loan_model, deposit_products_df, saving_products_df, loan_products_df
from schemas import DepositInput, SavingInput, LoanInput
from preprocessing import (
    recommend_top_n_deposit_products, recommend_top_n_saving_products, recommend_top_n_loan_products,
    deposit_label_encoders, deposit_scaler, deposit_categorical_features, deposit_numerical_features,
    saving_label_encoders, saving_scaler, saving_categorical_features, saving_numerical_features,
    loan_label_encoders, loan_scaler, loan_categorical_features, loan_numerical_features
)

# FastAPI 애플리케이션 생성
app = FastAPI()

@app.post("/ai/recommend/deposit")
def recommend_deposit_product_api(user_input: DepositInput):
    new_user_data = DataFrame([user_input.dict()])

    top_5_deposit_products = recommend_top_n_deposit_products(
        new_user_data, deposit_model, deposit_label_encoders, deposit_scaler,
        deposit_categorical_features, deposit_numerical_features,
        deposit_products_df, top_n=5
    )

    return {"top_5_deposit_products": top_5_deposit_products}

@app.post("/ai/recommend/savings")
def recommend_saving_product_api(user_input: SavingInput):
    new_user_data = DataFrame([user_input.dict()])

    top_5_saving_products = recommend_top_n_saving_products(
        new_user_data, saving_model, saving_label_encoders, saving_scaler,
        saving_categorical_features, saving_numerical_features,
        saving_products_df, top_n=5
    )

    return {"top_5_saving_products": top_5_saving_products}

@app.post("/ai/recommend/loan")
def recommend_loan_product_api(user_input: LoanInput):
    new_user_data = DataFrame([user_input.dict()])

    top_5_loan_products = recommend_top_n_loan_products(
        new_user_data, loan_model, loan_label_encoders, loan_scaler,
        loan_categorical_features, loan_numerical_features,
        loan_products_df, top_n=5
    )

    return {"top_5_loan_products": top_5_loan_products}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)