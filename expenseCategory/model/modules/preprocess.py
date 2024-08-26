# preprocess.py
import re


def preprocess_infer(text):
    # regularize
    # 문자열에서 한글, 영문, 숫자만 남기고 나머지는 공백으로 대체합니다.
    text = re.sub("[^가-힣a-zA-Z0-9]", " ", text).strip()

    # 문자열이 비어있을 경우 예외를 발생시킵니다.
    if not text:
        raise Exception('Cannot preprocess an empty string!')

    # 공백이 2개 이상 연속될 경우 하나의 공백으로 대체합니다.
    text = re.sub(' {2,}', ' ', text)

    return text
