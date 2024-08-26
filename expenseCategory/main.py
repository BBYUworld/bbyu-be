import json
import torch
from fastapi.responses import JSONResponse
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from model.inference_streamlit import load_model, inference
from model.modules.preprocess import preprocess_infer

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

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
    store_name = inp.name

    if not store_name:
        raise HTTPException(status_code=400, detail="The name field is empty.")

    try:
        # 모델과 토크나이저 설정
        category, prediction_probability = inference(preprocess_infer(store_name), 26, MODEL, TOKENIZER, device)

        # prediction_probability를 float으로 변환하고 소수점 둘째 자리까지 반올림
        prediction_probability = round(float(prediction_probability), 2)

        # 결과를 JSON으로 변환하여 반환
        result = {
            "category": category,
            "predictionProbability": prediction_probability
        }
        return JSONResponse(content=result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Prediction error: {e}")
