import pandas as pd
import re

def regularize(df, cased=False):  # cased = True -> 대소문자 구별 / cased = False -> 영어는 모두 소문자
    if isinstance(df, pd.DataFrame):

        if df['거래일자'].dtype == int:
            df['거래일자'] = pd.to_datetime(df['거래일자'], format='%Y%m%d').astype(str)

        if df['금액'].dtype != int:
            df['금액'] = df['금액'].str.replace(',', '').astype(int)

        df['업체명_r'] = df['업체명'].apply(lambda x: re.sub("[^가-힣a-zA-Z0-9]", " ", x).strip())

        for _ in range(10):
            df['업체명_r'] = df['업체명_r'].str.replace(' {2,}', ' ', regex=True)

        df = df[df['업체명_r'] != ''].reset_index(drop=True)

        if not cased:
            df['업체명_r'] = df['업체명_r'].str.lower()

        return df

    elif isinstance(df, str):
        word = re.sub("[^가-힣a-zA-Z0-9]", " ", df).strip()
        if word == '':
            raise Exception('Cannot Preprocess an Empty String!')
        word = re.sub(' {2,}', ' ', word)
        return word


def preprocess_infer(df):
    if isinstance(df, pd.DataFrame):
        df.dropna(how='any', inplace=True)
        return regularize(df)
    elif isinstance(df, str):
        return regularize(df)


if __name__ == '__main__':
    pass
