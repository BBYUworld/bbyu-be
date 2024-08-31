import pickle
from models import deposit_products_df, saving_products_df, loan_products_df

# 예금 전처리 데이터를 pickle로부터 로드
with open('deposit_preprocessing.pkl', 'rb') as f:
    deposit_preprocessing_dict = pickle.load(f)

deposit_label_encoders = deposit_preprocessing_dict['label_encoders']
deposit_scaler = deposit_preprocessing_dict['scaler']
deposit_categorical_features = deposit_preprocessing_dict['categorical_features']
deposit_numerical_features = deposit_preprocessing_dict['numerical_features']

# 적금 전처리 데이터를 pickle로부터 로드
with open('savings_preprocessing.pkl', 'rb') as f:
    saving_preprocessing_dict = pickle.load(f)

saving_label_encoders = saving_preprocessing_dict['label_encoders']
saving_scaler = saving_preprocessing_dict['scaler']
saving_categorical_features = saving_preprocessing_dict['categorical_features']
saving_numerical_features = saving_preprocessing_dict['numerical_features']

# 대출 전처리 데이터를 pickle로부터 로드
with open('loan_preprocessing.pkl', 'rb') as f:
    loan_preprocessing_dict = pickle.load(f)

loan_label_encoders = loan_preprocessing_dict['label_encoders']
loan_scaler = loan_preprocessing_dict['scaler']
loan_categorical_features = loan_preprocessing_dict['categorical_features']
loan_numerical_features = loan_preprocessing_dict['numerical_features']

# 기존 카테고리에 없는 새로운 값을 처리하기 위한 함수
def encode_with_unknown_handling(feature, encoder, value):
    if value in encoder.classes_:
        return encoder.transform([value])[0]
    else:
        return encoder.transform([encoder.classes_[0]])[0]

# 예금 상품 정보를 가져오는 함수
def get_deposit_product_info(deposit_id, deposit_products_df):
    return deposit_products_df[deposit_products_df['deposit_id'] == deposit_id].iloc[0]

# 적금 상품 정보를 가져오는 함수
def get_saving_product_info(saving_id, saving_products_df):
    return saving_products_df[saving_products_df['savings_id'] == saving_id].iloc[0]

# 대출 상품 정보를 가져오는 함수
def get_loan_product_info(loan_id, loan_products_df):
    return loan_products_df[loan_products_df['loan_id'] == loan_id].iloc[0]

# 예측 수행을 위한 전처리 및 데이터 보완 (예금)
def preprocess_and_predict_deposit(new_user_data, model, label_encoders, scaler, categorical_features, numerical_features):
    for feature in categorical_features:
        if feature in label_encoders:
            new_user_data[feature] = new_user_data[feature].apply(lambda x: encode_with_unknown_handling(feature, label_encoders[feature], x))

    for feature in numerical_features:
        if feature not in new_user_data.columns:
            new_user_data[feature] = 0

    new_user_data[numerical_features] = scaler.transform(new_user_data[numerical_features])

    X_categorical = [new_user_data[feature].values for feature in categorical_features]
    X_numerical = new_user_data[numerical_features].values

    prediction = model.predict(X_categorical + [X_numerical])

    return prediction

# 예측 수행을 위한 전처리 및 데이터 보완 (적금)
def preprocess_and_predict_saving(new_user_data, model, label_encoders, scaler, categorical_features, numerical_features):
    for feature in categorical_features:
        if feature in label_encoders:
            new_user_data[feature] = new_user_data[feature].apply(lambda x: encode_with_unknown_handling(feature, label_encoders[feature], x))

    for feature in numerical_features:
        if feature not in new_user_data.columns:
            new_user_data[feature] = 0

    new_user_data[numerical_features] = saving_scaler.transform(new_user_data[numerical_features])

    X_categorical = [new_user_data[feature].values for feature in categorical_features]
    X_numerical = new_user_data[numerical_features].values

    prediction = model.predict(X_categorical + [X_numerical])

    return prediction

# 예측 수행을 위한 전처리 및 데이터 보완 (대출)
def preprocess_and_predict_loan(new_user_data, model, label_encoders, scaler, categorical_features, numerical_features):
    for feature in categorical_features:
        if feature in label_encoders:
            new_user_data[feature] = new_user_data[feature].apply(lambda x: encode_with_unknown_handling(feature, label_encoders[feature], x))

    for feature in numerical_features:
        if feature not in new_user_data.columns:
            new_user_data[feature] = 0

    new_user_data[numerical_features] = loan_scaler.transform(new_user_data[numerical_features])

    X_categorical = [new_user_data[feature].values for feature in categorical_features]
    X_numerical = new_user_data[numerical_features].values

    prediction = model.predict(X_categorical + [X_numerical])

    return prediction

# 여러 deposit_id에 대해 예측을 수행하고, 상위 N개의 예금 상품을 추천하는 함수
def recommend_top_n_deposit_products(new_user_data, model, label_encoders, scaler, categorical_features, numerical_features, deposit_products_df, top_n=5):
    predictions = []

    for deposit_id in deposit_products_df['deposit_id'].unique():
        deposit_info = get_deposit_product_info(deposit_id, deposit_products_df)
        new_user_data['deposit_id'] = [deposit_id]
        new_user_data['deposit_interest_rate'] = [deposit_info['deposit_interest_rate']]
        new_user_data['term_months'] = [deposit_info['term_months']]
        new_user_data['min_deposit_amount'] = [deposit_info['min_deposit_amount']]
        new_user_data['max_deposit_amount'] = [deposit_info['max_deposit_amount']]

        prediction = preprocess_and_predict_deposit(new_user_data, model, label_encoders, scaler, categorical_features, numerical_features)
        predictions.append((deposit_id, prediction[0][0]))

    top_n_predictions = sorted(predictions, key=lambda x: x[1], reverse=True)[:top_n]

    return [(int(deposit_id), float(pred)) for deposit_id, pred in top_n_predictions]

# 여러 saving_id에 대해 예측을 수행하고, 상위 N개의 적금 상품을 추천하는 함수
def recommend_top_n_saving_products(new_user_data, model, label_encoders, scaler, categorical_features, numerical_features, saving_products_df, top_n=5):
    predictions = []

    for saving_id in saving_products_df['savings_id'].unique():
        saving_info = get_saving_product_info(saving_id, saving_products_df)
        new_user_data['savings_id'] = [saving_id]
        new_user_data['savings_interest_rate'] = [saving_info['savings_interest_rate']]
        new_user_data['term_months'] = [saving_info['term_months']]
        new_user_data['min_savings_amount'] = [saving_info['min_savings_amount']]
        new_user_data['max_savings_amount'] = [saving_info['max_savings_amount']]

        prediction = preprocess_and_predict_saving(new_user_data, model, label_encoders, scaler, categorical_features, numerical_features)
        predictions.append((saving_id, prediction[0][0]))

    top_n_predictions = sorted(predictions, key=lambda x: x[1], reverse=True)[:top_n]

    return [(int(saving_id), float(pred)) for saving_id, pred in top_n_predictions]

# 여러 loan_id에 대해 예측을 수행하고, 상위 N개의 대출 상품을 추천하는 함수
def recommend_top_n_loan_products(new_user_data, model, label_encoders, scaler, categorical_features, numerical_features, loan_products_df, top_n=5):
    predictions = []

    for loan_id in loan_products_df['loan_id'].unique():
        loan_info = get_loan_product_info(loan_id, loan_products_df)
        new_user_data['loan_id'] = [loan_id]
        new_user_data['interest_rate'] = [loan_info['interest_rate']]
        new_user_data['loan_limit'] = [loan_info['loan_limit']]
        new_user_data['loan_term_months'] = [loan_info['loan_term_months']]
        new_user_data['credit_score_requirement'] = [loan_info['credit_score_requirement']]

        prediction = preprocess_and_predict_loan(new_user_data, model, label_encoders, scaler, categorical_features, numerical_features)
        predictions.append((loan_id, prediction[0][0]))

    top_n_predictions = sorted(predictions, key=lambda x: x[1], reverse=True)[:top_n]

    return [(int(loan_id), float(pred)) for loan_id, pred in top_n_predictions]
