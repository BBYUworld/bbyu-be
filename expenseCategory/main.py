import json
import torch
from fastapi.responses import JSONResponse
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import pandas as pd
from model.inference_streamlit import load_model, inference
from model.modules.preprocess import preprocess_infer

# 디바이스 설정 (CUDA 또는 CPU)
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

# FastAPI 애플리케이션 설정
app = FastAPI()

# KoBERT 모델과 토크나이저 로드
try:
    MODEL, TOKENIZER = load_model('KoBERT', 26, 'CrossEntropy', device)
except Exception as e:
    raise RuntimeError(f"Error loading model and tokenizer: {e}")

class InferenceData(BaseModel):
    name: str

@app.post("/api/expenseCategory")
def inference_single(inp: InferenceData):
    merch_name = inp.name

    if not merch_name:
        raise HTTPException(status_code=400, detail="name 필드는 비어 있을 수 없습니다.")

    try:
        # 모델과 토크나이저 설정
        cate, prob = inference(preprocess_infer(merch_name), 26, MODEL, TOKENIZER, device, topk=True)

        # 결과를 DataFrame으로 변환 및 JSON으로 반환
        result = pd.DataFrame({'업종': cate.flatten(), '예측확률': prob.flatten()})
        result_json = result.to_json(orient='records')
        return JSONResponse(content=json.loads(result_json))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Prediction error: {e}")
