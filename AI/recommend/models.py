import pandas as pd
import tensorflow as tf

# 예금 데이터 로드
user_data_file = 'realistic_korean_financial_user_data.csv'
deposit_product_file = 'realistic_korean_deposit_products.csv'
deposit_application_file = 'realistic_korean_deposit_applications.csv'

user_data_df = pd.read_csv(user_data_file)
deposit_applications_df = pd.read_csv(deposit_application_file)
deposit_products_df = pd.read_csv(deposit_product_file)

# 적금 데이터 로드
saving_product_file = 'realistic_korean_savings_products.csv'
saving_application_file = 'realistic_korean_savings_applications.csv'

saving_applications_df = pd.read_csv(saving_application_file)
saving_products_df = pd.read_csv(saving_product_file)

# 대출 데이터 로드
loan_product_file = 'realistic_korean_loan_products.csv'
loan_application_file = 'realistic_korean_loan_applications.csv'

loan_applications_df = pd.read_csv(loan_application_file)
loan_products_df = pd.read_csv(loan_product_file)

# 학습된 모델 로드
deposit_model = tf.keras.models.load_model('deposit_deepfm_model.h5')
saving_model = tf.keras.models.load_model('savings_deepfm_model.h5')
loan_model = tf.keras.models.load_model('loan_deepfm_model.h5')
