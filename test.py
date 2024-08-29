import pandas as pd
import mysql.connector
from mysql.connector import Error

# 데이터베이스 연결 정보 설정
# db_config = {
#     'host': '3.39.19.140',
#     'user': 'ssafy',
#     'password': 'ssafy',
#     'database': 'gagyebbyu',
#     'port': 3306
# }

db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': '1234',
    'database': 'gagyebbyu',
    'port': 3306
}

# CSV 파일을 읽습니다.
csv_file_path = 'C:/Users/SSAFY/Downloads/realistic_korean_deposit_products.csv'  # 파일 경로를 적절하게 설정하세요
df = pd.read_csv(csv_file_path)

def insert_deposit_products():
    connection = None
    try:
        # 데이터베이스 연결
        connection = mysql.connector.connect(**db_config)
        cursor = connection.cursor()

        # 각 행을 데이터베이스에 삽입
        for _, row in df.iterrows():
            insert_query = """
            INSERT INTO deposit (deposit_id, deposit_interest_rate, term_months, min_deposit_amount, max_deposit_amount, deposit_interest_payment_method, 
            deposit_name, bank_name, description)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            cursor.execute(insert_query, (
                row['deposit_id'],
                row['deposit_interest_rate'],
                row['term_months'],
                row['min_deposit_amount'],
                row['max_deposit_amount'],
                row['deposit_interest_payment_method'],
                "name",
                "한국은행",
                "예금에 대한 간단한 설명"
            ))

        # 변경사항 커밋
        connection.commit()
        print("Deposit products have been inserted successfully.")

    except Error as e:
        print(f"Error: {e}")

    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()
            print("MySQL connection is closed.")

# 데이터 삽입 함수 호출
insert_deposit_products()
