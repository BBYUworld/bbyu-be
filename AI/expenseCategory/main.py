import json
import torch
from fastapi.responses import JSONResponse
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from model.inference_streamlit import load_model, inference
from model.modules.preprocess import preprocess_infer

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

app = FastAPI()

try:
    MODEL, TOKENIZER = load_model('KoBERT', 26, 'CrossEntropy', device)
except Exception as e:
    raise RuntimeError(f"Error loading model and tokenizer: {e}")


class InferenceData(BaseModel):
    place: str


@app.post("/ai/expense-category")
def inference_single(inp: InferenceData):
    store_name = inp.place

    if not store_name:
        raise HTTPException(status_code=400, detail="The place field is empty.")

    try:
        category, prediction_probability = inference(preprocess_infer(store_name), 26, MODEL, TOKENIZER, device)

        prediction_probability = round(float(prediction_probability), 2)

        result = {
            "category": category,
            "predictionProbability": prediction_probability
        }
        return JSONResponse(content=result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Prediction error: {e}")