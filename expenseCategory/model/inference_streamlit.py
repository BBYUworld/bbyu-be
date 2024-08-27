import os, torch
from transformers import AutoModelForSequenceClassification, AutoTokenizer
from model.modules.utils import load_yaml

# 루트 디렉터리 설정
PROJECT_DIR = os.path.dirname(os.path.abspath(os.path.dirname(__file__)))

# 설정 파일 로드
config_path = os.path.join(PROJECT_DIR, 'model', 'config', 'inference_config.yaml')
config = load_yaml(config_path)

def load_model(select_model, maxlen, loss, device):
    # 모델 이름 생성
    checkpoint_path = os.path.join(PROJECT_DIR, 'model', 'KoBERT_26_CrossEntropyLoss')
    pretrained_link = config.MODEL.pretrained_link[select_model]
    num_of_classes = config.MODEL.num_of_classes

    # 학습 없이 모델과 토크나이저 로드
    try:
        model = AutoModelForSequenceClassification.from_pretrained(checkpoint_path, num_labels=num_of_classes).to(device)
        tokenizer = AutoTokenizer.from_pretrained(pretrained_link)
    except Exception as e:
        raise RuntimeError(f"Error loading model or tokenizer: {e}")

    return model, tokenizer

def inference(data, max_seq_len, model, tokenizer, device):
    # 입력 데이터를 토큰화하고 텐서로 변환
    inputs = tokenizer(
        data,
        truncation=True,
        padding='max_length',
        max_length=max_seq_len,
        return_tensors='pt'
    ).to(device)

    # 모델을 평가 모드로 설정하고 예측 수행
    model.eval()
    with torch.no_grad():
        outputs = model(**inputs)
        probs = torch.nn.functional.softmax(outputs.logits, dim=-1)
        top_val, top_ind = torch.topk(probs, k=1, dim=1)
        top_val = top_val.cpu().numpy()[0][0]
        top_ind = top_ind.cpu().numpy()[0][0]

    # 상위 1개의 결과 반환
    return config.LABELING[top_ind], top_val
